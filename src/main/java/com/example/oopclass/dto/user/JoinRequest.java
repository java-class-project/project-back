package com.example.oopclass.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {
    private String userId;
    private String password;
    private String confirmPassword;
    private String studentNumber;
    private String mainMajor;
    private String subMajor1;
    private String subMajor2;
    private String username;
}
