package com.example.oopclass.domain.meeting;

import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "meeting")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "meeting_uuid", updatable = false)
    private UUID meetingUuid;

    @ManyToOne
    @JoinColumn(name = "subject_uuid", nullable = false)
    private Subject subject;

    @Column(name = "subject_num")
    private int subjectNum;

    @Column(name = "team_type")
    private String teamType;

    @Column(name = "status")
    private String status;

    @Column(name = "meeting_info")
    private String meetingInfo;

    @Column(name = "meeting_recruitment")
    private int meetingRecruitment;

    @Column(name = "meeting_recruitment_finished")
    private int meetingRecruitmentFinished;

    @ManyToOne
    @JoinColumn(name = "team_leader", nullable = false)
    private User teamLeader;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;
}
