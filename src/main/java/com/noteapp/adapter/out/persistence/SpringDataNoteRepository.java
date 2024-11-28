package com.noteapp.adapter.out.persistence;

import com.noteapp.domain.note.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataNoteRepository extends JpaRepository<NoteEntity, String> {
    Page<NoteEntity> findByUserId(String userId, Pageable pageable);
    Optional<NoteEntity> findByUserIdAndId(String userId, String id);
}