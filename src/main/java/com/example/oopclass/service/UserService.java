package com.example.oopclass.service;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.major.MajorRepository;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRepository;
import com.example.oopclass.dto.user.JoinRequest;
import com.example.oopclass.dto.user.UpdateRequest;
import com.example.oopclass.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UUID findUserUuidByUserId(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getUserUuid();
    }

    // 아이디 중복 체크
    public boolean isUserIdDuplicated(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    // 학번 중복 체크
    public boolean isStudentNumberDuplicated(String studentNumber) {
        return userRepository.findByStudentNumber(studentNumber).isPresent();
    }

    // 회원가입
    public UserResponse registerUser(JoinRequest request) {
        // 비밀번호 확인
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        // 아이디 중복 체크
        if (isUserIdDuplicated(request.getUserId())) {
            throw new IllegalArgumentException("User ID already exists.");
        }

        // 학번 중복 체크
        if (isStudentNumberDuplicated(request.getStudentNumber())) {
            throw new IllegalArgumentException("Student number already exists.");
        }

        // UUID 확인
        UUID mainMajorUuid = UUID.fromString(request.getMainMajor());
        UUID subMajor1Uuid = request.getSubMajor1() != null ? UUID.fromString(request.getSubMajor1()) : null;

        // Major 엔티티 조회
        Major mainMajor = majorRepository.findById(mainMajorUuid)
                .orElseThrow(() -> new IllegalArgumentException("Main major UUID is invalid."));
        Major subMajor1 = subMajor1Uuid != null ? majorRepository.findById(subMajor1Uuid).orElse(null) : null;

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 엔티티 생성 및 저장
        User user = request.toEntity(mainMajor, subMajor1, encodedPassword);
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser);
    }

    // 회원정보 조회
    public User getUserByUuid(UUID userUuid) {
        return userRepository.findById(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    // 회원정보 수정
    public UserResponse updateUser(UUID userUuid, UpdateRequest request) {
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        user.setUsername(request.getUsername());
        user.setStudentNumber(request.getStudentNumber());

        if (request.getMainMajor() != null) {
            Major mainMajor = majorRepository.findById(UUID.fromString(request.getMainMajor()))
                    .orElseThrow(() -> new IllegalArgumentException("Main major UUID is invalid."));
            user.setMainMajor(mainMajor);
        }

        if (request.getSubMajor1() != null) {
            Major subMajor1 = majorRepository.findById(UUID.fromString(request.getSubMajor1()))
                    .orElseThrow(() -> new IllegalArgumentException("Sub major 1 UUID is invalid."));
            user.setSubMajor1(subMajor1);
        } else {
            user.setSubMajor1(null);
        }

        userRepository.save(user);
        return new UserResponse(user);
    }
}
