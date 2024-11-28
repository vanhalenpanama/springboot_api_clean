package com.noteapp.application.user.usecase;

import com.noteapp.adapter.in.dto.UserDto;
import java.util.List;

public interface UserUseCase {
    UserDto.UserResponse createUser(UserDto.UserCreateRequest request);
    UserDto.UserResponse getUserById(String id);
    UserDto.UserResponse getUserByEmail(String email);
    List<UserDto.UserResponse> getUsers(int page, int size);
    UserDto.UserResponse updateUser(String userId, UserDto.UserUpdateRequest request);
    void deleteUser(String userId);
    UserDto.TokenResponse login(UserDto.LoginRequest request);
}