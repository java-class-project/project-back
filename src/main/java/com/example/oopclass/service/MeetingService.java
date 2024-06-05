package com.example.oopclass.service;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.major.MajorRepository;
import com.example.oopclass.domain.meeting.*;
import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.domain.subject.SubjectRepository;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRepository;
import com.example.oopclass.dto.meeting.CreateMeetingRequest;
import com.example.oopclass.dto.meeting.UpdateMeetingRequest;
import com.example.oopclass.websocket.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final MeetingInfoRepository meetingInfoRepository;
    private final MeetingStatusRepository meetingStatusRepository;
    private final UserRepository userRepository;

    private final MajorRepository majorRepository;

    private final SubjectRepository subjectRepository;
    private final NotificationService notificationService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;



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
                .classNum(request.getClassNum())
                .desiredCount(request.getDesiredCount())
                .title(request.getTitle())
                .description(request.getDescription())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Meeting savedMeeting = meetingRepository.save(meeting);

        MeetingInfo meetingInfo = MeetingInfo.builder()
                .meeting(savedMeeting)
                .meetingRecruitment(request.getDesiredCount())
                .meetingRecruitmentFinished(1)
                .build();

        meetingInfoRepository.save(meetingInfo);

        return savedMeeting;
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
    public void deleteMeeting(UUID meetingUuid, UUID userId) throws IllegalAccessException {
        Meeting meeting = meetingRepository.findByMeetingUuidAndDeletedAtIsNull(meetingUuid)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        if (!meeting.getUser().getUserId().equals(userId)) {
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
    public List<Meeting> filterAndSearchMeetings(UUID majorUuid, UUID subjectUuid, List<String> teamTypes, Integer desiredCount, String searchText) {
        return meetingRepository.filterAndSearchMeetings(majorUuid, subjectUuid, teamTypes, desiredCount, searchText);
    }

    @Transactional
    public void applyForMeeting(UUID meetingId, User applicant) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new IllegalArgumentException("Meeting not found"));
        MeetingInfo meetingInfo = meetingInfoRepository.findByMeeting(meeting)
                .orElseThrow(() -> new IllegalArgumentException("Meeting info not found"));
        if (meetingInfo.getMeetingRecruitment().equals(meetingInfo.getMeetingRecruitmentFinished())) {
            throw new IllegalStateException("Meeting recruitment is finished");
        }

        String applicantInfo = "이름: " + applicant.getUsername() + ", 학과: " + applicant.getMainMajor().getMajorName() + ", 학번: " + applicant.getStudentNumber() + ", 수업: " + meeting.getSubject().getSubjectName() + ", 신청 날짜: " + new Date();
        notificationService.notifyMeetingCreator(meeting.getUser().getUserId(), applicantInfo);
    }

    @Transactional
    public void respondToApplication(UUID meetingId, String applicantId, boolean accepted) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new IllegalArgumentException("Meeting not found"));
        User applicant = userRepository.findByUserId(applicantId).orElseThrow(() -> new IllegalArgumentException("Applicant not found"));
        MeetingInfo meetingInfo = meetingInfoRepository.findByMeeting(meeting)
                .orElseThrow(() -> new IllegalArgumentException("Meeting info not found"));

        if (accepted) {
            if (meetingInfo.getMeetingRecruitment().equals(meetingInfo.getMeetingRecruitmentFinished())) {
                throw new IllegalStateException("Meeting recruitment is finished");
            }
            meetingInfo.setMeetingRecruitmentFinished(meetingInfo.getMeetingRecruitmentFinished() + 1);
            meetingInfoRepository.save(meetingInfo);

            MeetingStatus meetingStatus = new MeetingStatus();
            meetingStatus.setMeeting(meeting);
            meetingStatus.setUser(applicant);
            meetingStatusRepository.save(meetingStatus);

            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(applicantId.toString(), "Application accepted", 1, TimeUnit.DAYS);
        } else {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(applicantId.toString(), "Application rejected", 1, TimeUnit.DAYS);
        }

        notificationService.notifyApplicant(applicant.getUserId(), accepted);
    }

    public MeetingStatus getMeetingStatusByUuid(UUID userUuid) {
        return meetingStatusRepository.findByUser_UserUuid(userUuid)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}
