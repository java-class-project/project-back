package com.example.oopclass.service;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.major.MajorRepository;
import com.example.oopclass.dto.major.MajorReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    public Major saveMajor(MajorReqDto majorReqDto) {
        Major major = new Major();
        major.setMajorName(majorReqDto.getMajorName());
        return majorRepository.save(major);
    }
}
