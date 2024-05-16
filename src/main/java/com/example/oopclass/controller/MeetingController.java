package com.example.oopclass.controller;

import com.example.oopclass.domain.meeting.Meeting;
import com.example.oopclass.dto.meeting.CreateMeetingRequest;
import com.example.oopclass.service.MeetingService;
import com.example.oopclass.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final UserService userService; // UserService 주입 추가

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody CreateMeetingRequest request, Authentication authentication) {
        String userId = authentication.getName(); // 여기서 userId는 일반 문자열입니다.
        UUID userUuid = userService.findUserUuidByUserId(userId); // 사용자 ID로부터 UUID를 가져옵니다.
        Meeting meeting = meetingService.createMeeting(request, userUuid);
        return ResponseEntity.ok(meeting);
    }

    @PostMapping("/{meetingUuid}/join")
    public ResponseEntity<Void> joinMeeting(@PathVariable UUID meetingUuid, Authentication authentication) {
        String userId = authentication.getName();
        UUID userUuid = userService.findUserUuidByUserId(userId);
        meetingService.joinMeeting(meetingUuid, userUuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(meetings);
    }
}
