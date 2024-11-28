package com.noteapp.application.user;

import com.noteapp.domain.user.User;
import java.util.List;

public interface UserQueryUseCase {
    User getUserById(String id);
    User getUserByEmail(String email);
    List<User> getUsers(int page, int size);
}