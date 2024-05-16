package com.example.oopclass.controller;

import com.example.oopclass.domain.user.User;
import com.example.oopclass.dto.auth.LoginRequest;
import com.example.oopclass.dto.user.LoginResponse;
import com.example.oopclass.security.JwtTokenProvider;
import com.example.oopclass.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "APIs for user authentication")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @Operation(summary = "User login")
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        logger.info("Current Authentication: {}", currentAuth);

        // 이미 인증된 사용자가 있을 경우 "이미 로그인된 상태" 메시지를 반환
        if (currentAuth != null && currentAuth.isAuthenticated() && !currentAuth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already logged in.");
        }

        // 인증 진행
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserId(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        logger.info("-----------------created jwt token: " + jwt);

        User user = userService.findByUserId(loginRequest.getUserId());
        logger.info("For check userId: {}, userUuid: {}, jwt: {}", user.getUserId(), user.getUserUuid(), jwt);

        return ResponseEntity.ok(new LoginResponse(user.getUserId(), user.getUserUuid(), jwt));
    }

    @Operation(summary = "User logout")
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        logger.info("Current Authentication: {}", currentAuth);

        if (currentAuth == null || !currentAuth.isAuthenticated() || currentAuth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already logged out.");
        }

        String token = tokenProvider.resolveToken(request);
        String userId = tokenProvider.getUserIdFromJWT(token);

        User user = userService.findByUserId(userId);

        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        return ResponseEntity.ok(new LoginResponse(user.getUserId(), user.getUserUuid(), token));
    }
}
