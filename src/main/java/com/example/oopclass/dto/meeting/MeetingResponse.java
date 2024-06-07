package com.example.oopclass.dto.meeting;

import com.example.oopclass.domain.meeting.Meeting;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class MeetingResponse {
    private UUID meetingUuid;

    private String subjectName;
    private int desiredCount;
    private String teamType;
    private String username;
    private String userId;
    private Integer classNum;
    private String studentNumber;
    private String userMajor;
    private String title;
    private String description;
    private Date date;
    private Integer meetingRecruitment;
    private Integer meetingRecruitmentFinished;
    private String status;


    public MeetingResponse(Meeting meeting) {
        this.meetingUuid = meeting.getMeetingUuid();
        this.subjectName = meeting.getSubject().getSubjectName();
        this.desiredCount = meeting.getDesiredCount();
        this.teamType = meeting.getTeamType();
        this.username = meeting.getUser().getUsername();
        this.userId = meeting.getUser().getUserId();
        this.classNum = meeting.getClassNum();
        this.studentNumber = meeting.getUser().getStudentNumber();
        this.userMajor = meeting.getUser().getMainMajor().getMajorName();
        this.title = meeting.getTitle();
        this.description = meeting.getDescription();
        this.date = meeting.getUpdatedAt();


        if (meeting.getMeetingInfo() != null) {
            this.meetingRecruitment = meeting.getMeetingInfo().getMeetingRecruitment();
            this.meetingRecruitmentFinished = meeting.getMeetingInfo().getMeetingRecruitmentFinished();
            this.status = meeting.getMeetingInfo().getMeetingRecruitmentFinished() > 1 ? "team" : "person";
        }
    }
}
