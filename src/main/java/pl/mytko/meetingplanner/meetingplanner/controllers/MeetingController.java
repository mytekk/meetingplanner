package pl.mytko.meetingplanner.meetingplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping(path = "/add")
    public String addNew(Model model, @ModelAttribute Meeting meeting, HttpServletRequest request) {

        System.out.println("JESTEM W POST");
        System.out.println("Id: " + meeting.getId());
        System.out.println("Title: " + meeting.getTitle());
        System.out.println("Owner: " + meeting.getOwner());
        System.out.println("Begining: " + meeting.getBegining());
        System.out.println("End: " + meeting.getEnd());
        System.out.println("Participants: " + meeting.getParticipants());
        System.out.println("Project: " + meeting.getProject());
        System.out.println("Room: " + meeting.getRoom());
        System.out.println("--------------------");
        System.out.println(request.getParameter("owner"));
        System.out.println("--------------------");
        System.out.println(request.getParameter("owner2"));
        System.out.println("--------------------");
        System.out.println(request.getParameter("begining-manual"));
        System.out.println("--------------------");
        System.out.println(request.getParameter("end-manual"));

        meeting.setOwner(userService.getCurrentLoggedUser());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTimeStart = LocalDateTime.parse(request.getParameter("begining-manual"), formatter);
        LocalDateTime dateTimeStop = LocalDateTime.parse(request.getParameter("end-manual"), formatter);

        meeting.setBegining(dateTimeStart);
        meeting.setEnd(dateTimeStop);

        System.out.println("PO ZMIANACH");
        System.out.println("Id: " + meeting.getId());
        System.out.println("Title: " + meeting.getTitle());
        System.out.println("Owner: " + meeting.getOwner());
        System.out.println("Begining: " + meeting.getBegining());
        System.out.println("End: " + meeting.getEnd());
        System.out.println("Participants: " + meeting.getParticipants());
        System.out.println("Project: " + meeting.getProject());
        System.out.println("Room: " + meeting.getRoom());


        jpaMeetingRepository.save(meeting);


        return "newMeeting";
    }

}
