package com.example.oopclass.dto.meeting;

import com.example.oopclass.domain.meeting.Meeting;
import com.example.oopclass.domain.meeting.MeetingStatus;

import java.util.Date;
import java.util.UUID;

public class MeetingStatusResponse {
    private String subjectName;
    private int desiredCount;
    private String teamType;
    private String username;

    private String studentNumber;
    private String userMajor;
    private String title;
    private String description;

    
    
    
    
    private Date date;

    public MeetingStatusResponse(MeetingStatus meetingStatus) {
        this.subjectName = meetingStatus.getMeeting().getSubject().getSubjectName();
        this.desiredCount = meetingStatus.getMeeting().getDesiredCount();
        this.teamType = meetingStatus.getMeeting().getTeamType();
        this.username = meetingStatus.getMeeting().getUser().getUsername();
        this.studentNumber = meetingStatus.getMeeting().getUser().getStudentNumber();
        this.userMajor = meetingStatus.getMeeting().getUser().getMainMajor().getMajorName();
        this.title = meetingStatus.getMeeting().getTitle();
        this.description = meetingStatus.getMeeting().getDescription();
        this.date = meetingStatus.getMeeting().getUpdatedAt();
    }
}
