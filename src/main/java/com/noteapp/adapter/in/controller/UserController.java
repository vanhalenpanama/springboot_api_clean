package com.noteapp.adapter.in.controller;

import com.noteapp.adapter.in.dto.UserDto;
import com.noteapp.application.user.UserCommandUseCase;
import com.noteapp.application.user.UserQueryUseCase;
import com.noteapp.domain.user.User;
import com.noteapp.common.jwt.JwtTokenProvider;
import com.noteapp.common.jwt.JwtTokenProvider.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserCommandUseCase userCommandUseCase;
    private final UserQueryUseCase userQueryUseCase;

    @PostMapping("/login")
    public UserDto.TokenResponse login(@RequestBody UserDto.LoginRequest request) {
        String token = userCommandUseCase.login(request.getEmail(), request.getPassword());
        return UserDto.TokenResponse.builder()
                .accessToken(token)
                .build();
    }

    @GetMapping("/me")
    public UserDto.UserResponse getCurrentUser(@AuthenticationPrincipal CurrentUser currentUser) {
        return toResponse(userQueryUseCase.getUserById(currentUser.getId()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.UserResponse createUser(@RequestBody UserDto.UserCreateRequest request) {
        User user = userCommandUseCase.createUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getMemo()
        );
        return toResponse(user);
    }

    @GetMapping
    public List<UserDto.UserResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return userQueryUseCase.getUsers(page, size).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto.UserResponse getUser(
            @PathVariable String userId,
            @AuthenticationPrincipal CurrentUser currentUser) {
        validateUserAccess(userId, currentUser);
        return toResponse(userQueryUseCase.getUserById(userId));
    }

    @PatchMapping("/{userId}")
    public UserDto.UserResponse updateUser(
            @PathVariable String userId,
            @RequestBody UserDto.UserUpdateRequest request,
            @AuthenticationPrincipal CurrentUser currentUser) {
        validateUserAccess(userId, currentUser);
        User updatedUser = userCommandUseCase.updateUser(
                userId,
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getMemo()
        );
        return toResponse(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable String userId,
            @AuthenticationPrincipal CurrentUser currentUser) {
        validateUserAccess(userId, currentUser);
        userCommandUseCase.deleteUser(userId);
    }

    private UserDto.UserResponse toResponse(User user) {
        UserDto.UserResponse response = new UserDto.UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setMemo(user.getMemo());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    private void validateUserAccess(String userId, CurrentUser currentUser) {
        User currentUserDetails = userQueryUseCase.getUserById(currentUser.getId());
        if (!currentUserDetails.getId().equals(userId) &&
                !JwtTokenProvider.Role.ROLE_ADMIN.equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
        }
    }
}

//package com.noteapp.adapter.in.controller;
//
//import com.noteapp.domain.user.User;
//import com.noteapp.application.user.UserCommandUseCase;
//import com.noteapp.application.user.UserQueryUseCase;
//import com.noteapp.common.jwt.JwtTokenProvider;
//import com.noteapp.common.jwt.JwtTokenProvider.CurrentUser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserController {
//    private final UserCommandUseCase userCommandUseCase;
//    private final UserQueryUseCase userQueryUseCase;
//
//    // DTO 클래스들을 컨트롤러 내부 static 클래스로 이동
//    @Data
//    public static class UserCreateRequest {
//        private String name;
//        private String email;
//        private String password;
//        private String memo;
//    }
//
//    @Data
//    public static class UserUpdateRequest {
//        private String name;
//        private String email;
//        private String password;
//        private String memo;
//    }
//
//    @Data
//    public static class UserResponse {
//        private String id;
//        private String name;
//        private String email;
//        private String memo;
//        private LocalDateTime createdAt;
//        private LocalDateTime updatedAt;
//    }
//
//    @Data
//    public static class LoginRequest {
//        private String email;
//        private String password;
//    }
//
//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class TokenResponse {
//        private String accessToken;
//        private String tokenType = "Bearer";
//    }
//
//    // User 도메인 객체를 UserResponse로 변환하는 메서드
//    private UserResponse toResponse(User user) {
//        UserResponse response = new UserResponse();
//        response.setId(user.getId());
//        response.setName(user.getName());
//        response.setEmail(user.getEmail());
//        response.setMemo(user.getMemo());
//        response.setCreatedAt(user.getCreatedAt());
//        response.setUpdatedAt(user.getUpdatedAt());
//        return response;
//    }
//
//    @PostMapping("/login")
//    public TokenResponse login(@RequestBody LoginRequest request) {
//        String token = userCommandUseCase.login(request.getEmail(), request.getPassword());
//        return TokenResponse.builder()
//                .accessToken(token)
//                .build();
//    }
//
//    @GetMapping("/me")
//    public UserResponse getCurrentUser(@AuthenticationPrincipal CurrentUser currentUser) {
//        return toResponse(userQueryUseCase.getUserById(currentUser.getId()));
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponse createUser(@RequestBody UserCreateRequest request) {
//        User user = userCommandUseCase.createUser(
//                request.getName(),
//                request.getEmail(),
//                request.getPassword(),
//                request.getMemo()
//        );
//        return toResponse(user);
//    }
//
//    @GetMapping
//    public List<UserResponse> getUsers(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size) {
//        return userQueryUseCase.getUsers(page, size).stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList());
//    }
//
//    @GetMapping("/{userId}")
//    public UserResponse getUser(
//            @PathVariable String userId,
//            @AuthenticationPrincipal CurrentUser currentUser) {
//        User currentUserDetails = userQueryUseCase.getUserById(currentUser.getId());
//
//        if (!currentUserDetails.getId().equals(userId) &&
//                !JwtTokenProvider.Role.ROLE_ADMIN.equals(currentUser.getRole())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
//        }
//
//        return toResponse(userQueryUseCase.getUserById(userId));
//    }
//
//    @PatchMapping("/{userId}")
//    public UserResponse updateUser(
//            @PathVariable String userId,
//            @RequestBody UserUpdateRequest request,
//            @AuthenticationPrincipal CurrentUser currentUser) {
//        User currentUserDetails = userQueryUseCase.getUserById(currentUser.getId());
//
//        if (!currentUserDetails.getId().equals(userId) &&
//                !JwtTokenProvider.Role.ROLE_ADMIN.equals(currentUser.getRole())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
//        }
//
//        User updatedUser = userCommandUseCase.updateUser(
//                userId,
//                request.getName(),
//                request.getEmail(),
//                request.getPassword(),
//                request.getMemo()
//        );
//        return toResponse(updatedUser);
//    }
//
//    @DeleteMapping("/{userId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteUser(
//            @PathVariable String userId,
//            @AuthenticationPrincipal CurrentUser currentUser) {
//        User currentUserDetails = userQueryUseCase.getUserById(currentUser.getId());
//
//        if (!currentUserDetails.getId().equals(userId) &&
//                !JwtTokenProvider.Role.ROLE_ADMIN.equals(currentUser.getRole())) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
//        }
//
//        userCommandUseCase.deleteUser(userId);
//    }
//}
