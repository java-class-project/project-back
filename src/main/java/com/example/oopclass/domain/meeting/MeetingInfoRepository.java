package com.example.oopclass.domain.meeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MeetingInfoRepository extends JpaRepository<MeetingInfo, UUID> {

    Optional<MeetingInfo> findByMeeting(Meeting meeting);
}







