package com.example.oopclass.domain.meeting;

import com.example.oopclass.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MeetingUserResponse {
    private List<UserWithRole> users;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserWithRole {
        private String userId;
        private String username;
        private String mainMajor;
        private String subMajor1;
        private String subMajor2;
        private String studentNumber;
        private String role;
        private String meetingRole;

        public UserWithRole(User user, String meetingRole) {
            this.userId = user.getUserId();
            this.username = user.getUsername();
            this.mainMajor = user.getMainMajor().getMajorName();
            this.subMajor1 = user.getSubMajor1() != null ? user.getSubMajor1().getMajorName() : null;
            this.subMajor2 = user.getSubMajor2() != null ? user.getSubMajor2().getMajorName() : null;
            this.studentNumber = user.getStudentNumber();
            this.meetingRole = meetingRole;
        }
    }
}



