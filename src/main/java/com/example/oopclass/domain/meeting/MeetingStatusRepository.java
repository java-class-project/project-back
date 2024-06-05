package com.example.oopclass.domain.meeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MeetingStatusRepository extends JpaRepository<MeetingStatus, UUID> {
    Optional<MeetingStatus> findByUser_UserUuid(UUID userUuid);
}
