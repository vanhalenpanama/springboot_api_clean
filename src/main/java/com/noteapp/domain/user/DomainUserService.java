package com.noteapp.domain.user;

import com.noteapp.adapter.in.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainUserService {
    public User createUser(UserDto.UserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setMemo(request.getMemo());
        user.initializeTimestamps();
        return user;
    }

    public User updateUser(User user, UserDto.UserUpdateRequest request) {
        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(request.getPassword());
        if (request.getMemo() != null) user.setMemo(request.getMemo());
        user.updateTimestamp();
        return user;
    }
    public boolean validatePassword(User user, String rawPassword, String encodedPassword) {
        return encodedPassword.equals(user.getPassword());
    }
}
