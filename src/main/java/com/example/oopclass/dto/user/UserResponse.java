package com.example.oopclass.dto.user;

import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.major.Major;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserResponse {
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

    public UserResponse(User user) {
        this.userUuid = user.getUserUuid();
        this.username = user.getUsername();
        this.userId = user.getUserId();
        this.studentNumber = user.getStudentNumber();

        Major mainMajor = user.getMainMajor();
        this.mainMajorUuid = mainMajor != null ? mainMajor.getMajorUuid() : null;
        this.mainMajorName = mainMajor != null ? mainMajor.getMajorName() : null;

        Major subMajor1 = user.getSubMajor1();
        this.subMajor1Uuid = subMajor1 != null ? subMajor1.getMajorUuid() : null;
        this.subMajor1Name = subMajor1 != null ? subMajor1.getMajorName() : null;

        Major subMajor2 = user.getSubMajor2();
        this.subMajor2Uuid = subMajor2 != null ? subMajor2.getMajorUuid() : null;
        this.subMajor2Name = subMajor2 != null ? subMajor2.getMajorName() : null;
    }
}
