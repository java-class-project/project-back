package com.example.oopclass.controller;

import com.example.oopclass.domain.user.User;
import com.example.oopclass.dto.user.UpdateRequest;
import com.example.oopclass.dto.user.UserResponse;
import com.example.oopclass.dto.user.JoinRequest;
import com.example.oopclass.service.UserService;
import com.example.oopclass.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;




    @GetMapping("/{userUuid}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable UUID userUuid, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        User user = userService.getUserByUuid(userUuid);
        if (user == null || !user.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        UserResponse response = new UserResponse(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{userUuid}")
    public ResponseEntity<UserResponse> updateUserInfo(@PathVariable UUID userUuid, @RequestBody @Valid UpdateRequest request, HttpServletRequest httpRequest) {
        String token = jwtTokenProvider.resolveToken(httpRequest);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        User user = userService.getUserByUuid(userUuid);
        if (user == null || !user.getUserId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        userService.updateUser(userUuid, request);
        User updatedUser = userService.getUserByUuid(userUuid);
        UserResponse response = new UserResponse(updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid JoinRequest request) {
        userService.registerUser(request);
        User newUser = userService.findByUserId(request.getUserId());
        UserResponse response = new UserResponse(newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/register/check-userid")
    public ResponseEntity<Boolean> checkUserIdDuplicate(@RequestParam String userId) {
        boolean isAvailable = userService.isUserIdDuplicated(userId);
        return ResponseEntity.ok(isAvailable);
    }

    @GetMapping("/register/check-studentnumber")
    public ResponseEntity<Boolean> checkStudentNumberDuplicate(@RequestParam String studentNumber) {
        boolean isAvailable = userService.isStudentNumberDuplicated(studentNumber);
        return ResponseEntity.ok(isAvailable);
    }
}
