package pl.mytko.meetingplanner.meetingplanner.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.mytko.meetingplanner.meetingplanner.models.Project;
import pl.mytko.meetingplanner.meetingplanner.models.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface JpaProjectRepository extends CrudRepository<Project, Long> {

    List<Project> findByOwner(User owner);

    @Query("SELECT p FROM Project p WHERE :user MEMBER OF p.members")
    List<Project> findByMemberOfMembers(@Param("user") User user);

    List<Project> findByMembers_username(String username);

}
