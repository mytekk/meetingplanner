package pl.mytko.meetingplanner.meetingplanner.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.mytko.meetingplanner.meetingplanner.models.Role;

public interface JpaRoleRepository extends CrudRepository<Role, Long> {
}
