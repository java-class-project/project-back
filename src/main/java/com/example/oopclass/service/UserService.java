package com.example.oopclass.service;

import com.example.oopclass.domain.major.Major;
import com.example.oopclass.domain.major.MajorRepository;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.domain.user.UserRepository;
import com.example.oopclass.domain.user.UserRole;
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

    public boolean isUserIdDuplicated(String userId) {
        return !userRepository.findByUserId(userId).isPresent();
    }

    public boolean isStudentNumberDuplicated(String studentNumber) {
        return !userRepository.findByStudentNumber(studentNumber).isPresent();
    }

    public UserResponse registerUser(JoinRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        if (!isUserIdDuplicated(request.getUserId())) {
            throw new IllegalArgumentException("User ID already exists.");
        }

        if (!isStudentNumberDuplicated(request.getStudentNumber())) {
            throw new IllegalArgumentException("Student number already exists.");
        }

        Major mainMajor = majorRepository.findById(UUID.fromString(request.getMainMajor()))
                .orElseThrow(() -> new IllegalArgumentException("Main major UUID is invalid."));
        Major subMajor1 = request.getSubMajor1() != null ? majorRepository.findById(UUID.fromString(request.getSubMajor1())).orElse(null) : null;
        Major subMajor2 = request.getSubMajor2() != null ? majorRepository.findById(UUID.fromString(request.getSubMajor2())).orElse(null) : null;

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .password(encodedPassword)
                .studentNumber(request.getStudentNumber())
                .mainMajor(mainMajor)
                .subMajor1(subMajor1)
                .subMajor2(subMajor2)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        return new UserResponse(user);
    }

    public User getUserByUuid(UUID userUuid) {
        return userRepository.findById(userUuid).orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

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
