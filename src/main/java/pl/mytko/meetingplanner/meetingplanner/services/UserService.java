package pl.mytko.meetingplanner.meetingplanner.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.mytko.meetingplanner.meetingplanner.models.User;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaUserRepository;

@Component
public class UserService {

    private JpaUserRepository jpaUserRepository;

    @Autowired
    public UserService(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    public User getCurrentLoggedUser() {
        //z sesji pobieramy "springowego usera"
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //wyszukuje "mojego" usera na podstawie usename springowego usera - bo username musi byc unikalne
        User user = jpaUserRepository.findByUsername(principal.getUsername());

        return user;
    }
}
