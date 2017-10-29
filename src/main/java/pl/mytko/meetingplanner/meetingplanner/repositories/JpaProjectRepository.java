package pl.mytko.meetingplanner.meetingplanner.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.mytko.meetingplanner.meetingplanner.models.Project;

public interface JpaProjectRepository extends CrudRepository<Project, Long> {
}
