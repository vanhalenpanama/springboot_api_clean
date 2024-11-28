package com.noteapp.application.note.usecase;

import com.noteapp.adapter.in.dto.NoteDto;

public interface NoteUseCase {
    NoteDto.NoteList getNotes(String userId, int page, int itemsPerPage);

    NoteDto.NoteResponse getNote(String userId, String id);

    NoteDto.NoteResponse createNote(String userId, NoteDto.NoteCreateRequest request);

    NoteDto.NoteResponse updateNote(String userId, String id, NoteDto.NoteUpdateRequest request);

    void deleteNote(String userId, String id);
}