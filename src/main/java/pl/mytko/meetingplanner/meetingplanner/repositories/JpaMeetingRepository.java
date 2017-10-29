package pl.mytko.meetingplanner.meetingplanner.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.mytko.meetingplanner.meetingplanner.models.Meeting;

public interface JpaMeetingRepository extends CrudRepository<Meeting, Long> {
}
