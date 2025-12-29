package org.example.notesrest.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.notesrest.dto.CreateNoteRequest;
import org.example.notesrest.dto.GetAllNotesResponse;
import org.example.notesrest.dto.GetNoteResponse;
import org.example.notesrest.dto.PageResponse;
import org.example.notesrest.dto.ReplaceNoteRequest;
import org.example.notesrest.entity.Note;
import org.example.notesrest.repo.NoteRepository;
import org.example.notesrest.service.NoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;

    @Override
    public Long createNote(CreateNoteRequest request) {
        return noteRepository.save(Note.builder()
                .title(request.title())
                .content(request.content())
                .build()
        ).getId();
    }

    @Override
    public GetNoteResponse getNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

        return GetNoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    @Override
    public PageResponse<GetAllNotesResponse> getAllNotes(Pageable pageable) {
        var page = noteRepository.findAll(pageable)
                .map(note -> GetAllNotesResponse.builder()
                        .id(note.getId())
                        .title(note.getTitle())
                        .content(note.getContent())
                        .createdAt(note.getCreatedAt())
                        .updatedAt(note.getUpdatedAt())
                        .build());

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    public void replaceNote(Long id, ReplaceNoteRequest request) {
        if (!noteRepository.existsById(id)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        noteRepository.save(Note.builder()
                .id(id)
                .title(request.title())
                .content(request.content())
                .build());
    }

    @Override
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

}
