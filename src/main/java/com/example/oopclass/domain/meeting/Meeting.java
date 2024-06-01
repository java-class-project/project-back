package com.example.oopclass.domain.meeting;

import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "meeting_uuid", updatable = false)
    private UUID meetingUuid;

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "major_uuid", nullable = false)
    private Major major;

    @ManyToOne
    @JoinColumn(name = "subject_uuid", nullable = false)
    private Subject subject;

    @Column(name = "team_type")
    private String teamType;

    @Column(name = "desired_count")
    private int desiredCount;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;
}
