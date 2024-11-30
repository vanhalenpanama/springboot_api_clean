package com.noteapp.domain.note;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag save(Tag tag);

    Optional<Tag> findByName(String name);

    List<Tag> findUnusedTags();

    void deleteAll(List<Tag> tags);
}