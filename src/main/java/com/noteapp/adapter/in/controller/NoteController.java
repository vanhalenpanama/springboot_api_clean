package com.noteapp.adapter.in.controller;

import com.noteapp.adapter.in.dto.NoteDto;
import com.noteapp.domain.note.Note;
import com.noteapp.domain.note.Tag;
import com.noteapp.application.note.NoteUseCase;
import com.noteapp.common.jwt.JwtTokenProvider.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteUseCase noteUseCase;

    private NoteDto.NoteResponse toResponse(Note note) {
        NoteDto.NoteResponse response = new NoteDto.NoteResponse();
        response.setId(note.getId());
        response.setUserId(note.getUserId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setMemoDate(note.getMemoDate());
        response.setTags(note.getTag().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet()));
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        return response;
    }

    private NoteDto.NoteList toNoteList(List<Note> notes, int totalCount, int page, int itemsPerPage) {
        NoteDto.NoteList noteList = new NoteDto.NoteList();
        noteList.setTotalCount(totalCount);
        noteList.setPage(page);
        noteList.setItemsPerPage(itemsPerPage);
        noteList.setNotes(notes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
        return noteList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDto.NoteResponse createNote(
            @RequestBody NoteDto.NoteCreateRequest request,
            @AuthenticationPrincipal CurrentUser currentUser) {
        Note note = noteUseCase.createNote(
                currentUser.getId(),
                request.getTitle(),
                request.getContent(),
                request.getMemoDate(),
                request.getTags()
        );
        return toResponse(note);
    }

    @GetMapping
    public NoteDto.NoteList getNotes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int itemsPerPage,
            @AuthenticationPrincipal CurrentUser currentUser) {
        List<Note> notes = noteUseCase.getNotes(currentUser.getId(), page, itemsPerPage);
        return toNoteList(notes, notes.size(), page, itemsPerPage);
    }

    @GetMapping("/{id}")
    public NoteDto.NoteResponse getNote(
            @PathVariable String id,
            @AuthenticationPrincipal CurrentUser currentUser) {
        return toResponse(noteUseCase.getNote(currentUser.getId(), id));
    }

    @PutMapping("/{id}")
    public NoteDto.NoteResponse updateNote(
            @PathVariable String id,
            @RequestBody NoteDto.NoteUpdateRequest request,
            @AuthenticationPrincipal CurrentUser currentUser) {
        Note note = noteUseCase.updateNote(
                currentUser.getId(),
                id,
                request.getTitle(),
                request.getContent(),
                request.getMemoDate(),
                request.getTags()
        );
        return toResponse(note);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(
            @PathVariable String id,
            @AuthenticationPrincipal CurrentUser currentUser) {
        noteUseCase.deleteNote(currentUser.getId(), id);
    }
}









//package com.noteapp.adapter.in.controller;
//
//import com.noteapp.adapter.in.dto.NoteDto;
//import com.noteapp.domain.note.Note;
//import com.noteapp.domain.note.Tag;
//import com.noteapp.application.note.NoteUseCase;
//import com.noteapp.common.jwt.JwtTokenProvider.CurrentUser;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@RestController
//@RequestMapping("/notes")
//@RequiredArgsConstructor
//public class NoteController {
//    private final NoteUseCase noteUseCase;
//
//    // Note 도메인 객체를 NoteResponse로 변환하는 메서드 추가
//    private NoteDto.NoteResponse toResponse(Note note) {
//        NoteDto.NoteResponse response = new NoteDto.NoteResponse();
//        response.setId(note.getId());
//        response.setUserId(note.getUserId());
//        response.setTitle(note.getTitle());
//        response.setContent(note.getContent());
//        response.setMemoDate(note.getMemoDate());
//        response.setTags(note.getTags().stream()
//                .map(Tag::getName)
//                .collect(Collectors.toSet()));
//        response.setCreatedAt(note.getCreatedAt());
//        response.setUpdatedAt(note.getUpdatedAt());
//        return response;
//    }
//
//    private NoteDto.NoteList toNoteList(List<Note> notes, int totalCount, int page, int itemsPerPage) {
//        NoteDto.NoteList noteList = new NoteDto.NoteList();
//        noteList.setTotalCount(totalCount);
//        noteList.setPage(page);
//        noteList.setItemsPerPage(itemsPerPage);
//        noteList.setNotes(notes.stream()
//                .map(this::toResponse)
//                .collect(Collectors.toList()));
//        return noteList;
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public NoteDto.NoteResponse createNote(
//            @RequestBody NoteDto.NoteCreateRequest request,
//            @AuthenticationPrincipal CurrentUser currentUser) {
//        Note note = noteUseCase.createNote(
//                currentUser.getId(),
//                request.getTitle(),
//                request.getContent(),
//                request.getMemoDate(),
//                request.getTags()
//        );
//        return toResponse(note);
//    }
//}
//
//    @GetMapping
//    public NoteDto.NoteList getNotes(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int itemsPerPage,
//            @AuthenticationPrincipal CurrentUser currentUser) {
//        return noteUseCase.getNotes(currentUser.getId(), page, itemsPerPage);
//    }
//
//    @GetMapping("/{id}")
//    public NoteDto.NoteResponse getNote(
//            @PathVariable String id,
//            @AuthenticationPrincipal CurrentUser currentUser) {
//        return noteUseCase.getNote(currentUser.getId(), id);
//    }
//
//    @PutMapping("/{id}")
//    public NoteDto.NoteResponse updateNote(
//            @PathVariable String id,
//            @RequestBody NoteDto.NoteUpdateRequest request,
//            @AuthenticationPrincipal CurrentUser currentUser) {
//        return noteUseCase.updateNote(currentUser.getId(), id, request);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteNote(
//            @PathVariable String id,
//            @AuthenticationPrincipal CurrentUser currentUser) {
//        noteUseCase.deleteNote(currentUser.getId(), id);
//    }
//}
