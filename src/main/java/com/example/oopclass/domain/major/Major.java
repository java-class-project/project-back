package com.example.oopclass.domain.major;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "major")


@Getter
@Setter
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "major_uuid", columnDefinition = "UUID")
    private UUID majorUuid;

    @Column(name = "major_name")
    private String majorName;
}
