package pl.mytko.meetingplanner.meetingplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mytko.meetingplanner.meetingplanner.models.Meeting;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaMeetingRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaProjectRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaUserRepository;

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

}
