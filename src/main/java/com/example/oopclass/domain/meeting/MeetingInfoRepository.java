package com.example.oopclass.domain.meeting;

import com.example.oopclass.domain.meeting.MeetingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeetingInfoRepository extends JpaRepository<MeetingInfo, UUID> {
}
