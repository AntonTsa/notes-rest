package org.example.notesrest.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record RestContractExceptionResponse(
        LocalDateTime timestamp,
        String error,
        String message
) implements Serializable {}
