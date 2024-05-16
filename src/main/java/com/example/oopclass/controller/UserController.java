package com.example.oopclass.controller;

import com.example.oopclass.dto.user.JoinRequest;
import com.example.oopclass.dto.user.LoginRequest;
import com.example.oopclass.dto.user.UpdateRequest;
import com.example.oopclass.dto.user.UserResponse;
import com.example.oopclass.domain.user.User;
import com.example.oopclass.security.JwtTokenProvider;
import com.example.oopclass.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid JoinRequest request) {
        userService.registerUser(request);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{userUuid}")
    public ResponseEntity<UserResponse> getUserInfo(@PathVariable UUID userUuid) {
        User user = userService.getUserByUuid(userUuid);
        UserResponse response = new UserResponse(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{userUuid}")
    public ResponseEntity<String> updateUserInfo(@PathVariable UUID userUuid, @RequestBody @Valid UpdateRequest request) {
        userService.updateUser(userUuid, request);
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody @Valid LoginRequest request) {
        // 현재 인증된 사용자 정보를 가져옴
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        // 이미 인증된 사용자가 있을 경우 "이미 로그인된 상태" 메시지를 반환
        if (currentAuth != null && currentAuth.isAuthenticated() && !(currentAuth instanceof UsernamePasswordAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already logged in.");
        }

        // 인증 진행
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        return ResponseEntity.ok("User logged out successfully");
    }
}
