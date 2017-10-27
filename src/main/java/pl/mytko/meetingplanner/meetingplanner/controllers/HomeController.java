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

import java.util.Iterator;
import java.util.Set;

@Controller
public class HomeController {

    private JpaUserRepository jpaUserRepository;

    @Autowired
    public HomeController(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @GetMapping(path = {"/", "/home"})
    public String home() {
        return "home";
    }

    // "/login" POST controller is provided by Spring Security

    @GetMapping(path = "/login")
    public String login(Model model, String error, String logout) {
//        System.out.println("jestem w endpoincie!");
//        System.out.println("error message: " + error);
//        System.out.println("logout message: " + logout);
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

        //z sesji pobieramy "springowego usera"
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //wyszukuje "mojego" usera na podstawie usename springowego usera - bo username musi byc unikalne
        User user = jpaUserRepository.findByUsername(principal.getUsername());

        Set<Role> roles = user.getRoles();

//        Iterator<Role> it = roles.iterator();
//        while(it.hasNext()){
//            System.out.println(it.next());
//        }

        model.addAttribute("user", user);

        return "dashboard";
    }
}
