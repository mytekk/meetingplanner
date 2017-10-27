package pl.mytko.meetingplanner.meetingplanner.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping(path = {"/", "/home"})
    public String home() {
        return "home";
    }

    // "/login" POST controller is provided by Spring Security

    @GetMapping(path = "/login")
    public String login(Model model, String error, String logout) {
        System.out.println("jestem w endpoincie!");
        System.out.println("error message: " + error);
        System.out.println("logout message: " + logout);
        if (error != null) {
            model.addAttribute("errorMessage", "Username or password is incorrent");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Logout successful");
        }

        return "login";
    }
}
