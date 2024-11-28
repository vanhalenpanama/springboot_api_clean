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

//package com.noteapp.application.user;
//
//import com.noteapp.domain.user.User;
//import com.noteapp.domain.user.DomainUserService;
//import com.noteapp.domain.user.UserRepository;
//import com.noteapp.adapter.in.dto.UserDto;
//import com.noteapp.common.jwt.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class UserService implements UserUseCase_backup {
//    private final UserRepository userRepository;
//    private final DomainUserService domainUserService;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    @Transactional
//    public UserDto.UserResponse createUser(UserDto.UserCreateRequest request) {
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("User already exists");
//        }
//
//        // 비밀번호 인코딩된 새로운 request 객체 생성
//        UserDto.UserCreateRequest encodedRequest = new UserDto.UserCreateRequest();
//        encodedRequest.setName(request.getName());
//        encodedRequest.setEmail(request.getEmail());
//        encodedRequest.setPassword(passwordEncoder.encode(request.getPassword()));
//        encodedRequest.setMemo(request.getMemo());
//
//        // 도메인 서비스를 통해 User 생성
//        User user = domainUserService.createUser(encodedRequest);
//
//        // UserPort를 통해 저장하고 DTO로 변환하여 반환
//        return convertToDto(userRepository.save(user));
//    }
//
//
//
//    @Override
//    public UserDto.UserResponse getUserById(String id) {
//        return convertToDto(userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found")));
//    }
//
//    @Override
//    public UserDto.UserResponse getUserByEmail(String email) {
//        return convertToDto(userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found")));
//    }
//
//    @Override
//    public List<UserDto.UserResponse> getUsers(int page, int size) {
//        return userRepository.findAll(page, size).stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional
//    public UserDto.UserResponse updateUser(String userId, UserDto.UserUpdateRequest request) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (request.getPassword() != null) {
//            request.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
//
//        // 수정된 User 객체를 받아서 저장
//        User updatedUser = domainUserService.updateUser(user, request);
//        return convertToDto(userRepository.save(updatedUser));
//    }
//
//    @Override
//    @Transactional
//    public void deleteUser(String userId) {
//        userRepository.deleteById(userId);
//    }
//
//    @Override
//    public UserDto.TokenResponse login(UserDto.LoginRequest request) {
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        String token = jwtTokenProvider.createToken(user.getId(), JwtTokenProvider.Role.ROLE_USER);
//        return UserDto.TokenResponse.builder()
//                .accessToken(token)
//                .build();
//    }
//
//    private UserDto.UserResponse convertToDto(User user) {
//        UserDto.UserResponse response = new UserDto.UserResponse();
//        response.setId(user.getId());
//        response.setName(user.getName());
//        response.setEmail(user.getEmail());
//        response.setMemo(user.getMemo());
//        response.setCreatedAt(user.getCreatedAt());
//        response.setUpdatedAt(user.getUpdatedAt());
//        return response;
//    }
//}