package com.example.oopclass.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequest {
    private String username;
    private String studentNumber;
    private String mainMajor;
    private String subMajor1;
    private String subMajor2;
}
