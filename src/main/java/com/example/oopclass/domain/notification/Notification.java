package com.example.oopclass.domain.notification;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID notificationUuid;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "is_read", nullable = false)



    private boolean isRead;
}
