package com.example.oopclass.domain.meeting;

import com.example.oopclass.domain.meeting.Meeting;
import com.example.oopclass.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@Entity
@Table(name = "meeting_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaRepositories(basePackages = "com.example.oopclass.domain")
public class MeetingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID meetingStatusUuid;

    @ManyToOne
    @JoinColumn(name = "meeting_uuid", nullable = false)
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

}
