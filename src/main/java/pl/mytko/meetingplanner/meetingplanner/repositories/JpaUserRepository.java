package pl.mytko.meetingplanner.meetingplanner.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.mytko.meetingplanner.meetingplanner.models.User;

public interface JpaUserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
