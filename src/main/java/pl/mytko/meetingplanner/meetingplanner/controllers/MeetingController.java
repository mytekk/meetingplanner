package pl.mytko.meetingplanner.meetingplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mytko.meetingplanner.meetingplanner.models.Meeting;
import pl.mytko.meetingplanner.meetingplanner.models.Project;
import pl.mytko.meetingplanner.meetingplanner.models.User;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaMeetingRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaProjectRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaRoomRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaUserRepository;
import pl.mytko.meetingplanner.meetingplanner.services.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/meetings")
public class MeetingController {

    private JpaProjectRepository jpaProjectRepository;
    private JpaUserRepository jpaUserRepository;
    private JpaMeetingRepository jpaMeetingRepository;
    private JpaRoomRepository jpaRoomRepository;
    private UserService userService;

    @Autowired
    public MeetingController(JpaProjectRepository jpaProjectRepository,
                             JpaUserRepository jpaUserRepository,
                             JpaMeetingRepository jpaMeetingRepository,
                             JpaRoomRepository jpaRoomRepository,
                             UserService userService) {
        this.jpaProjectRepository = jpaProjectRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaMeetingRepository = jpaMeetingRepository;
        this.jpaRoomRepository = jpaRoomRepository;
        this.userService = userService;
    }

    @GetMapping(path = "/all")
    public String all(Model model) {
        model.addAttribute("allMeetings", jpaMeetingRepository.findAll());
        model.addAttribute("pageTitle", "All meetings");

        return "meetings";
    }

    @GetMapping(path = "/my")
    public String my(Model model) {

        User currentLoggedUser = userService.getCurrentLoggedUser();

        model.addAttribute("allMeetings", jpaMeetingRepository.findByOwner(currentLoggedUser));
        model.addAttribute("pageTitle", "My meetings");

        return "meetings";
    }

    @GetMapping(path = "/involved")
    public String involved(Model model) {

        User currentLoggedUser = userService.getCurrentLoggedUser();
        List<Meeting> myMeetings = (List<Meeting>) jpaMeetingRepository.findByParticipantOfparticipants(currentLoggedUser);

        model.addAttribute("allMeetings", myMeetings);
        model.addAttribute("pageTitle", "Meetings that I am involved into");

        return "meetings";
    }

    @GetMapping(path = "/details/{value}")
    public String details(Model model, @PathVariable("value") String param) {

        model.addAttribute("meeting", jpaMeetingRepository.findOne(Long.valueOf(param)));
        return "meeting";
    }

    @PostMapping(value = "/delete/{meetingId}")
    public String deleteSingleMeeting(Model model, @PathVariable("meetingId") String meetingId, RedirectAttributes redir) {

        User currentLoggedUser = userService.getCurrentLoggedUser();

        Meeting meetingToDelete = jpaMeetingRepository.findOne(Long.valueOf(meetingId));
        String message = "";
        String redirectString = "";

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfMeetingToDelete = meetingToDelete.getEnd();
        User owner = meetingToDelete.getOwner();
        Set<User> participants = meetingToDelete.getParticipants();

        if (currentLoggedUser.equals(owner) || participants.contains(currentLoggedUser)) {
            if (endOfMeetingToDelete.isAfter(now)) {
                message = "This meeting cannot be deleted, it hasn't finished yet.";
                redirectString = "redirect:/meetings/details/" + meetingId;
            } else {
                jpaMeetingRepository.delete(Long.valueOf(meetingId));
                message = "Meeting deleted";
                redirectString = "redirect:/meetings/involved";
            }
        } else {
            message = "You cannot delete this meeting because your are not owner of this meting nor a participant.";
            redirectString = "redirect:/meetings/details/" + meetingId;
        }

        redir.addFlashAttribute("message", message);

        return redirectString;
    }

    @GetMapping(path = "/add")
    public String add(Model model) {

        model.addAttribute("pageTitle", "Ading new meeting");
        model.addAttribute("owner", userService.getCurrentLoggedUser());
        model.addAttribute("currentDateTime", LocalDateTime.now());
        model.addAttribute("defaultEndDateTime", LocalDateTime.now().plusHours(1));
        model.addAttribute("availableProjects", jpaProjectRepository.findAll());
        model.addAttribute("availableRooms", jpaRoomRepository.findAll());
        model.addAttribute("availableParticipants", jpaUserRepository.findAll());
        model.addAttribute("meeting", new Meeting()); //bez tego bedzie org.thymeleaf.spring4.processor.attr.SpringInputGeneralFieldAttrProcessor

        return "newMeeting";
    }

    public boolean validateMeetingDates(LocalDateTime begining, LocalDateTime end) {
        return begining.isBefore(end);
    }

    public boolean validateMeetingDuration(LocalDateTime begining, LocalDateTime end) {
        long between = ChronoUnit.MINUTES.between(begining, end);
        return (between >= 15 && between <= 120) ? true : false;
    }

    @PostMapping(path = "/add")
    public String addNew(@ModelAttribute Meeting meeting, BindingResult result, Model model, HttpServletRequest request, RedirectAttributes redir) {

        meeting.setOwner(userService.getCurrentLoggedUser());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTimeStart = LocalDateTime.parse(request.getParameter("begining-manual"), formatter);
        LocalDateTime dateTimeStop = LocalDateTime.parse(request.getParameter("end-manual"), formatter);

        meeting.setBegining(dateTimeStart);
        meeting.setEnd(dateTimeStop);

        if (validateMeetingDates(meeting.getBegining(), meeting.getEnd())) {
            if (validateMeetingDuration(meeting.getBegining(), meeting.getEnd())) {
                jpaMeetingRepository.save(meeting);
                redir.addFlashAttribute("message", "Meeting added");
            } else {
                redir.addFlashAttribute("message", "Meeting duration must be between 15 and 120 minutes");
            }
        } else {
            redir.addFlashAttribute("message", "Begining date must be before ending date");
        }




        return "redirect:/meetings/my";
    }

}
