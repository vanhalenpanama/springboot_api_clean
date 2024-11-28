package com.noteapp.domain.tag.port;

import com.noteapp.adapter.out.persistence.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, String> {
    Optional<TagEntity> findByName(String name);

    @Query("SELECT t FROM TagEntity t WHERE t.notes IS EMPTY")
    List<TagEntity> findByNotesEmpty();
}