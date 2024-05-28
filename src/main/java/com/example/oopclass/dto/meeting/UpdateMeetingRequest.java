package com.example.oopclass.dto.meeting;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateMeetingRequest {
    private String teamType; // "TeamProject", "Study", "Project"
    private UUID majorUuid; // 전공 UUID
    private UUID subjectUuid; // 강의 UUID
    private int desiredCount; // 희망 인원 수
    private String title; // 제목
    private String description; // 설명
}