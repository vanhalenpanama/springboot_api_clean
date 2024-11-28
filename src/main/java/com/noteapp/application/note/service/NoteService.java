package com.noteapp.application.note.service;

import com.noteapp.application.note.service.usecase.NoteUseCase;
import com.noteapp.domain.note.Note;
import com.noteapp.domain.note.DomainNoteService;
import com.noteapp.domain.note.port.NotePort;
import com.noteapp.domain.tag.Tag;
import com.noteapp.domain.tag.port.TagPort;
import com.noteapp.adapter.in.dto.NoteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService implements NoteUseCase {
    private final NotePort notePort;
    private final TagPort tagPort;
    private final DomainNoteService domainNoteService;

    @Override
    public NoteDto.NoteList getNotes(String userId, int page, int itemsPerPage) {
        Page<Note> notePage = notePort.findByUserId(userId, PageRequest.of(page - 1, itemsPerPage));

        NoteDto.NoteList noteList = new NoteDto.NoteList();
        noteList.setTotalCount((int) notePage.getTotalElements());
        noteList.setPage(page);
        noteList.setItemsPerPage(itemsPerPage);
        noteList.setNotes(notePage.getContent().stream()
                .map(this::convertToNoteResponse)
                .collect(Collectors.toList()));

        return noteList;
    }

    @Override
    public NoteDto.NoteResponse getNote(String userId, String id) {
        Note note = notePort.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));
        return convertToNoteResponse(note);
    }

    @Override
    @Transactional
    public NoteDto.NoteResponse createNote(String userId, NoteDto.NoteCreateRequest request) {
        Set<Tag> tags = processTagNames((Set<String>) request.getTags());
        Note note = domainNoteService.createNote(userId, request, tags);
        return convertToNoteResponse(notePort.save(note));
    }

    @Override
    @Transactional
    public NoteDto.NoteResponse updateNote(String userId, String id, NoteDto.NoteUpdateRequest request) {
        Note note = notePort.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        Set<Tag> tags = request.getTags() != null ? processTagNames((Set<String>) request.getTags()) : new HashSet<>();
        Note updatedNote = domainNoteService.updateNote(note, request, tags);
        return convertToNoteResponse(notePort.save(updatedNote));
    }

    @Override
    @Transactional
    public void deleteNote(String userId, String id) {
        Note note = notePort.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        domainNoteService.clearTags(note);
        notePort.delete(note);

        List<Tag> unusedTags = tagPort.findUnusedTags();
        tagPort.deleteAll(unusedTags);
    }


    private Set<Tag> processTagNames(Set<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        tagNames.forEach(tagName -> {
            Tag tag = tagPort.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        newTag.initializeTimestamps();
                        return newTag;
                    });
            tags.add(tag);
        });
        return tags;
    }

    private NoteDto.NoteResponse convertToNoteResponse(Note note) {
        NoteDto.NoteResponse response = new NoteDto.NoteResponse();
        response.setId(note.getId());
        response.setUserId(note.getUserId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setMemoDate(note.getMemoDate());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());

        Set<String> tagNames = note.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
        response.setTags(tagNames);

        return response;
    }
}