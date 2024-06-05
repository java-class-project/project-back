package com.example.oopclass.domain.meeting;

import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.security.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "meetings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "meeting_uuid")
    private UUID meetingUuid;

    @Column(name = "team_type", length = 5000)
    private String teamType;

    @Column(name="class_num")
    private Integer classNum;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "desired_count")
    private Integer desiredCount;

    @Column(name = "title", length = 255)
    private String title;

    @ManyToOne
    @JoinColumn(name = "subject_uuid")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "major_uuid")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private User user;
}
