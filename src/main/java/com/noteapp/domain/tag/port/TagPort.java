package com.noteapp.domain.tag.port;

import com.noteapp.domain.tag.Tag;
import java.util.List;
import java.util.Optional;

public interface TagPort {
    Tag save(Tag tag);

    Optional<Tag> findByName(String name);

    List<Tag> findUnusedTags();

    void deleteAll(List<Tag> tags);
}