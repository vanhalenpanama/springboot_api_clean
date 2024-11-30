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

//    private User(String name, String email, String password, String memo) {
//        this.id = UUID.randomUUID().toString();
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.memo = memo;
//        this.active = true;
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = this.createdAt;
//    }
    public void initializeTimestamps() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}


//package com.noteapp.domain.user;
//
//import lombok.Getter;
//import lombok.Setter;
//import java.time.LocalDateTime;
//
//@Getter @Setter
//public class User {
//    private String id;
//    private String name;
//    private String email;
//    private String password;
//    private String memo;
//    private boolean isActive;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//
//    public void initializeTimestamps() {
//        this.createdAt = LocalDateTime.now();
//        this.updatedAt = LocalDateTime.now();
//    }
//
//    public void updateTimestamp() {
//        this.updatedAt = LocalDateTime.now();
//    }
//}
