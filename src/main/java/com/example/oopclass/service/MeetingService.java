package com.example.oopclass.service;

import com.example.oopclass.domain.meeting.Meeting;
import com.example.oopclass.domain.meeting.MeetingRepository;
import com.example.oopclass.domain.meeting.MeetingStatus;
import com.example.oopclass.domain.meeting.MeetingStatusRepository;
import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.domain.subject.SubjectRepository;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRepository;
import com.example.oopclass.dto.meeting.CreateMeetingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingStatusRepository meetingStatusRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Transactional
    public Meeting createMeeting(CreateMeetingRequest request, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Subject subject = subjectRepository.findById(request.getSubjectUuid()).orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        Meeting meeting = Meeting.builder()
                .subject(subject)
                .subjectNum(request.getSubjectNum())
                .teamType(request.getTeamType())
                .status("OPEN")
                .meetingInfo(request.getMeetingInfo())
                .meetingRecruitment(request.getMeetingRecruitment())
                .meetingRecruitmentFinished(request.getMeetingRecruitmentFinished())
                .createdAt(new Date())
                .teamLeader(user)
                .build();

        return meetingRepository.save(meeting);
    }

    @Transactional
    public void joinMeeting(UUID meetingUuid, UUID userUuid) {
        Meeting meeting = meetingRepository.findById(meetingUuid).orElseThrow(() -> new IllegalArgumentException("Meeting not found"));
        User user = userRepository.findById(userUuid).orElseThrow(() -> new IllegalArgumentException("User not found"));

        MeetingStatus meetingStatus = MeetingStatus.builder()
                .meeting(meeting)
                .user(user)
                .build();

        meetingStatusRepository.save(meetingStatus);
    }

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }
}