package com.example.oopclass.domain.meeting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MeetingStatusRepository extends JpaRepository<MeetingStatus, UUID> {
}







