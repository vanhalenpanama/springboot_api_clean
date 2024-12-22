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
