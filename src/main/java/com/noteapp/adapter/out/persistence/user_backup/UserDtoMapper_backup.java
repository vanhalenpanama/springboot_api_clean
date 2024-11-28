//package com.noteapp.application.user;
//

//import com.noteapp.domain.user.User;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserDtoMapper {
//
//    public UserDto_bakcup.UserResponse toDto(User user) {
//        UserDto_bakcup.UserResponse response = new UserDto_bakcup.UserResponse();
//        response.setId(user.getId());
//        response.setName(user.getName());
//        response.setEmail(user.getEmail());
//        response.setMemo(user.getMemo());
//        response.setCreatedAt(user.getCreatedAt());
//        response.setUpdatedAt(user.getUpdatedAt());
//        return response;
//    }
//
//    public UserDto_bakcup.TokenResponse toTokenDto(String token) {
//        return UserDto_bakcup.TokenResponse.builder()
//                .accessToken(token)
//                .build();
//    }
//}