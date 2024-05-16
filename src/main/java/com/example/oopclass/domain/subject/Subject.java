package com.example.oopclass.domain.subject;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "subject")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "subject_uuid", updatable = false, nullable = false)
    private UUID subjectUuid;

    @Column(name = "subject_name", length = 50000)
    private String subjectName;
}
