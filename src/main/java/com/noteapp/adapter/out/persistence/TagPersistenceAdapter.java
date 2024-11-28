package com.noteapp.adapter.out.persistence;

import com.noteapp.domain.tag.Tag;
import com.noteapp.domain.tag.port.TagPort;
import com.noteapp.domain.tag.port.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TagPersistenceAdapter implements TagPort {
    private final TagRepository tagRepository;

    @Override
    public Tag save(Tag tag) {
        TagEntity tagEntity = toEntity(tag);
        TagEntity savedEntity = tagRepository.save(tagEntity);
        return toModel(savedEntity);
    }


    @Override
    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name).map(this::toModel);
    }

    @Override
    public List<Tag> findUnusedTags() {
        return tagRepository.findByNotesEmpty().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll(List<Tag> tags) {
        List<TagEntity> entities = tags.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        tagRepository.deleteAll(entities);
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