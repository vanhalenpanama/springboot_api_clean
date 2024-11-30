package com.noteapp.application.note;

import com.noteapp.domain.note.Note;
import java.util.List;
import java.util.Set;

public interface NoteUseCase {
    // Command
    Note createNote(String userId, String title, String content, String memoDate, Set<String> tags);
    Note updateNote(String userId, String id, String title, String content, String memoDate, Set<String> tags);
    void deleteNote(String userId, String id);

    // Query
    List<Note> getNotes(String userId, int page, int itemsPerPage);
    Note getNote(String userId, String id);
}

