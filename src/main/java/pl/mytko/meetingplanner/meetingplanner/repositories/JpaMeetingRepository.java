package pl.mytko.meetingplanner.meetingplanner.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.mytko.meetingplanner.meetingplanner.models.Meeting;
import pl.mytko.meetingplanner.meetingplanner.models.Project;
import pl.mytko.meetingplanner.meetingplanner.models.User;

import java.util.List;

public interface JpaMeetingRepository extends CrudRepository<Meeting, Long> {
    List<Meeting> findByOwner(User user);

    @Query("SELECT m FROM Meeting m WHERE :user MEMBER OF m.participants")
    List<Meeting> findByParticipantOfparticipants(@Param("user") User user);
}
