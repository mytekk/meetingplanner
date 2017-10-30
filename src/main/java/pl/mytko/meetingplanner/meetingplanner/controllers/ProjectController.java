package pl.mytko.meetingplanner.meetingplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mytko.meetingplanner.meetingplanner.models.Project;
import pl.mytko.meetingplanner.meetingplanner.models.User;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaProjectRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaUserRepository;
import pl.mytko.meetingplanner.meetingplanner.services.UserService;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private JpaProjectRepository jpaProjectRepository;
    private JpaUserRepository jpaUserRepository;
    private UserService userService;

    @Autowired
    public ProjectController(JpaProjectRepository jpaProjectRepository, JpaUserRepository jpaUserRepository, UserService userService) {
        this.jpaProjectRepository = jpaProjectRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.userService = userService;
    }

    @GetMapping(path = "/all")
    public String all(Model model) {
        model.addAttribute("allProjects", jpaProjectRepository.findAll());
        model.addAttribute("pageTitle", "All projects");

        return "projects";
    }

    @GetMapping(path = "/my")
    public String my(Model model) {

        User currentLoggedUser = userService.getCurrentLoggedUser();

        model.addAttribute("allProjects", jpaProjectRepository.findByOwner(currentLoggedUser));
        model.addAttribute("pageTitle", "My projects");

        return "projects";
    }

    @GetMapping(path = "/involved")
    public String involved(Model model) {

        User currentLoggedUser = userService.getCurrentLoggedUser();

        List<Project> myProjects = (List<Project>) jpaProjectRepository.findByMemberOfMembers(currentLoggedUser);

        model.addAttribute("allProjects", myProjects);
        model.addAttribute("pageTitle", "Projects that I am involved into");

        return "projects";
    }

    @GetMapping(path = "/details/{value}")
    public String details(Model model, @PathVariable("value") String param) {

        model.addAttribute("project", jpaProjectRepository.findOne(Long.valueOf(param)));
        return "project";
    }

    @PostMapping(value = "/delete/{projectId}")
    public String deleteSingleProject(@PathVariable("projectId") String projectId) {

        jpaProjectRepository.delete(Long.valueOf(projectId));

        return "redirect:/projects/involved";
    }

}
