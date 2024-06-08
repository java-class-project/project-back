package com.example.oopclass.service;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.major.MajorRepository;
import com.example.oopclass.domain.meeting.*;
import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.domain.subject.SubjectRepository;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRepository;
import com.example.oopclass.dto.meeting.CreateMeetingRequest;
import com.example.oopclass.dto.meeting.MeetingResponse;
import com.example.oopclass.dto.meeting.UpdateMeetingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    public List<MeetingResponse> getAllMeetings() {
        return meetingRepository.findAll().stream()
                .map(MeetingResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeetingResponse> filterAndSearchMeetings(UUID majorUuid, UUID subjectUuid, List<String> teamTypes, Integer desiredCount, Integer classNum, String searchText, List<String> status) {
        List<Meeting> meetings;
        if (majorUuid == null && subjectUuid == null && teamTypes == null && desiredCount == null && classNum == null && searchText == null && status == null) {
            meetings = meetingRepository.findAll();
        } else {
            meetings = meetingRepository.filterAndSearchMeetings(
                    majorUuid, subjectUuid, teamTypes, desiredCount, classNum, searchText, status);
        }

        return meetings.stream()
                .map(MeetingResponse::new)
                .collect(Collectors.toList());
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
    public UUID respondToApplication(UUID meetingId, String applicantId, boolean accepted) {
        System.out.println("Meeting ID: " + meetingId);
        System.out.println("Applicant ID: " + applicantId);

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new IllegalArgumentException("Meeting not found"));
        User applicant = userRepository.findByUserId(applicantId).orElseThrow(() -> new IllegalArgumentException("Applicant not found"));

        System.out.println("Retrieved Meeting: " + meeting.getMeetingUuid());

        MeetingInfo meetingInfo = meetingInfoRepository.findByMeeting(meeting)
                .orElseThrow(() -> new IllegalArgumentException("Meeting info not found"));

        UUID notificationUuid;
        if (accepted) {
            if (meetingInfo.getMeetingRecruitment().equals(meetingInfo.getMeetingRecruitmentFinished())) {
                throw new IllegalStateException("Meeting recruitment is finished");
            }
            meetingInfo.setMeetingRecruitmentFinished(meetingInfo.getMeetingRecruitmentFinished() + 1);
            meetingInfoRepository.save(meetingInfo);

            MeetingStatus meetingStatus = new MeetingStatus();
            meetingStatus.setMeeting(meeting);
            meetingStatus.setUser(applicant);

            System.out.println("Meeting Status to be inserted: " + meetingStatus);

            meetingRepository.findById(meeting.getMeetingUuid()).orElseThrow(() -> new IllegalArgumentException("Meeting not found in repository"));
            userRepository.findByUserId(applicant.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found in repository"));

            meetingStatusRepository.save(meetingStatus);

            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(applicantId.toString(), "Application accepted", 1, TimeUnit.DAYS);

            notificationUuid = notificationService.notifyApplicant(applicant.getUserId(), true);
        } else {
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(applicantId.toString(), "Application rejected", 1, TimeUnit.DAYS);

            notificationUuid = notificationService.notifyApplicant(applicant.getUserId(), false);
        }

        notificationService.notifyMeetingCreator(meeting.getUser().getUserId(), applicant.getUsername());

        return notificationUuid;
    }

    @Transactional(readOnly = true)
    public List<MeetingResponse> getMeetingsByUserUuid(UUID userUuid) {
        List<Meeting> createdMeetings = meetingRepository.findByUser_UserUuid(userUuid);

        List<MeetingStatus> meetingStatuses = meetingStatusRepository.findByUser_UserUuid(userUuid);
        List<Meeting> participatedMeetings = meetingStatuses.stream()
                .map(MeetingStatus::getMeeting)
                .collect(Collectors.toList());

        createdMeetings.addAll(participatedMeetings);

        List<Meeting> distinctMeetings = createdMeetings.stream()
                .distinct()
                .collect(Collectors.toList());

        return distinctMeetings.stream()
                .map(MeetingResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MeetingUserResponse.UserWithRole> getUsersByMeetingWithRoles(UUID meetingUuid) {
        Meeting meeting = meetingRepository.findById(meetingUuid)
                .orElseThrow(() -> new IllegalArgumentException("Meeting not found"));

        List<MeetingUserResponse.UserWithRole> usersWithRoles = new ArrayList<>();


        User leader = meeting.getUser();
        usersWithRoles.add(new MeetingUserResponse.UserWithRole(leader, "leader"));

        List<MeetingStatus> meetingStatuses = meetingStatusRepository.findByMeeting_MeetingUuid(meetingUuid);
        for (MeetingStatus meetingStatus : meetingStatuses) {
            usersWithRoles.add(new MeetingUserResponse.UserWithRole(meetingStatus.getUser(), "member"));
        }

        return usersWithRoles;
    }
}
