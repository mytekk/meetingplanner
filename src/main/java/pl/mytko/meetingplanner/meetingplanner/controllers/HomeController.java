package pl.mytko.meetingplanner.meetingplanner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.mytko.meetingplanner.meetingplanner.models.Role;
import pl.mytko.meetingplanner.meetingplanner.models.User;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaUserRepository;
import pl.mytko.meetingplanner.meetingplanner.services.UserService;

import java.util.Iterator;
import java.util.Set;

@Controller
public class HomeController {

    private JpaUserRepository jpaUserRepository;
    private UserService userService;

    @Autowired
    public HomeController(JpaUserRepository jpaUserRepository, UserService userService) {
        this.jpaUserRepository = jpaUserRepository;
        this.userService = userService;
    }

    @GetMapping(path = {"/", "/home"})
    public String home() {
        return "home";
    }

    // "/login" POST controller is provided by Spring Security

    @GetMapping(path = "/login")
    public String login(Model model, String error, String logout) {

        if (error != null) {
            model.addAttribute("errorMessage", "Username or password is incorrent");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Logout successful");
        }

        return "login";
    }

    @GetMapping(path = "/dashboard")
    public String dashboard(Model model) {

        User currentLoggedUser = userService.getCurrentLoggedUser();
        model.addAttribute("user", currentLoggedUser);

        return "dashboard";
    }
}
