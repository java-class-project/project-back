package com.example.oopclass.service;

import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.domain.subject.SubjectRepository;
import com.example.oopclass.dto.subject.CreateSubjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Transactional
    public Subject createSubject(CreateSubjectRequest request) {
        Subject subject = Subject.builder()
                .subjectName(request.getSubjectName())
                .build();

        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<Subject> getSubjectsByMajor(UUID majorUuid) {
        return subjectRepository.findByMajorMajorUuid(majorUuid);
    }
}




