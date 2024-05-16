package com.example.oopclass.service;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.major.MajorRepository;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRepository;
import com.example.oopclass.dto.user.JoinRequest;
import com.example.oopclass.dto.user.UpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }


    // 회원가입
    public void registerUser(JoinRequest request) {
        // UUID 확인
        UUID mainMajorUuid = UUID.fromString(request.getMainMajor());
        UUID subMajor1Uuid = request.getSubMajor1() != null ? UUID.fromString(request.getSubMajor1()) : null;
        UUID subMajor2Uuid = request.getSubMajor2() != null ? UUID.fromString(request.getSubMajor2()) : null;

        // Major 엔티티 조회
        Major mainMajor = majorRepository.findById(mainMajorUuid)
                .orElseThrow(() -> new IllegalArgumentException("본전공 UUID가 유효하지 않습니다."));
        Major subMajor1 = subMajor1Uuid != null ? majorRepository.findById(subMajor1Uuid).orElse(null) : null;
        Major subMajor2 = subMajor2Uuid != null ? majorRepository.findById(subMajor2Uuid).orElse(null) : null;

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성 및 저장
        User user = request.toEntity(mainMajor, subMajor1, subMajor2, encodedPassword);
        userRepository.save(user);
    }

    // 회원정보 조회
    public User getUserByUuid(UUID userUuid) {
        return userRepository.findById(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    // 회원정보 수정
    public void updateUser(UUID userUuid, UpdateRequest request) {
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setUsername(request.getUsername());
        user.setStudentNumber(request.getStudentNumber());

        if (request.getMainMajor() != null) {
            Major mainMajor = majorRepository.findById(UUID.fromString(request.getMainMajor()))
                    .orElseThrow(() -> new IllegalArgumentException("본전공 UUID가 유효하지 않습니다."));
            user.setMainMajor(mainMajor);
        }

        if (request.getSubMajor1() != null) {
            Major subMajor1 = majorRepository.findById(UUID.fromString(request.getSubMajor1()))
                    .orElseThrow(() -> new IllegalArgumentException("부전공1 UUID가 유효하지 않습니다."));
            user.setSubMajor1(subMajor1);
        } else {
            user.setSubMajor1(null);
        }

        if (request.getSubMajor2() != null) {
            Major subMajor2 = majorRepository.findById(UUID.fromString(request.getSubMajor2()))
                    .orElseThrow(() -> new IllegalArgumentException("부전공2 UUID가 유효하지 않습니다."));
            user.setSubMajor2(subMajor2);
        } else {
            user.setSubMajor2(null);
        }

        userRepository.save(user);
    }
}
