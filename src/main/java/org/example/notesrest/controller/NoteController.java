package org.example.notesrest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.example.notesrest.dto.NoteRequest;
import org.example.notesrest.dto.NoteResponse;
import org.example.notesrest.dto.PageResponse;
import org.example.notesrest.entity.Note;
import org.example.notesrest.mapper.NoteMapper;
import org.example.notesrest.service.NoteService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;
    private final NoteMapper mapper;

    @PostMapping
    public ResponseEntity<URI> create(
            @Valid @RequestBody NoteRequest noteRequest,
            HttpServletRequest httpServletRequest
    ) {
        Note note = mapper.toNote(noteRequest);
        return ResponseEntity.created(
                URI.create(
                        httpServletRequest.getRequestURI()
                                + "/"
                                + noteService.createNote(note)
                )
        ).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponse> getNote(@PathVariable @Positive long id) {
        return ResponseEntity.ok(mapper.toNoteResponse(noteService.getNote(id)));
    }

    @GetMapping
    public ResponseEntity<PageResponse<NoteResponse>> getAllNotes(
            @PageableDefault(size = 3)
            @SortDefault.SortDefaults(
                    @SortDefault(sort = "id", direction = Sort.Direction.ASC)
            )Pageable pageable
    ) {

        return ResponseEntity.ok(mapper.toPageResponse(noteService.getAllNotes(pageable)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNote(
            @PathVariable @Positive long id,
            @Valid @RequestBody NoteRequest noteRequest
    ) {
        noteService.replaceNote(id, mapper.toNote(noteRequest));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable @Positive Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
