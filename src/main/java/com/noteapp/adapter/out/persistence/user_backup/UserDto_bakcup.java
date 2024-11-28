package com.noteapp.adapter.out.persistence.user_backup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto_bakcup {
    @Data
    public static class UserCreateRequest {
        private String name;
        private String email;
        private String password;
        private String memo;
    }
    
    @Data
    public static class UserUpdateRequest {
        private String name;
        private String email;
        private String password;
        private String memo;
    }
    
    @Data
    public static class UserResponse {
        private String id;
        private String name;
        private String email;
        private String memo;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
    
    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResponse {
        private String accessToken;
        private String tokenType = "Bearer";
    }
}