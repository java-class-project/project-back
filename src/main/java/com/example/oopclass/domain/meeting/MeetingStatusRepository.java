package com.example.oopclass.domain.meeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeetingStatusRepository extends JpaRepository<MeetingStatus, UUID> {
    List<MeetingStatus> findByUser_UserUuid(UUID userUuid);
    List<MeetingStatus> findByMeeting_MeetingUuid(UUID meetingUuid);

}



