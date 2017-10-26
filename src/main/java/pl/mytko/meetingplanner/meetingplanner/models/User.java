package pl.mytko.meetingplanner.meetingplanner.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @Length.List({
            @Length(min = 3),
            @Length(max = 50)
    })
    private String firstName;

    @Column
    @NotNull
    @Length.List({
            @Length(min = 3),
            @Length(max = 50)
    })
    private String lastName;
}
