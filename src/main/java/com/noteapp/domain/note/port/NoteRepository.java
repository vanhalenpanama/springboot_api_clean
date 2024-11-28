package com.noteapp.domain.note.port;

import com.noteapp.adapter.out.persistence.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<NoteEntity, String> {
    Page<NoteEntity> findByUserId(String userId, Pageable pageable);
    Optional<NoteEntity> findByUserIdAndId(String userId, String id);
}