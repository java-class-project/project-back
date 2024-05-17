package com.example.oopclass.dto.user;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {
    private String userId;
    private String username;
    private String password;
    private String studentNumber;
    private String mainMajor;
    private String subMajor1;
    private String subMajor2;

    public User toEntity(Major mainMajor, Major subMajor1, Major subMajor2, String encodedPassword) {
        return User.builder()
                .userId(userId)
                .username(username)
                .password(encodedPassword)
                .studentNumber(studentNumber)
                .mainMajor(mainMajor)
                .subMajor1(subMajor1)
                .subMajor2(subMajor2)
                .role(UserRole.USER)
                .build();
    }
}
