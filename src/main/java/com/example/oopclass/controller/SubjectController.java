package com.example.oopclass.controller;

import com.example.oopclass.domain.subject.Subject;
import com.example.oopclass.dto.subject.CreateSubjectRequest;
import com.example.oopclass.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody CreateSubjectRequest request) {
        Subject subject = subjectService.createSubject(request);
        return ResponseEntity.ok(subject);
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }
}
