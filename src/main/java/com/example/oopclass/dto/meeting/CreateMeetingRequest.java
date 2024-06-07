package com.example.oopclass.dto.meeting;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateMeetingRequest {
    private String teamType;
    private UUID majorUuid;
    private UUID subjectUuid;
    private int classNum;
    private int desiredCount;
    private String title;
    private String description;
}
