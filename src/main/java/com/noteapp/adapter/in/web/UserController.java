package com.noteapp.adapter.in.web;

import com.noteapp.adapter.in.dto.UserDto;
import com.noteapp.application.user.usecase.UserUseCase;
import com.noteapp.domain.user.User;
import com.noteapp.common.jwt.JwtTokenProvider;
import com.noteapp.common.jwt.JwtTokenProvider.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

      private final UserUseCase userUseCase;

    @PostMapping("/login")
    public UserDto.TokenResponse login(@RequestBody UserDto.LoginRequest request) {
        return userUseCase.login(request);
    }


    @GetMapping("/me")
    public UserDto.UserResponse getCurrentUser(@AuthenticationPrincipal CurrentUser currentUser) {
        return userUseCase.getUserById(currentUser.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.UserResponse createUser(@RequestBody UserDto.UserCreateRequest request) {
        return userUseCase.createUser(request);
    }

    @GetMapping
    public List<UserDto.UserResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return userUseCase.getUsers(page, size);
    }

    @GetMapping("/{userId}")
    public UserDto.UserResponse getUser(
            @PathVariable String userId,
            @AuthenticationPrincipal CurrentUser currentUser) {

        UserDto.UserResponse currentUserDetails = userUseCase.getUserById(currentUser.getId());

        if (!currentUserDetails.getId().equals(userId) &&
                !JwtTokenProvider.Role.ROLE_ADMIN.equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
        }

        return userUseCase.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto.UserResponse updateUser(
            @PathVariable String userId,
            @RequestBody UserDto.UserUpdateRequest request,
            @AuthenticationPrincipal CurrentUser currentUser) {
        UserDto.UserResponse currentUserDetails = userUseCase.getUserById(currentUser.getId());

        if (!currentUserDetails.getId().equals(userId) &&
                !JwtTokenProvider.Role.ROLE_ADMIN.equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
        }

        return userUseCase.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @PathVariable String userId,
            @AuthenticationPrincipal CurrentUser currentUser) {
        UserDto.UserResponse currentUserDetails = userUseCase.getUserById(currentUser.getId());

        if (!currentUserDetails.getId().equals(userId) &&
                !JwtTokenProvider.Role.ROLE_ADMIN.equals(currentUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근 권한이 없습니다");
        }

        userUseCase.deleteUser(userId);
    }

    private UserDto.UserResponse convertToDto(User user) {
        UserDto.UserResponse response = new UserDto.UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setMemo(user.getMemo());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}