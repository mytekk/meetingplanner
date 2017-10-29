package pl.mytko.meetingplanner.meetingplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaProjectRepository;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private JpaProjectRepository jpaProjectRepository;

    @Autowired
    public ProjectController(JpaProjectRepository jpaProjectRepository) {
        this.jpaProjectRepository = jpaProjectRepository;
    }

    @GetMapping(path = "/all")
    public String all(Model model) {
        model.addAttribute("allProjects", jpaProjectRepository.findAll());
        model.addAttribute("pageTitle", "All projects");
        return "projects";
    }
}
