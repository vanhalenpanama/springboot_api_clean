package com.noteapp.adapter.out.persistence;

import com.noteapp.domain.user.User;
import com.noteapp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserRepository {
    private final InfraUserRepository infraUserRepository;

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setMemo(user.getMemo());
        entity.setActive(user.isActive());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    private User toModel(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setMemo(entity.getMemo());
        user.setActive(entity.isActive());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        return user;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = toEntity(user);
        UserEntity savedEntity = infraUserRepository.save(userEntity);
        return toModel(savedEntity);
    }

    @Override
    public Optional<User> findById(String id) {
        return infraUserRepository.findById(id).map(this::toModel);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return infraUserRepository.findByEmail(email).map(this::toModel);
    }


    @Override
    public List<User> findAll(int page, int size) {
        return infraUserRepository.findAll(PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return infraUserRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(String id) {
        infraUserRepository.deleteById(id);
    }
}