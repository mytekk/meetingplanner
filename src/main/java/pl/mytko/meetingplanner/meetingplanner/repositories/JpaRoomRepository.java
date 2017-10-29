package pl.mytko.meetingplanner.meetingplanner.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.mytko.meetingplanner.meetingplanner.models.Room;

public interface JpaRoomRepository extends CrudRepository<Room, Long> {
}
