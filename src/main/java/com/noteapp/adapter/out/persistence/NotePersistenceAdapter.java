package com.noteapp.adapter.out.persistence;

import com.noteapp.domain.note.Note;
import com.noteapp.domain.note.NoteRepository;
import com.noteapp.domain.note.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotePersistenceAdapter implements NoteRepository {
    private final SpringDataNoteRepository repository;

    private TagEntity tagToEntity(Tag tag) {
        TagEntity entity = new TagEntity();
        entity.setId(tag.getId());
        entity.setName(tag.getName());
        entity.setCreatedAt(tag.getCreatedAt());
        entity.setUpdatedAt(tag.getUpdatedAt());
        return entity;
    }

    private Tag tagToModel(TagEntity entity) {
        Tag tag = new Tag();
        tag.setId(entity.getId());
        tag.setName(entity.getName());
        tag.setCreatedAt(entity.getCreatedAt());
        tag.setUpdatedAt(entity.getUpdatedAt());
        return tag;
    }


    private NoteEntity toEntity(Note note) {
        NoteEntity entity = new NoteEntity();
        entity.setId(note.getId());
        entity.setUserId(note.getUserId());
        entity.setTitle(note.getTitle());
        entity.setContent(note.getContent());
        entity.setMemoDate(note.getMemoDate());
        entity.setCreatedAt(note.getCreatedAt());
        entity.setUpdatedAt(note.getUpdatedAt());
        entity.setTag(note.getTag().stream()
                .map(this::tagToEntity)
                .collect(Collectors.toSet()));
        return entity;
    }



    private Note toModel(NoteEntity entity) {
        Note note = new Note();
        note.setId(entity.getId());
        note.setUserId(entity.getUserId());
        note.setTitle(entity.getTitle());
        note.setContent(entity.getContent());
        note.setMemoDate(entity.getMemoDate());
        note.setCreatedAt(entity.getCreatedAt());
        note.setUpdatedAt(entity.getUpdatedAt());
        note.setTag(entity.getTag().stream()
                .map(this::tagToModel)
                .collect(Collectors.toSet()));
        return note;
    }

    @Override
    public Note save(Note note) {
        NoteEntity entity = toEntity(note);
        return toModel(repository.save(entity));
    }

    @Override
    public Optional<Note> findByUserIdAndId(String userId, String id) {
        return repository.findByUserIdAndId(userId, id).map(this::toModel);
    }

    @Override
    public Page<Note> findByUserId(String userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable).map(this::toModel);
    }

    @Override
    public void delete(Note note) {
        repository.delete(toEntity(note));
    }
}