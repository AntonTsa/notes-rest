package org.example.notesrest.mapper;

import org.example.notesrest.dto.NoteRequest;
import org.example.notesrest.dto.NoteResponse;
import org.example.notesrest.dto.PageResponse;
import org.example.notesrest.entity.Note;
import org.springframework.data.domain.Page;

public interface NoteMapper {
    Note toNote(NoteRequest request);

    NoteResponse toNoteResponse(Note note);

    PageResponse<NoteResponse> toPageResponse(Page<Note> notePage);
}
