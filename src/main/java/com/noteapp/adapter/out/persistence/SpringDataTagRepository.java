package com.noteapp.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface SpringDataTagRepository extends JpaRepository<TagEntity, String> {
    Optional<TagEntity> findByName(String name);

    @Query("SELECT t FROM TagEntity t WHERE t.note IS EMPTY")
    List<TagEntity> findByNotesEmpty();
}