package com.noteapp.domain.user.port;

import com.noteapp.adapter.out.persistence.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, String> {  // String -> UUID로 변경
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
}