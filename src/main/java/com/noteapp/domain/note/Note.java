package com.noteapp.domain.note;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class Note {
    private String id;
    private String userId;
    private String title;
    private String content;
    private String memoDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Tag> tag = new HashSet<>();

    public void initializeTimestamps() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addTag(Tag tag) {
        this.tag.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tag.remove(tag);
    }
}

