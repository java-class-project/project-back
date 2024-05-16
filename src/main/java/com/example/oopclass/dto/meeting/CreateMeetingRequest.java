package com.example.oopclass.dto.meeting;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateMeetingRequest {
    private UUID subjectUuid;
    private int subjectNum;
    private String teamType;
    private String meetingInfo;
    private int meetingRecruitment;
    private int meetingRecruitmentFinished;
}
