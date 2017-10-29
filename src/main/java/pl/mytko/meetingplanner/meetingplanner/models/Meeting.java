package pl.mytko.meetingplanner.meetingplanner.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @Size(min = 3, max = 30)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column
    @NotNull
    private LocalDateTime begining;

    @Column
    @NotNull
    private LocalDateTime end;

    @ManyToMany(mappedBy = "meetings")
    private Set<User> participants;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

}
