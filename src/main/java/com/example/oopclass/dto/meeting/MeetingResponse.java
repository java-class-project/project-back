package com.example.oopclass.dto.meeting;

import com.example.oopclass.domain.meeting.Meeting;
import lombok.Getter;

import java.util.Date;

@Getter
public class MeetingResponse {
    private String subjectName;
    private int desiredCount;
    private String teamType;
    private String username;
    private String userId;
    private String studentNumber;
    private String userMajor;
    private String title;
    private String description;

    private Date date;

    public MeetingResponse(Meeting meeting) {
        this.subjectName = meeting.getSubject().getSubjectName();
        this.desiredCount = meeting.getDesiredCount();
        this.teamType = meeting.getTeamType();
        this.username = meeting.getUser().getUsername();
        this.userId = meeting.getUser().getUserId();
        this.studentNumber = meeting.getUser().getStudentNumber();
        this.userMajor = meeting.getUser().getMainMajor().getMajorName();
        this.title = meeting.getTitle();
        this.description = meeting.getDescription();
        this.date = meeting.getUpdatedAt();
    }
}
