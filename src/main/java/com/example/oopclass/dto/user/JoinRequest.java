package com.example.oopclass.dto.user;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRole;
import lombok.Getter;
import lombok.Setter;

// import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class JoinRequest {
    // @NotBlank(message = "이름을 입력하세요")
    private String username;

    // @NotBlank(message = "로그인 아이디를 입력하세요")
    private String userId;

    // @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    private String passwordCheck;

    // @NotBlank(message = "학번을 입력하세요")
    private String studentNumber;

    // @NotBlank(message = "본전공을 입력하세요")
    private String mainMajor;

    private String subMajor1;
    private String subMajor2;

    public User toEntity(Major mainMajorEntity, Major subMajor1Entity, Major subMajor2Entity, String encodedPassword) {
        return User.builder()
                .username(this.username)
                .userId(this.userId)
                .password(encodedPassword)
                .studentNumber(this.studentNumber)
                .mainMajor(mainMajorEntity)
                .subMajor1(subMajor1Entity)
                .subMajor2(subMajor2Entity)
                .role(UserRole.USER)
                .build();
    }
}
