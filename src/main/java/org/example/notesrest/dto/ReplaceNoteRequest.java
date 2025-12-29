package org.example.notesrest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record ReplaceNoteRequest(
        @NotBlank
        @Size(min = 3, max = 100)
        String title,
        @NotBlank
        @Size(min = 3, max = 1000)
        String content
) implements Serializable {}
