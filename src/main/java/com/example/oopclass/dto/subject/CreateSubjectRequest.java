package com.example.oopclass.dto.subject;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateSubjectRequest {
    private String subjectName;
    private UUID majorUuid;

}
