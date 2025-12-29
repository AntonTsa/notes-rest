package org.example.notesrest.mapper.impl;

import org.example.notesrest.dto.NoteRequest;
import org.example.notesrest.dto.NoteResponse;
import org.example.notesrest.dto.PageResponse;
import org.example.notesrest.entity.Note;
import org.example.notesrest.mapper.NoteMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class NoteMapperImpl implements NoteMapper {

    @Override
    public Note toNote(NoteRequest request) {
        if (request == null) {
            return null;
        }

        return Note.builder()
                .title(request.title())
                .content(request.content())
                .build();
    }

    @Override
    public NoteResponse toNoteResponse(Note note) {
        if (note == null) {
            return null;
        }

        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    @Override
    public PageResponse<NoteResponse> toPageResponse(Page<Note> notePage) {
        if (notePage == null) {
            return null;
        }

        return new PageResponse<>(
                notePage.map(this::toNoteResponse).getContent(),
                notePage.getNumber(),
                notePage.getSize(),
                notePage.getTotalElements(),
                notePage.getTotalPages()
        );
    }
}
