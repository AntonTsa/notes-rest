package org.example.notesrest.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.notesrest.entity.Note;
import org.example.notesrest.repo.NoteRepository;
import org.example.notesrest.service.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public Long createNote(Note note) {
        return noteRepository.save(note).getId();
    }

    @Override
    public Note getNote(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<Note> getAllNotes(Pageable pageable) {
        return noteRepository.findAll(pageable);
    }

    @Override
    public void replaceNote(Long id, Note note) {
        if (!noteRepository.existsById(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        noteRepository.save(note);
    }

    @Override
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

}
