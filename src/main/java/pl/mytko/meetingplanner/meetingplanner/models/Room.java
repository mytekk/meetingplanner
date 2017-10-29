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

    //===================================


    public Room(String number, String description) {
        this.number = number;
        this.description = description;
    }

    public Room() {
    }

    //===================================


    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    //===============================


    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }
}
