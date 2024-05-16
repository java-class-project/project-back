package com.example.oopclass.domain.user;

import com.example.oopclass.domain.major.Major;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_uuid", updatable = false)
    private UUID userUuid;

    @Column(name = "username")
    private String username;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "password")
    private String password;

    @Column(name = "student_number")
    private String studentNumber;

    @ManyToOne
    @JoinColumn(name = "main_major_uuid", nullable = false)
    private Major mainMajor;

    @ManyToOne
    @JoinColumn(name = "sub_major1_uuid")
    private Major subMajor1;

    @ManyToOne
    @JoinColumn(name = "sub_major2_uuid")
    private Major subMajor2;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
}
