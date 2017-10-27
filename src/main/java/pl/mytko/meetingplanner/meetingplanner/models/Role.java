package pl.mytko.meetingplanner.meetingplanner.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @Size(min = 3, max = 30)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    //=================================

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    //=================================

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }
}
