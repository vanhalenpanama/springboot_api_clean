package com.noteapp.domain.user.port;

import com.noteapp.domain.user.User;
import java.util.List;
import java.util.Optional;

public interface UserPort {
    User save(User user);
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    List<User> findAll(int page, int size);
    boolean existsByEmail(String email);
    void deleteById(String id);
}