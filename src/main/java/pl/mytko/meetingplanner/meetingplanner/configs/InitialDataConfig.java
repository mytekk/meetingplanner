package pl.mytko.meetingplanner.meetingplanner.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.mytko.meetingplanner.meetingplanner.models.Role;
import pl.mytko.meetingplanner.meetingplanner.models.User;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaRoleRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaUserRepository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class InitialDataConfig {

    private JpaRoleRepository jpaRoleRepository;
    private JpaUserRepository jpaUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public InitialDataConfig(JpaRoleRepository jpaRoleRepository, JpaUserRepository jpaUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jpaRoleRepository = jpaRoleRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void init() {

        //role
        Role adminRole = new Role("ADMIN");
        Role employeeRole = new Role("EMPLOYEE");

        jpaRoleRepository.save(adminRole);
        jpaRoleRepository.save(employeeRole);

        //uzytkownicy
        User administrator = new User("admin", "admin", "Janek", "Administracyjny");
        administrator.setPassword(bCryptPasswordEncoder.encode(administrator.getPassword()));
        Set<Role> adminRoleSet = new HashSet<>();
        adminRoleSet.add(jpaRoleRepository.findByName("ADMIN"));
        administrator.setRoles(adminRoleSet);
        jpaUserRepository.save(administrator);

        User pracownik = new User("pracownik", "pracownik", "Piotr", "Pracownikowy");
        pracownik.setPassword(bCryptPasswordEncoder.encode(pracownik.getPassword()));
        Set<Role> employeeRoleSet = new HashSet<>();
        employeeRoleSet.add(jpaRoleRepository.findByName("EMPLOYEE"));
        pracownik.setRoles(employeeRoleSet);
        jpaUserRepository.save(pracownik);

    }

}