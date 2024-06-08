package com.example.oopclass.domain.subject;

import com.example.oopclass.domain.major.Major;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.UUID;

@Entity
@Table(name = "subject")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaRepositories(basePackages = "com.example.oopclass.domain")
public class Subject {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "subject_uuid", updatable = false, nullable = false)
    private UUID subjectUuid;

    @Column(name = "subject_name", length = 50000)
    private String subjectName;

    @ManyToOne




    @JoinColumn(name = "major_uuid")
    private Major major;
}
