package com.example.oopclass.domain.meeting;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "meeting_info")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingInfo {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "meeting_info_uuid", updatable = false, nullable = false)
    private UUID meetingInfoUuid;

    @Column(name = "meeting_recruitment")
    private Integer meetingRecruitment;

    @Column(name = "meeting_recruitment_finished")
    private Integer meetingRecruitmentFinished;
}