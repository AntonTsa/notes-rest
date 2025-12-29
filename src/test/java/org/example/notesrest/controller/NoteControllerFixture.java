package org.example.notesrest.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.example.notesrest.dto.CreateNoteRequest;
import org.example.notesrest.dto.GetAllNotesResponse;
import org.example.notesrest.dto.GetNoteResponse;
import org.example.notesrest.dto.PageResponse;
import org.example.notesrest.dto.ReplaceNoteRequest;
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
    static final CreateNoteRequest CREATE_NOTE_REQUEST = CreateNoteRequest.builder()
            .title("New Note")
            .content("New Content")
            .build();

    static final ReplaceNoteRequest REPLACE_NOTE_REQUEST = ReplaceNoteRequest.builder()
            .title("Updated Note")
            .content("Updated Content")
            .build();

    static final ReplaceNoteRequest REPLACE_NOTE_REQUEST_INVALID = ReplaceNoteRequest.builder()
            .build();

    static final GetAllNotesResponse FIRST_GET_NOTE_RESPONSE = GetAllNotesResponse.builder()
            .id(1L)
            .title("First Note")
            .content("First Content")
            .createdAt(CREATED_AT_DATE_TIME)
            .updatedAt(UPDATED_AT_DATE_TIME)
            .build();

    static final GetAllNotesResponse SECOND_GET_NOTE_RESPONSE = GetAllNotesResponse.builder()
            .id(2L)
            .title("Second Note")
            .content("Second Content")
            .createdAt(CREATED_AT_DATE_TIME)
            .updatedAt(UPDATED_AT_DATE_TIME)
            .build();

    static final GetNoteResponse GET_NOTE_RESPONSE_VALID = GetNoteResponse.builder()
            .id(23L)
            .title("Second Note")
            .content("Second Content")
            .createdAt(CREATED_AT_DATE_TIME)
            .updatedAt(UPDATED_AT_DATE_TIME)
            .build();

    static final List<GetAllNotesResponse> GET_ALL_NOTES_RESPONSE = List.of(
            FIRST_GET_NOTE_RESPONSE,
            SECOND_GET_NOTE_RESPONSE);

    static final PageResponse<GetAllNotesResponse> GET_ALL_NOTES_RESPONSE_PAGEABLE =
            new PageResponse<>(
                    GET_ALL_NOTES_RESPONSE,
                    PAGE_REQUEST.getPageNumber(),
                    PAGE_REQUEST.getPageSize(),
                    GET_ALL_NOTES_RESPONSE.size(),
                    1);

    static final PageResponse<GetAllNotesResponse> GET_ALL_NOTES_EMPTY_RESPONSE_PAGEABLE =
            new PageResponse<>(
                    List.of(),
                    PAGE_REQUEST.getPageNumber(),
                    PAGE_REQUEST.getPageSize(),
                    0,
                    0);

    static final String CREATE_NOTE_REQUEST_INVALID = "{\"test\":\"test\"}";
    static final String CREATE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE =
            "content: must not be blank, title: must not be blank";
    static final String REPLACE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE =
            "content: must not be blank, title: must not be blank";
    static final String DELETE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE =
            "id: provided wrong type, expected type is Long";
    private static final String API_V1 = "/api";
    static final String NOTES_URL_VALID = API_V1 + "/notes";
    static final String NOTE_URL_VALID = NOTES_URL_VALID + "/{id}";
    private static final String URL_PATH_SEPARATOR = "/";
    static final String EXPECTED_CREATED_URL = NOTES_URL_VALID + URL_PATH_SEPARATOR + NOTE_ID_VALID;

}
