package com.example.oopclass.controller;

import com.example.oopclass.domain.meeting.*;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.dto.meeting.CreateMeetingRequest;
import com.example.oopclass.dto.meeting.MeetingResponse;
import com.example.oopclass.dto.meeting.MeetingStatusResponse;
import com.example.oopclass.dto.meeting.UpdateMeetingRequest;
import com.example.oopclass.dto.user.UserResponse;
import com.example.oopclass.security.JwtTokenProvider;
import com.example.oopclass.service.MeetingService;
import com.example.oopclass.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Meeting> createMeeting(@RequestBody CreateMeetingRequest request, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        Meeting meeting = meetingService.createMeeting(request, userId);
        return ResponseEntity.ok(meeting);
    }

    @PutMapping("/{meetingId}")
    public ResponseEntity<Meeting> updateMeeting(@PathVariable UUID meetingId, @RequestBody UpdateMeetingRequest request, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        try {
            Meeting meeting = meetingService.updateMeeting(meetingId, request, userId);
            return ResponseEntity.ok(meeting);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable UUID meetingId, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        try {
            UUID userUuid = UUID.fromString(userId);
            meetingService.deleteMeeting(meetingId, userUuid);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }

    @GetMapping
    public ResponseEntity<List<MeetingResponse>> getAllMeetings() {
        List<Meeting> meetings = meetingService.getAllMeetings();
        List<MeetingResponse> meetingResponses = meetings.stream()
                .map(MeetingResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(meetingResponses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MeetingResponse>> filterAndSearchMeetings(
            @RequestParam(required = false) UUID majorUuid,
            @RequestParam(required = false) UUID subjectUuid,
            @RequestParam(required = false) List<String> teamTypes,
            @RequestParam(required = false) Integer classNum,
            @RequestParam(required = false) Integer desiredCount,
            @RequestParam(required = false) String searchText,
            @RequestParam(required = false) String status) {

        List<MeetingResponse> meetingResponses = meetingService.filterAndSearchMeetings(majorUuid, subjectUuid, teamTypes, desiredCount, classNum != null ? classNum : 0, searchText, status);
        return ResponseEntity.ok(meetingResponses);
    }


    @PostMapping("/{meetingId}/apply")
    public ResponseEntity<String> applyForMeeting(@PathVariable UUID meetingId, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        User applicant = jwtTokenProvider.getUserFromJWT(token);
        meetingService.applyForMeeting(meetingId, applicant);
        return ResponseEntity.ok("Application sent successfully.");
    }

    @PostMapping("/{meetingId}/respond")
    public ResponseEntity<UUID> respondToApplication(@PathVariable UUID meetingId, @RequestParam String applicantId, @RequestParam boolean accepted, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        System.out.println("#######" + "userId: " + userId + "applicantId: " + applicantId);
        UUID notificationUuid = meetingService.respondToApplication(meetingId, applicantId, accepted);
        return ResponseEntity.ok(notificationUuid);
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<List<MeetingResponse>> getMeetingInfo(@PathVariable UUID userUuid, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        User user = userService.getUserByUuid(userUuid);

        if (!user.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<MeetingResponse> meetingResponses = meetingService.getMeetingsByUserUuid(userUuid);
        return ResponseEntity.ok(meetingResponses);
    }


    @GetMapping("/{meetingId}/users")
    public ResponseEntity<MeetingUserResponse> getUsersByMeeting(@PathVariable UUID meetingId, HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        List<MeetingUserResponse.UserWithRole> users = meetingService.getUsersByMeetingWithRoles(meetingId);
        MeetingUserResponse response = new MeetingUserResponse();
        response.setUsers(users);

        return ResponseEntity.ok(response);
    }

}
