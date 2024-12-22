package com.noteapp.application.user;


import com.noteapp.domain.user.User;
import com.noteapp.domain.user.DomainUserService;
import com.noteapp.domain.user.UserRepository;
import com.noteapp.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserCommandUseCase, UserQueryUseCase {
    private final UserRepository userRepository;
    private final DomainUserService domainUserService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
//    private final UserDtoMapper userDtoMapper;

    @Override
    @Transactional
    public User createUser(String name, String email, String password, String memo) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = domainUserService.createUser(name, email, encodedPassword, memo);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(String userId, String name, String email, String password, String memo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String encodedPassword = password != null ? passwordEncoder.encode(password) : null;
        User updatedUser = domainUserService.updateUser(user, name, email, encodedPassword, memo);
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtTokenProvider.createToken(user.getId(), JwtTokenProvider.Role.ROLE_USER);
    }

    @Override
    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> getUsers(int page, int size) {
        return userRepository.findAll(page, size);
    }
}
