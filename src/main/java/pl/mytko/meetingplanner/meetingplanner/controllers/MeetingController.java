package pl.mytko.meetingplanner.meetingplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.mytko.meetingplanner.meetingplanner.models.Meeting;
import pl.mytko.meetingplanner.meetingplanner.models.Project;
import pl.mytko.meetingplanner.meetingplanner.models.User;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaMeetingRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaProjectRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaUserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/meetings")
public class MeetingController {

    private JpaProjectRepository jpaProjectRepository;
    private JpaUserRepository jpaUserRepository;
    private JpaMeetingRepository jpaMeetingRepository;

    @Autowired
    public MeetingController(JpaProjectRepository jpaProjectRepository,
                              JpaUserRepository jpaUserRepository,
                              JpaMeetingRepository jpaMeetingRepository) {
        this.jpaProjectRepository = jpaProjectRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaMeetingRepository = jpaMeetingRepository;
    }

    @GetMapping(path = "/all")
    public String all(Model model) {
        model.addAttribute("allMeetings", jpaMeetingRepository.findAll());
        model.addAttribute("pageTitle", "All meetings");

        return "meetings";
    }

    @GetMapping(path = "/my")
    public String my(Model model) {
        //z sesji pobieramy "springowego usera"
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //wyszukuje "mojego" usera na podstawie usename springowego usera - bo username musi byc unikalne
        User user = jpaUserRepository.findByUsername(principal.getUsername());

        model.addAttribute("allMeetings", jpaMeetingRepository.findByOwner(user));
        model.addAttribute("pageTitle", "My meetings");

        return "meetings";
    }

    @GetMapping(path = "/involved")
    public String involved(Model model) {
        //z sesji pobieramy "springowego usera"
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //wyszukuje "mojego" usera na podstawie usename springowego usera - bo username musi byc unikalne
        User user = jpaUserRepository.findByUsername(principal.getUsername());

        List<Meeting> myMeetings = (List<Meeting>) jpaMeetingRepository.findByParticipantOfparticipants(user);

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

        //z sesji pobieramy "springowego usera"
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //wyszukuje "mojego" usera na podstawie usename springowego usera - bo username musi byc unikalne
        User user = jpaUserRepository.findByUsername(principal.getUsername());

        Meeting meetingToDelete = jpaMeetingRepository.findOne(Long.valueOf(meetingId));
        String message = "";
        String redirectString = "";

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfMeetingToDelete = meetingToDelete.getEnd();
        User owner = meetingToDelete.getOwner();
        Set<User> participants = meetingToDelete.getParticipants();

        if (user.equals(owner) || participants.contains(user)) {
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

        redir.addFlashAttribute("message",message);

        return redirectString;
    }

}
