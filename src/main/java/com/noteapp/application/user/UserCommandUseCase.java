package com.noteapp.application.user;

import com.noteapp.domain.user.User;

public interface UserCommandUseCase {
    User createUser(String name, String email, String password, String memo);
    User updateUser(String userId, String name, String email, String password, String memo);
    void deleteUser(String userId);
    String login(String email, String password);
}