package com.example.oopclass.controller;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.dto.major.MajorReqDto;
import com.example.oopclass.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/major")
public class MajorController {
    @Autowired
    private MajorService majorService;

    @PostMapping
    public ResponseEntity<Major> createMajor(@RequestBody MajorReqDto majorReqDto) {
        Major major = majorService.saveMajor(majorReqDto);
        return ResponseEntity.ok(major);
    }

    @GetMapping
    public ResponseEntity<List<Major>> getAllMajors() {
        List<Major> majors = majorService.getAllMajorsSorted();
        return ResponseEntity.ok(majors);
    }
}
