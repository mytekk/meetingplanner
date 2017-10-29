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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_meeting",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "meeting_id")
    )
    private Set<User> participants;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    //=============================


    public Meeting(String title, User owner, LocalDateTime begining, LocalDateTime end, Set<User> participants, Project project, Room room) {
        this.title = title;
        this.owner = owner;
        this.begining = begining;
        this.end = end;
        this.participants = participants;
        this.project = project;
        this.room = room;
    }

    public Meeting() {
    }

    //============================


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getOwner() {
        return owner;
    }

    public LocalDateTime getBegining() {
        return begining;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public Project getProject() {
        return project;
    }

    public Room getRoom() {
        return room;
    }

    //===============================


    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setBegining(LocalDateTime begining) {
        this.begining = begining;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    //=================================


    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", owner=" + owner +
                ", begining=" + begining +
                ", end=" + end +
                ", participants=" + participants +
                ", project=" + project +
                ", room=" + room +
                '}';
    }
}
