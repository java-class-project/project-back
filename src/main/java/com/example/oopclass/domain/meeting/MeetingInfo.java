package com.example.oopclass.domain.meeting;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@Entity
@Table(name = "meeting_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EnableJpaRepositories(basePackages = "com.example.oopclass.domain")
public class MeetingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID meetingInfoUuid;

    @Column(nullable = false)
    private Integer meetingRecruitment;

    @Column(nullable = false)
    private Integer meetingRecruitmentFinished;

    @OneToOne
    @JoinColumn(name = "meeting_uuid", nullable = false)
    private Meeting meeting;
}
