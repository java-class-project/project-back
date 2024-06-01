package com.example.oopclass.service;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.major.MajorRepository;
import com.example.oopclass.domain.meeting.Meeting;
import com.example.oopclass.domain.meeting.MeetingRepository;
import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.domain.subject.SubjectRepository;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRepository;
import com.example.oopclass.dto.meeting.CreateMeetingRequest;
import com.example.oopclass.dto.meeting.UpdateMeetingRequest;
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
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public Meeting createMeeting(CreateMeetingRequest request, String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Major major = majorRepository.findById(request.getMajorUuid()).orElseThrow(() -> new IllegalArgumentException("Major not found"));
        Subject subject = subjectRepository.findById(request.getSubjectUuid()).orElseThrow(() -> new IllegalArgumentException("Subject not found"));

        Meeting meeting = Meeting.builder()
                .user(user)
                .teamType(request.getTeamType())
                .major(major)
                .subject(subject)
                .desiredCount(request.getDesiredCount())
                .title(request.getTitle())
                .description(request.getDescription())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return meetingRepository.save(meeting);
    }

    @Transactional
    public Meeting updateMeeting(UUID meetingUuid, UpdateMeetingRequest request, String userId) throws IllegalAccessException {
        Meeting meeting = meetingRepository.findByMeetingUuidAndDeletedAtIsNull(meetingUuid)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        if (!meeting.getUser().getUserId().equals(userId)) {
            throw new IllegalAccessException("You do not have permission to update this meeting");
        }

        meeting.setTeamType(request.getTeamType());
        meeting.setMajor(majorRepository.findById(request.getMajorUuid())
                .orElseThrow(() -> new IllegalArgumentException("Major not found")));
        meeting.setSubject(subjectRepository.findById(request.getSubjectUuid())
                .orElseThrow(() -> new IllegalArgumentException("Subject not found")));
        meeting.setDesiredCount(request.getDesiredCount());
        meeting.setTitle(request.getTitle());
        meeting.setDescription(request.getDescription());
        meeting.setUpdatedAt(new Date());

        return meetingRepository.save(meeting);
    }

    @Transactional
    public void deleteMeeting(UUID meetingUuid, UUID userUuid) throws IllegalAccessException {
        Meeting meeting = meetingRepository.findByMeetingUuidAndDeletedAtIsNull(meetingUuid)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        if (!meeting.getUser().getUserUuid().equals(userUuid)) {
            throw new IllegalAccessException("You do not have permission to delete this meeting");
        }

        meeting.setDeletedAt(new Date());
        meetingRepository.save(meeting);
    }

    @Transactional(readOnly = true)
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Meeting> filterAndSearchMeetings(UUID majorUuid, UUID subjectUuid, String teamType, Integer desiredCount, String searchText) {
        return meetingRepository.filterAndSearchMeetings(majorUuid, subjectUuid, teamType, desiredCount, searchText);
    }
}
