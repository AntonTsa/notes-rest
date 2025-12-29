package org.example.notesrest.service;

import org.example.notesrest.dto.CreateNoteRequest;
import org.example.notesrest.dto.GetAllNotesResponse;
import org.example.notesrest.dto.GetNoteResponse;
import org.example.notesrest.dto.PageResponse;
import org.example.notesrest.dto.ReplaceNoteRequest;
import org.springframework.data.domain.Pageable;

public interface NoteService {
    Long createNote(CreateNoteRequest request);

    GetNoteResponse getNote(Long id);

    PageResponse<GetAllNotesResponse> getAllNotes(Pageable pageable);

    void replaceNote(Long id, ReplaceNoteRequest request);

    void deleteNote(Long id);
}
