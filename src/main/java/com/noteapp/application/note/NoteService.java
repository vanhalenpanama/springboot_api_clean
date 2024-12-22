package com.noteapp.application.note;

import com.noteapp.domain.note.Note;
import com.noteapp.domain.note.DomainNoteService;
import com.noteapp.domain.note.NoteRepository;
import com.noteapp.domain.note.Tag;
import com.noteapp.domain.note.TagRepository;
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

@Service
@RequiredArgsConstructor
public class NoteService implements NoteUseCase {
    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final DomainNoteService domainNoteService;

    @Override
    public List<Note> getNotes(String userId, int page, int itemsPerPage) {
        Page<Note> notePage = noteRepository.findByUserId(userId, PageRequest.of(page - 1, itemsPerPage));
        return notePage.getContent();
    }

    @Override
    public Note getNote(String userId, String id) {
        return noteRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @Override
    @Transactional
    public Note createNote(String userId, String title, String content, String memoDate, Set<String> tagNames) {
        Set<Tag> tags = processTagNames(tagNames);
        Note note = domainNoteService.createNote(userId, title, content, memoDate, tags);
        return noteRepository.save(note);
    }

    @Override
    @Transactional
    public Note updateNote(String userId, String id, String title, String content, String memoDate, Set<String> tagNames) {
        Note note = noteRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        Set<Tag> tags = tagNames != null ? processTagNames(tagNames) : new HashSet<>();
        Note updatedNote = domainNoteService.updateNote(note, title, content, memoDate, tags);
        return noteRepository.save(updatedNote);
    }

    @Override
    @Transactional
    public void deleteNote(String userId, String id) {
        Note note = noteRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY));

        domainNoteService.clearTags(note);
        noteRepository.delete(note);

        List<Tag> unusedTags = tagRepository.findUnusedTags();
        tagRepository.deleteAll(unusedTags);
    }

    private Set<Tag> processTagNames(Set<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        tagNames.forEach(tagName -> {
            Tag tag = tagRepository.findByName(tagName)
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
}
