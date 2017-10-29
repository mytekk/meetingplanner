package pl.mytko.meetingplanner.meetingplanner.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.mytko.meetingplanner.meetingplanner.models.Project;
import pl.mytko.meetingplanner.meetingplanner.models.Role;
import pl.mytko.meetingplanner.meetingplanner.models.User;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaProjectRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaRoleRepository;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaUserRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
public class InitialDataConfig {

    private JpaRoleRepository jpaRoleRepository;
    private JpaUserRepository jpaUserRepository;
    private JpaProjectRepository jpaProjectRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public InitialDataConfig(JpaRoleRepository jpaRoleRepository, JpaUserRepository jpaUserRepository, JpaProjectRepository jpaProjectRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jpaRoleRepository = jpaRoleRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaProjectRepository = jpaProjectRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void init() {

        //role
        Role adminRole = new Role("ROLE_ADMIN");
        Role employeeRole = new Role("ROLE_EMPLOYEE");

        jpaRoleRepository.save(adminRole);
        jpaRoleRepository.save(employeeRole);

        //uzytkownicy
        Set<Role> adminRoleSet = new HashSet<>();
        adminRoleSet.add(jpaRoleRepository.findByName("ROLE_ADMIN"));

        Set<Role> employeeRoleSet = new HashSet<>();
        employeeRoleSet.add(jpaRoleRepository.findByName("ROLE_EMPLOYEE"));


        User administrator = new User("admin", "admin", "Janek", "Administracyjny");
        administrator.setPassword(bCryptPasswordEncoder.encode(administrator.getPassword()));
        administrator.setRoles(adminRoleSet);
        jpaUserRepository.save(administrator);

        User pracownik = new User("pracownik", "pracownik", "Piotr", "Pracownikowy");
        pracownik.setPassword(bCryptPasswordEncoder.encode(pracownik.getPassword()));
        pracownik.setRoles(employeeRoleSet);
        jpaUserRepository.save(pracownik);

        User drugiPracownik = new User("mytek", "mytek", "Bartek", "Mytko");
        drugiPracownik.setPassword(bCryptPasswordEncoder.encode(drugiPracownik.getPassword()));
        drugiPracownik.setRoles(employeeRoleSet);
        jpaUserRepository.save(drugiPracownik);

        User trzeciPracownik = new User("maria", "maria", "Maria", "Mytko");
        trzeciPracownik.setPassword(bCryptPasswordEncoder.encode(trzeciPracownik.getPassword()));
        trzeciPracownik.setRoles(employeeRoleSet);
        jpaUserRepository.save(trzeciPracownik);

        User czwartyPracownik = new User("mietek", "mietek", "Mieczysław", "Mytko");
        czwartyPracownik.setPassword(bCryptPasswordEncoder.encode(czwartyPracownik.getPassword()));
        czwartyPracownik.setRoles(employeeRoleSet);
        jpaUserRepository.save(czwartyPracownik);

        //projekty
        Project project1 = new Project(pracownik, "e-poznan portal internetowy", "projekt polega na przebudowaniu strony epoznan.pl");
        jpaProjectRepository.save(project1);

        Project project2 = new Project(drugiPracownik, "blog kulinarny", "projekt dotyczy stworzenia bloga kulinarnego dla Jana Kowalskiego");
        jpaProjectRepository.save(project2);

        Project project3 = new Project(drugiPracownik, "forum dla kobiet w ciąży", "zaprojektowanie forum internetowego dla ciężarnych");
        jpaProjectRepository.save(project3);

        Project project4 = new Project(trzeciPracownik, "portal z cenami paliw", "stworzenie strony z cenami paliw z danego miasta");
        jpaProjectRepository.save(project4);

        //dodanie projektow do pracownikow
        pracownik.setProjects(new ArrayList<Project>(Arrays.asList(project1, project2, project4)));
        jpaUserRepository.save(pracownik);

        drugiPracownik.setProjects(new ArrayList<Project>(Arrays.asList(project1, project2, project3)));
        jpaUserRepository.save(drugiPracownik);

        trzeciPracownik.setProjects(new ArrayList<Project>(Arrays.asList(project1, project2, project3, project4)));
        jpaUserRepository.save(trzeciPracownik);

        czwartyPracownik.setProjects(new ArrayList<Project>(Arrays.asList(project2, project4)));
        jpaUserRepository.save(czwartyPracownik);

    }

}