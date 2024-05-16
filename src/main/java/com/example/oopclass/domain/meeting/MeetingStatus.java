package com.example.oopclass.domain.meeting;

import com.example.oopclass.domain.user.User;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "meeting_status")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingStatus {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "meeting_status_uuid", updatable = false, nullable = false)
    private UUID meetingStatusUuid;

    @ManyToOne
    @JoinColumn(name = "meeting_uuid")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private User user;
}