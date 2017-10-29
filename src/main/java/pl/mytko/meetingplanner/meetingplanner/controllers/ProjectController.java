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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private JpaProjectRepository jpaProjectRepository;
    private JpaUserRepository jpaUserRepository;

    @Autowired
    public ProjectController(JpaProjectRepository jpaProjectRepository, JpaUserRepository jpaUserRepository) {
        this.jpaProjectRepository = jpaProjectRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @GetMapping(path = "/all")
    public String all(Model model) {
        model.addAttribute("allProjects", jpaProjectRepository.findAll());
        model.addAttribute("pageTitle", "All projects");

        return "projects";
    }

    @GetMapping(path = "/my")
    public String my(Model model) {
        //z sesji pobieramy "springowego usera"
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //wyszukuje "mojego" usera na podstawie usename springowego usera - bo username musi byc unikalne
        User user = jpaUserRepository.findByUsername(principal.getUsername());

        model.addAttribute("allProjects", jpaProjectRepository.findByOwner(user));
        model.addAttribute("pageTitle", "My projects");

        return "projects";
    }

    @GetMapping(path = "/involved")
    public String involved(Model model) {
        //z sesji pobieramy "springowego usera"
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //wyszukuje "mojego" usera na podstawie usename springowego usera - bo username musi byc unikalne
        User user = jpaUserRepository.findByUsername(principal.getUsername());

        List<Project> myProjects = (List<Project>) jpaProjectRepository.findByMemberOfMembers(user);

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
