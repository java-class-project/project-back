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
    private String confirmPassword; // 비밀번호 재확인
    private String studentNumber;
    private String mainMajor;
    private String subMajor1;

    public User toEntity(Major mainMajor, Major subMajor1, String encodedPassword) {
        return User.builder()
                .userId(userId)
                .username(username)
                .password(encodedPassword)
                .studentNumber(studentNumber)
                .mainMajor(mainMajor)
                .subMajor1(subMajor1)
                .role(UserRole.USER)
                .build();
    }
}
