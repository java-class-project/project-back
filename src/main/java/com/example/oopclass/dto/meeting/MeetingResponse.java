package com.example.oopclass.dto.meeting;

import com.example.oopclass.domain.meeting.Meeting;
import lombok.Getter;

@Getter
public class MeetingResponse {
    private String subjectName;
    private int desiredCount;
    private String teamType;
    private String username;
    private String title;
    private String description;

    public MeetingResponse(Meeting meeting) {
        this.subjectName = meeting.getSubject().getSubjectName();
        this.desiredCount = meeting.getDesiredCount();
        this.teamType = meeting.getTeamType();
        this.username = meeting.getUser().getUsername();
        this.title = meeting.getTitle();
        this.description = meeting.getDescription();
    }
}
