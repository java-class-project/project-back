package com.example.oopclass.controller;

import com.example.oopclass.domain.meeting.Meeting;
import com.example.oopclass.dto.meeting.CreateMeetingRequest;
import com.example.oopclass.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody CreateMeetingRequest request, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        Meeting meeting = meetingService.createMeeting(request, userId);
        return ResponseEntity.ok(meeting);
    }

    @PostMapping("/{meetingUuid}/join")
    public ResponseEntity<Void> joinMeeting(@PathVariable UUID meetingUuid, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        meetingService.joinMeeting(meetingUuid, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(meetings);
    }
}
