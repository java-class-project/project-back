package com.example.oopclass.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponse {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userUuid")
    private UUID userUuid;

    @JsonProperty("token")
    private String token;
}
