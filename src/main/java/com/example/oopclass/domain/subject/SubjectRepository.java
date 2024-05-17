package com.example.oopclass.domain.subject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
    List<Subject> findByMajorMajorUuid(UUID majorUuid);
}
