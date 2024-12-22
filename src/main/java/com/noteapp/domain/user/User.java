package com.noteapp.domain.user;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import java.time.LocalDateTime;

@Getter @Setter
public class User {
    private String id = UUID.randomUUID().toString();
    private String name;
    private String email;
    private String password;
    private String memo;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void initializeTimestamps() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}

