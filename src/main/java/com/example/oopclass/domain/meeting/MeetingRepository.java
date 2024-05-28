package com.example.oopclass.domain.meeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    Optional<Meeting> findByMeetingUuidAndDeletedAtIsNull(UUID meetingUuid);
}
