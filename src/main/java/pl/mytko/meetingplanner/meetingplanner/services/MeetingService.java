package pl.mytko.meetingplanner.meetingplanner.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mytko.meetingplanner.meetingplanner.models.Meeting;
import pl.mytko.meetingplanner.meetingplanner.repositories.JpaMeetingRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class MeetingService {

    private JpaMeetingRepository jpaMeetingRepository;

    @Autowired
    public MeetingService(JpaMeetingRepository jpaMeetingRepository) {
        this.jpaMeetingRepository = jpaMeetingRepository;
    }


    public boolean validateMeetingDates(LocalDateTime begining, LocalDateTime end) {
        return begining.isBefore(end);
    }

    public boolean validateMeetingDuration(LocalDateTime begining, LocalDateTime end) {
        long between = ChronoUnit.MINUTES.between(begining, end);
        return (between >= 15 && between <= 120) ? true : false;
    }

    public boolean validateRoom(Meeting meeting) {
        List<Meeting> byRoom = jpaMeetingRepository.findByRoom(meeting.getRoom());

        Iterator<Meeting> iterator = byRoom.iterator();
        while (iterator.hasNext()) {
            Meeting next = iterator.next();
            if (meeting.getBegining().isAfter(next.getBegining()) && meeting.getBegining().isBefore(next.getEnd())
                    ||
                    meeting.getEnd().isAfter(next.getBegining()) && meeting.getEnd().isBefore(next.getEnd())
                    ||
                    meeting.getBegining().isBefore(next.getBegining()) && meeting.getEnd().isAfter(next.getEnd())
                    ) {
                return false;
            }
        }
        return true;
    }

    public List<Meeting> getColidingMeetings(Meeting meeting) {
        List<Meeting> listToReturn = new ArrayList<>();

        List<Meeting> byRoom = jpaMeetingRepository.findByRoom(meeting.getRoom());

        Iterator<Meeting> iterator = byRoom.iterator();
        while (iterator.hasNext()) {
            Meeting next = iterator.next();
            if (meeting.getBegining().isAfter(next.getBegining()) && meeting.getBegining().isBefore(next.getEnd())
                    ||
                    meeting.getEnd().isAfter(next.getBegining()) && meeting.getEnd().isBefore(next.getEnd())
                    ||
                    meeting.getBegining().isBefore(next.getBegining()) && meeting.getEnd().isAfter(next.getEnd())
                    ) {
                listToReturn.add(next);
            }
        }
        return listToReturn;
    }

}
