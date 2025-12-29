package org.example.notesrest.service;

import org.example.notesrest.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    Long createNote(Note note);

    Note getNote(Long id);

    Page<Note> getAllNotes(Pageable pageable);

    void replaceNote(Long id, Note note);

    void deleteNote(Long id);
}
