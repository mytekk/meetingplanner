package pl.mytko.meetingplanner.meetingplanner.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @Length.List({
            @Length(min = 3),
            @Length(max = 10)
    })
    private String number;

    @Column
    @Length.List({
            @Length(min = 3),
            @Length(max = 50)
    })
    private String description;

    @OneToMany(mappedBy = "room")
    private Set<Meeting> meetings;
}
