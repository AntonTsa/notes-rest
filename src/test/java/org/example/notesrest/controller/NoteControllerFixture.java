package org.example.notesrest.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.example.notesrest.dto.NoteRequest;
import org.example.notesrest.dto.NoteResponse;
import org.example.notesrest.dto.PageResponse;
import org.example.notesrest.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class NoteControllerFixture {
    static final Long NOTE_ID_VALID = 1024L;
    static final Long NOTE_ID_INVALID = 42L;
    static final String NOTE_ID_MALFORMED = "MALFORMED_ID";
    static final String STORAGE_EXCEPTION_MESSAGE = "Connection Error";
    static final String NOT_FOUND_EXCEPTION_MESSAGE = "NOT_FOUND";
    static final LocalDateTime CREATED_AT_DATE_TIME = LocalDateTime.parse("2025-12-02T10:15:30");
    static final LocalDateTime UPDATED_AT_DATE_TIME = LocalDateTime.parse("2025-12-02T10:15:30");
    static final PageRequest PAGE_REQUEST = PageRequest.of(0, 3, Sort.Direction.ASC, "id");
    static final Note NOTE_VALID = Note.builder()
            .title("New Note")
            .content("New Content")
            .build();
    static final NoteRequest NOTE_REQUEST_VALID = NoteRequest.builder()
            .title("New Note")
            .content("New Content")
            .build();

    static final Note NOTE_INVALID = Note.builder()
            .build();

    static final NoteRequest NOTE_REQUEST_INVALID = NoteRequest.builder()
            .build();

    static final NoteResponse NOTE_RESPONSE_VALID = NoteResponse.builder()
            .id(23L)
            .title("Second Note")
            .content("Second Content")
            .createdAt(CREATED_AT_DATE_TIME)
            .updatedAt(UPDATED_AT_DATE_TIME)
            .build();

    static final NoteResponse FIRST_NOTE_RESPONSE = NoteResponse.builder()
            .id(1L)
            .title("First Note")
            .content("First Content")
            .createdAt(CREATED_AT_DATE_TIME)
            .updatedAt(UPDATED_AT_DATE_TIME)
            .build();

    static final NoteResponse SECOND_NOTE_RESPONSE = NoteResponse.builder()
            .id(2L)
            .title("Second Note")
            .content("Second Content")
            .createdAt(CREATED_AT_DATE_TIME)
            .updatedAt(UPDATED_AT_DATE_TIME)
            .build();

    static final List<NoteResponse> LIST_NOTE_RESPONSES = List.of(
            FIRST_NOTE_RESPONSE,
            SECOND_NOTE_RESPONSE);

    static final Note FIRST_NOTE = Note.builder()
            .id(1L)
            .title("First Note")
            .content("First Content")
            .createdAt(CREATED_AT_DATE_TIME)
            .updatedAt(UPDATED_AT_DATE_TIME)
            .build();

    static final Note SECOND_NOTE = Note.builder()
            .id(2L)
            .title("Second Note")
            .content("Second Content")
            .createdAt(CREATED_AT_DATE_TIME)
            .updatedAt(UPDATED_AT_DATE_TIME)
            .build();

    static final List<Note> LIST_NOTES = List.of(
            FIRST_NOTE,
            SECOND_NOTE);

    static final Page<Note> ALL_NOTES_PAGEABLE =
            new PageImpl<>(
                    LIST_NOTES,
                    PAGE_REQUEST,
                    LIST_NOTES.size());

    static final Page<Note> ALL_NOTES_EMPTY_PAGEABLE =
            new PageImpl<>(
                    List.of(),
                    PAGE_REQUEST,
                    0);
    static final PageResponse<NoteResponse> ALL_NOTES_RESPONSES_EMPTY_PAGE_RESPONSE =
            new PageResponse<>(
                    List.of(),
                    0,
                    0,
                    0,
                    0);
    static final PageResponse<NoteResponse> PAGE_RESPONSE_NOTE_RESPONSE =
            new PageResponse<>(
                    LIST_NOTE_RESPONSES,
                    PAGE_REQUEST.getPageNumber(),
                    PAGE_REQUEST.getPageSize(),
                    LIST_NOTE_RESPONSES.size(),
                    1);
    static final String BAD_REQUEST_MESSAGE =
            "content: must not be blank, title: must not be blank";
    static final String DELETE_BAD_REQUEST_MESSAGE =
            "id: provided wrong type, expected type is Long";
    private static final String API_V1 = "/api";
    static final String NOTES_URL_VALID = API_V1 + "/notes";
    static final String NOTE_URL_VALID = NOTES_URL_VALID + "/{id}";
    private static final String URL_PATH_SEPARATOR = "/";
    static final String EXPECTED_CREATED_URL = NOTES_URL_VALID + URL_PATH_SEPARATOR + NOTE_ID_VALID;

}
