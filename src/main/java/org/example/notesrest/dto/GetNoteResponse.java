package org.example.notesrest.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record GetNoteResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {}
