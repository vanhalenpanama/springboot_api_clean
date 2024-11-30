package com.noteapp.adapter.out.persistence;

import com.noteapp.domain.note.Tag;
import com.noteapp.domain.note.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TagPersistenceAdapter implements TagRepository {
    private final SpringDataTagRepository repository;

    @Override
    public Tag save(Tag tag) {
        TagEntity entity = toEntity(tag);
        TagEntity savedEntity = repository.save(entity);
        return toModel(savedEntity);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return repository.findByName(name).map(this::toModel);
    }

    @Override
    public List<Tag> findUnusedTags() {
        return repository.findByNotesEmpty().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll(List<Tag> tags) {
        List<TagEntity> entities = tags.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        repository.deleteAll(entities);
    }

    private TagEntity toEntity(Tag tag) {
        TagEntity entity = new TagEntity();
        entity.setId(tag.getId());
        entity.setName(tag.getName());
        entity.setCreatedAt(tag.getCreatedAt());
        entity.setUpdatedAt(tag.getUpdatedAt());
        return entity;
    }

    private Tag toModel(TagEntity entity) {
        Tag tag = new Tag();
        tag.setId(entity.getId());
        tag.setName(entity.getName());
        tag.setCreatedAt(entity.getCreatedAt());
        tag.setUpdatedAt(entity.getUpdatedAt());
        return tag;
    }
}