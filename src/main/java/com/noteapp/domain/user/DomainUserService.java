package com.noteapp.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainUserService {

    public User createUser(String name, String email, String password, String memo) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setMemo(memo);
        user.initializeTimestamps();
        return user;
    }

    public User updateUser(User user, String name, String email, String password, String memo) {
        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        if (password != null) user.setPassword(password);
        if (memo != null) user.setMemo(memo);
        user.updateTimestamp();
        return user;
    }

    public boolean validatePassword(User user, String password) {
        return user.getPassword().equals(password);
    }
}
