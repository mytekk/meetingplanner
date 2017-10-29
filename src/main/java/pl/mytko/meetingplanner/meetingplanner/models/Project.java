package pl.mytko.meetingplanner.meetingplanner.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column
    @NotNull
    @Length.List({
            @Length(min = 3),
            @Length(max = 50)
    })
    private String title;

    @Column
    @NotNull
    @Length.List({
            @Length(min = 3),
            @Length(max = 200)
    })
    private String description;

    @ManyToMany(mappedBy = "projects")
    private Set<User> members;

    @OneToMany(mappedBy = "project")
    private Set<Meeting> meetings;
}
