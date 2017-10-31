package pl.mytko.meetingplanner.meetingplanner.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.mytko.meetingplanner.meetingplanner.models.*;
import pl.mytko.meetingplanner.meetingplanner.repositories.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

@Configuration
public class InitialDataConfig {

    private JpaRoleRepository jpaRoleRepository;
    private JpaUserRepository jpaUserRepository;
    private JpaProjectRepository jpaProjectRepository;
    private JpaRoomRepository jpaRoomRepository;
    private JpaMeetingRepository jpaMeetingRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public InitialDataConfig(JpaRoleRepository jpaRoleRepository,
                             JpaUserRepository jpaUserRepository,
                             JpaProjectRepository jpaProjectRepository,
                             JpaRoomRepository jpaRoomRepository,
                             JpaMeetingRepository jpaMeetingRepository,
                             BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jpaRoleRepository = jpaRoleRepository;
        this.jpaUserRepository = jpaUserRepository;
        this.jpaProjectRepository = jpaProjectRepository;
        this.jpaRoomRepository = jpaRoomRepository;
        this.jpaMeetingRepository = jpaMeetingRepository;
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

        User employee_1 = new User("pracownik", "pracownik", "Piotr", "Pracownikowy");
        employee_1.setPassword(bCryptPasswordEncoder.encode(employee_1.getPassword()));
        employee_1.setRoles(employeeRoleSet);
        jpaUserRepository.save(employee_1);

        User employee_2 = new User("mytek", "mytek", "Bartek", "Mytko");
        employee_2.setPassword(bCryptPasswordEncoder.encode(employee_2.getPassword()));
        employee_2.setRoles(employeeRoleSet);
        jpaUserRepository.save(employee_2);

        User employee_3 = new User("maria", "maria", "Maria", "Mytko");
        employee_3.setPassword(bCryptPasswordEncoder.encode(employee_3.getPassword()));
        employee_3.setRoles(employeeRoleSet);
        jpaUserRepository.save(employee_3);

        User employee_4 = new User("mietek", "mietek", "Mieczysław", "Mytko");
        employee_4.setPassword(bCryptPasswordEncoder.encode(employee_4.getPassword()));
        employee_4.setRoles(employeeRoleSet);
        jpaUserRepository.save(employee_4);

        //projekty
        Project project1 = new Project(employee_1, "e-poznan portal internetowy", "projekt polega na przebudowaniu strony epoznan.pl");
        jpaProjectRepository.save(project1);

        Project project2 = new Project(employee_2, "blog kulinarny", "projekt dotyczy stworzenia bloga kulinarnego dla Jana Kowalskiego");
        jpaProjectRepository.save(project2);

        Project project3 = new Project(employee_2, "forum dla kobiet w ciąży", "zaprojektowanie forum internetowego dla ciężarnych");
        jpaProjectRepository.save(project3);

        Project project4 = new Project(employee_3, "portal z cenami paliw", "stworzenie strony z cenami paliw z danego miasta");
        jpaProjectRepository.save(project4);

        //dodanie projektow do pracownikow
        employee_1.setProjects(new HashSet<Project>(Arrays.asList(project1, project2, project4)));
        jpaUserRepository.save(employee_1);

        employee_2.setProjects(new HashSet<Project>(Arrays.asList(project1, project2, project3)));
        jpaUserRepository.save(employee_2);

        employee_3.setProjects(new HashSet<Project>(Arrays.asList(project1, project2, project3, project4)));
        jpaUserRepository.save(employee_3);

        employee_4.setProjects(new HashSet<Project>(Arrays.asList(project2, project4)));
        jpaUserRepository.save(employee_4);

        //sale
        Room room1 = new Room("0-1", "salka nr 1");
        Room room2 = new Room("0-2", "salka nr 2");
        Room room3 = new Room("0-3", "salka nr 3");
        Room room4 = new Room("0-4", "salka nr 4");
        Room room5 = new Room("I-4", "salka nr 4 na piętrze");

        jpaRoomRepository.save(room1);
        jpaRoomRepository.save(room2);
        jpaRoomRepository.save(room3);
        jpaRoomRepository.save(room4);
        jpaRoomRepository.save(room5);

        //spotkania
        Meeting spotkanie_pierwsze = new Meeting("spotkanie wstępne (projekt 1)",
                employee_1,
                LocalDateTime.of(2017, 10, 15, 10, 00, 00),
                LocalDateTime.of(2017, 10, 15, 11, 00, 00),
                new HashSet<User>(Arrays.asList(employee_1, employee_2, employee_3)),
                project1,
                room1);
        jpaMeetingRepository.save(spotkanie_pierwsze);

        Meeting spotkanie_drugie = new Meeting("spotkanie pierwsze (projekt 1)",
                employee_1,
                LocalDateTime.of(2017, 10, 20, 11, 00, 00),
                LocalDateTime.of(2017, 10, 20, 12, 00, 00),
                new HashSet<User>(Arrays.asList(employee_1, employee_2, employee_3)),
                project1,
                room2);
        jpaMeetingRepository.save(spotkanie_drugie);

        Meeting spotkanie_trzecie = new Meeting("spotkanie drugie (projekt 1)",
                employee_2,
                LocalDateTime.of(2017, 10, 21, 13, 00, 00),
                LocalDateTime.of(2017, 10, 21, 14, 15, 00),
                new HashSet<User>(Arrays.asList(employee_2, employee_3, employee_4)),
                project1,
                room2);
        jpaMeetingRepository.save(spotkanie_trzecie);

        Meeting spotkanie_czwarte = new Meeting("spotkanie trzecie (projekt 2)",
                employee_2,
                LocalDateTime.of(2017, 10, 16, 9, 00, 00),
                LocalDateTime.of(2017, 10, 16, 11, 00, 00),
                new HashSet<User>(Arrays.asList(employee_2, employee_3)),
                project2,
                room3);
        jpaMeetingRepository.save(spotkanie_czwarte);

        Meeting spotkanie_piate = new Meeting("spotkanie pierwsze (projekt 3)",
                employee_1,
                LocalDateTime.of(2017, 10, 20, 10, 00, 00),
                LocalDateTime.of(2017, 10, 20, 11, 30, 00),
                new HashSet<User>(Arrays.asList(employee_1, employee_2, employee_3, employee_4)),
                project3,
                room4);
        jpaMeetingRepository.save(spotkanie_piate);

        Meeting spotkanie_szoste = new Meeting("spotkanie drugie (projekt 3)",
                employee_1,
                LocalDateTime.of(2017, 12, 20, 10, 00, 00),
                LocalDateTime.of(2017, 12, 20, 11, 30, 00),
                new HashSet<User>(Arrays.asList(employee_1, employee_2, employee_3, employee_4)),
                project3,
                room4);
        jpaMeetingRepository.save(spotkanie_szoste);


    }

}