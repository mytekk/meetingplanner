package pl.mytko.meetingplanner.meetingplanner.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.mytko.meetingplanner.meetingplanner.models.Project;
import pl.mytko.meetingplanner.meetingplanner.models.User;

import java.util.List;

public interface JpaProjectRepository extends CrudRepository<Project, Long> {

    List<Project> findByOwner(User owner);
}
