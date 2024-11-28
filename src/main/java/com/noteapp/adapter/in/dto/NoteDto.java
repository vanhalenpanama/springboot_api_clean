package com.noteapp.adapter.in.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.List;

public class NoteDto {
    @Data
    public static class TagBase {
        private String name;
    }

    @Data
    public static class TagCreate extends TagBase {
    }

    @Data
    public static class TagResponse extends TagBase {
        private String id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    public static class NoteCreateRequest {
        private String title;
        private String content;
        private String memoDate;
        private Set<String> tags;  // 명시적 타입 지정
    }

    @Data
    public static class NoteUpdateRequest {
        private String title;
        private String content;
        private String memoDate;
        private Set<String> tags;  // 명시적 타입 지정
    }

    @Data
    public static class NoteResponse {
        private String id;
        private String userId;
        private String title;
        private String content;
        private String memoDate;
        private Set<String> tags;  // 명시적 타입 지정
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    public static class NoteList {
        private int totalCount;
        private int page;
        private int itemsPerPage;
        private List<NoteResponse> notes;  // Set에서 List로 변경하고 타입 지정
    }
}