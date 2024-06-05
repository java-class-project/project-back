package com.example.oopclass.domain.meeting;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "meeting_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID meetingInfoUuid;

    @Column(nullable = false)
    private Integer meetingRecruitment;

    @Column(nullable = false)
    private Integer meetingRecruitmentFinished;

    @ManyToOne
    @JoinColumn(name = "meeting_uuid", nullable = false)
    private Meeting meeting;
}
