package com.example.oopclass.controller;

import com.example.oopclass.domain.meeting.Meeting;
import com.example.oopclass.dto.meeting.CreateMeetingRequest;
import com.example.oopclass.dto.meeting.UpdateMeetingRequest;
import com.example.oopclass.service.MeetingService;
import com.example.oopclass.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

@RestController
@RequestMapping("/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody CreateMeetingRequest request, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        UUID userUuid = jwtTokenProvider.getUserUuidFromJWT(token);

        Meeting meeting = meetingService.createMeeting(request, userUuid);
        return ResponseEntity.ok(meeting);
    }

    @PutMapping("/{meetingId}")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable UUID meetingId, @RequestBody UpdateMeetingRequest request, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        UUID userUuid = jwtTokenProvider.getUserUuidFromJWT(token);

        try {
            Meeting meeting = meetingService.updateMeeting(meetingId, request, userUuid);
            return ResponseEntity.ok(meeting);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable UUID meetingId, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        UUID userUuid = jwtTokenProvider.getUserUuidFromJWT(token);

        try {
            meetingService.deleteMeeting(meetingId, userUuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }
}
