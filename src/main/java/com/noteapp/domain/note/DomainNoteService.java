package com.noteapp.domain.note;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DomainNoteService {

    public Note createNote(String userId, String title, String content, String memoDate, Set<Tag> tag) {
        Note note = new Note();
        note.setUserId(userId);
        note.setTitle(title);
        note.setContent(content);
        note.setMemoDate(memoDate);
        note.setTag(tag);
        note.initializeTimestamps();
        return note;
    }

    public Note updateNote(Note note, String title, String content, String memoDate, Set<Tag> tag) {
        if (title != null) note.setTitle(title);
        if (content != null) note.setContent(content);
        if (memoDate != null) note.setMemoDate(memoDate);
        if (tag != null) {
            note.getTag().clear();
            note.setTag(tag);
        }
        note.updateTimestamp();
        return note;
    }

    public void clearTags(Note note) {
        note.getTag().clear();
    }
}


//package com.noteapp.domain.note;
//
//import com.noteapp.adapter.in.dto.NoteDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class DomainNoteService {
//
//    public Note createNote(String userId, NoteDto.NoteCreateRequest request, Set<Tag> tags) {
//        Note note = new Note();
//        note.setUserId(userId);
//        note.setTitle(request.getTitle());
//        note.setContent(request.getContent());
//        note.setMemoDate(request.getMemoDate());
//        note.setTags(tags);
//        note.initializeTimestamps();
//        return note;
//    }
//
//    public Note updateNote(Note note, NoteDto.NoteUpdateRequest request, Set<Tag> tags) {
//        if (request.getTitle() != null) note.setTitle(request.getTitle());
//        if (request.getContent() != null) note.setContent(request.getContent());
//        if (request.getMemoDate() != null) note.setMemoDate(request.getMemoDate());
//        if (request.getTags() != null) {
//            note.getTags().clear();
//            note.setTags(tags);
//        }
//        note.updateTimestamp();
//        return note;
//    }
//
//    public void clearTags(Note note) {
//        note.getTags().clear();
//    }
//}