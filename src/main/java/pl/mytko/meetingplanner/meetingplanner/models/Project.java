package pl.mytko.meetingplanner.meetingplanner.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
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

    @ManyToMany(mappedBy = "projects", fetch = FetchType.EAGER)
    private List<User> members;

    @OneToMany(mappedBy = "project")
    private Set<Meeting> meetings;

    //==============================


    public Project(User owner, String title, String description, List<User> members) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.members = members;
    }

    public Project() {
    }

    public Project(User owner, String title, String description, List<User> members, Set<Meeting> meetings) {
        this.owner = owner;
        this.title = title;
        this.description = description;
        this.members = members;
        this.meetings = meetings;
    }

    //==================================


    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<User> getMembers() {
        return members;
    }

    public Set<Meeting> getMeetings() {
        return meetings;
    }

    //=====================================

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }


    //=====================================

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", owner=" + owner +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", members=" + members +
                '}';
    }

}
