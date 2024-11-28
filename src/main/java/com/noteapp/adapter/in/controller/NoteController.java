package com.noteapp.adapter.in.controller;

import com.noteapp.adapter.in.dto.NoteDto;
import com.noteapp.application.note.service.usecase.NoteUseCase;
import com.noteapp.common.jwt.JwtTokenProvider.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteUseCase noteUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDto.NoteResponse createNote(
            @RequestBody NoteDto.NoteCreateRequest request,
            @AuthenticationPrincipal CurrentUser currentUser) {
        return noteUseCase.createNote(currentUser.getId(), request);
    }

    @GetMapping
    public NoteDto.NoteList getNotes(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int itemsPerPage,
            @AuthenticationPrincipal CurrentUser currentUser) {
        return noteUseCase.getNotes(currentUser.getId(), page, itemsPerPage);
    }

    @GetMapping("/{id}")
    public NoteDto.NoteResponse getNote(
            @PathVariable String id,
            @AuthenticationPrincipal CurrentUser currentUser) {
        return noteUseCase.getNote(currentUser.getId(), id);
    }

    @PutMapping("/{id}")
    public NoteDto.NoteResponse updateNote(
            @PathVariable String id,
            @RequestBody NoteDto.NoteUpdateRequest request,
            @AuthenticationPrincipal CurrentUser currentUser) {
        return noteUseCase.updateNote(currentUser.getId(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(
            @PathVariable String id,
            @AuthenticationPrincipal CurrentUser currentUser) {
        noteUseCase.deleteNote(currentUser.getId(), id);
    }
}