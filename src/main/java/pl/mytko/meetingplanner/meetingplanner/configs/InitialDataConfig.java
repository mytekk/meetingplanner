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
        Meeting spotkanie_pierwsze = new Meeting("spotkanie pierwsze",
                pracownik,
                LocalDateTime.of(2017, 10, 15, 10, 00, 00),
                LocalDateTime.of(2017, 10, 15, 11, 00, 00),
                new HashSet<User>(Arrays.asList(pracownik, drugiPracownik, trzeciPracownik)),
                project1,
                room1);
        jpaMeetingRepository.save(spotkanie_pierwsze);

        Meeting spotkanie_drugie = new Meeting("spotkanie pierwsze",
                pracownik,
                LocalDateTime.of(2017, 10, 20, 11, 00, 00),
                LocalDateTime.of(2017, 10, 20, 12, 00, 00),
                new HashSet<User>(Arrays.asList(pracownik, drugiPracownik, trzeciPracownik)),
                project1,
                room2);
        jpaMeetingRepository.save(spotkanie_drugie);

        Meeting spotkanie_trzecie = new Meeting("spotkanie drugie",
                drugiPracownik,
                LocalDateTime.of(2017, 10, 21, 13, 00, 00),
                LocalDateTime.of(2017, 10, 21, 14, 15, 00),
                new HashSet<User>(Arrays.asList(pracownik, drugiPracownik, trzeciPracownik)),
                project1,
                room2);
        jpaMeetingRepository.save(spotkanie_trzecie);

        Meeting spotkanie_czwarte = new Meeting("spotkanie trzecie",
                drugiPracownik,
                LocalDateTime.of(2017, 10, 16, 9, 00, 00),
                LocalDateTime.of(2017, 10, 16, 11, 00, 00),
                new HashSet<User>(Arrays.asList(pracownik, drugiPracownik, trzeciPracownik)),
                project2,
                room3);
        jpaMeetingRepository.save(spotkanie_czwarte);

        Meeting spotkanie_piate = new Meeting("spotkanie pierwsze",
                pracownik,
                LocalDateTime.of(2017, 10, 20, 10, 00, 00),
                LocalDateTime.of(2017, 10, 20, 11, 30, 00),
                new HashSet<User>(Arrays.asList(pracownik, drugiPracownik, trzeciPracownik)),
                project3,
                room4);
        jpaMeetingRepository.save(spotkanie_piate);

        System.out.println("SPOTKANIE PIERWSZE PRZED ZAPISEM");
        System.out.println(spotkanie_pierwsze);

        System.out.println("PO ZAPISIE DO BAZY");
        Iterable<Meeting> repositoryAll = jpaMeetingRepository.findAll();
        Iterator<Meeting> iterator = repositoryAll.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }

}