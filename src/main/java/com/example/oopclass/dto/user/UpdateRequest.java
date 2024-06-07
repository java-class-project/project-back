package com.example.oopclass.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateRequest {
    private UUID userUuid;
    private String username;
    private String userId;
    private String studentNumber;
    private UUID mainMajorUuid;
    private String mainMajorName;
    private UUID subMajor1Uuid;
    private String subMajor1Name;
    private UUID subMajor2Uuid;
    private String subMajor2Name;



}
