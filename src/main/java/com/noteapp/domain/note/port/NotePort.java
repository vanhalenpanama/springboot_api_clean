package com.noteapp.domain.note.port;

import com.noteapp.domain.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface NotePort {
    Note save(Note note);

    Optional<Note> findByUserIdAndId(String userId, String id);

    Page<Note> findByUserId(String userId, Pageable pageable);

    void delete(Note note);
}