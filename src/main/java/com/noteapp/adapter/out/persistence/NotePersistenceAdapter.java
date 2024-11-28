package com.noteapp.adapter.out.persistence;

import com.noteapp.domain.note.Note;
import com.noteapp.domain.note.port.NotePort;
import com.noteapp.domain.note.port.NoteRepository;
import com.noteapp.domain.tag.Tag;
import com.noteapp.domain.tag.port.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotePersistenceAdapter implements NotePort {
    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;

    @Override
    public Note save(Note note) {
        NoteEntity noteEntity = toEntity(note);
        NoteEntity savedEntity = noteRepository.save(noteEntity);
        return toModel(savedEntity);
    }

    @Override
    public Optional<Note> findByUserIdAndId(String userId, String id) {
        return noteRepository.findByUserIdAndId(userId, id).map(this::toModel);
    }

    @Override
    public Page<Note> findByUserId(String userId, Pageable pageable) {
        return noteRepository.findByUserId(userId, pageable).map(this::toModel);
    }

    @Override
    public void delete(Note note) {
        noteRepository.delete(toEntity(note));
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
        entity.setTags(note.getTags().stream()
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
        note.setTags(entity.getTags().stream()
                .map(this::tagToModel)
                .collect(Collectors.toSet()));
        return note;
    }


    private TagEntity tagToEntity(Tag tag) {
        if (tag.getId() != null) {
            // 기존 태그인 경우 참조만 가져옴
            return tagRepository.getReferenceById(tag.getId());
        }

        // 새로운 태그인 경우 새 엔티티 생성
        TagEntity entity = new TagEntity();
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
}