package org.example.notesrest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.notesrest.controller.NoteControllerFixture.CREATE_NOTE_REQUEST;
import static org.example.notesrest.controller.NoteControllerFixture.CREATE_NOTE_REQUEST_INVALID;
import static org.example.notesrest.controller.NoteControllerFixture.CREATE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE;
import static org.example.notesrest.controller.NoteControllerFixture.DELETE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE;
import static org.example.notesrest.controller.NoteControllerFixture.EXPECTED_CREATED_URL;
import static org.example.notesrest.controller.NoteControllerFixture.GET_ALL_NOTES_EMPTY_RESPONSE_PAGEABLE;
import static org.example.notesrest.controller.NoteControllerFixture.GET_ALL_NOTES_RESPONSE;
import static org.example.notesrest.controller.NoteControllerFixture.GET_ALL_NOTES_RESPONSE_PAGEABLE;
import static org.example.notesrest.controller.NoteControllerFixture.GET_NOTE_RESPONSE_VALID;
import static org.example.notesrest.controller.NoteControllerFixture.NOTES_URL_VALID;
import static org.example.notesrest.controller.NoteControllerFixture.NOTE_ID_INVALID;
import static org.example.notesrest.controller.NoteControllerFixture.NOTE_ID_MALFORMED;
import static org.example.notesrest.controller.NoteControllerFixture.NOTE_ID_VALID;
import static org.example.notesrest.controller.NoteControllerFixture.NOTE_URL_VALID;
import static org.example.notesrest.controller.NoteControllerFixture.NOT_FOUND_EXCEPTION_MESSAGE;
import static org.example.notesrest.controller.NoteControllerFixture.PAGE_REQUEST;
import static org.example.notesrest.controller.NoteControllerFixture.REPLACE_NOTE_REQUEST;
import static org.example.notesrest.controller.NoteControllerFixture.REPLACE_NOTE_REQUEST_INVALID;
import static org.example.notesrest.controller.NoteControllerFixture.REPLACE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE;
import static org.example.notesrest.controller.NoteControllerFixture.STORAGE_EXCEPTION_MESSAGE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Objects;
import lombok.SneakyThrows;
import org.example.notesrest.dto.GetAllNotesResponse;
import org.example.notesrest.dto.GetNoteResponse;
import org.example.notesrest.dto.PageResponse;
import org.example.notesrest.dto.RestContractExceptionResponse;
import org.example.notesrest.service.NoteService;
import org.hibernate.exception.JDBCConnectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = NoteController.class)
@AutoConfigureMockMvc
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean(answers = Answers.RETURNS_SMART_NULLS)
    private NoteService noteService;

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Test
    @DisplayName("""
            GIVEN valid createNoteRequest object
            WHEN performing POST request
            THEN return response with code 201, valid location and empty body
            """)
    void createNoteValid() throws Exception {
        // GIVEN
        given(noteService.createNote(CREATE_NOTE_REQUEST)).willReturn(NOTE_ID_VALID);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(post(NOTES_URL_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(CREATE_NOTE_REQUEST))
                )
                // THEN
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getHeader(HttpHeaders.LOCATION))
                .isNotEmpty()
                .isEqualTo(EXPECTED_CREATED_URL);
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN invalid createNoteRequest object
            WHEN performing POST request
            THEN return response with code 400 and message "Bad Request"
            """)
    void createNoteBadRequest() throws Exception {
        // GIVEN

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(post(NOTES_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(CREATE_NOTE_REQUEST_INVALID)
                        )
                        // THEN
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(CREATE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size
            WHEN performing GET request
            THEN return response with code 200 and list of notes
            """)
    void getAllNotesValid() throws Exception {
        // GIVEN
        given(noteService.getAllNotes(PageRequest.of(0, 3, Sort.Direction.ASC, "id")))
                .willReturn(GET_ALL_NOTES_RESPONSE_PAGEABLE);

        // WHEN
        PageResponse<GetAllNotesResponse> actualResponse = fromJson(mockMvc
                        .perform(get(NOTES_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {
                }
        );

        // AND THEN
        assertThat(actualResponse.totalElements()).isEqualTo(2L);
        assertThat(actualResponse.totalPages()).isEqualTo(1);
        assertThat(actualResponse.content()).isEqualTo(GET_ALL_NOTES_RESPONSE);
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size
            WHEN performing GET request
            THEN return response with code 200 and empty list of notes
            """)
    void getAllNotesEmptyValid() throws Exception {
        // GIVEN
        given(noteService.getAllNotes(PAGE_REQUEST))
                .willReturn(GET_ALL_NOTES_EMPTY_RESPONSE_PAGEABLE);

        // WHEN
        PageResponse<GetAllNotesResponse> actualResponse = fromJson(mockMvc
                        .perform(get(NOTES_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                new TypeReference<>() {
                }
        );

        // AND THEN
        assertThat(actualResponse.totalElements()).isZero();
        assertThat(actualResponse.totalPages()).isZero();
        assertThat(actualResponse.content()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN default page number and page size
            WHEN performing GET request and Storage returns exception
            THEN return response with code 500 and error message
            """)
    void getAllNotesInternalServiceError() throws Exception {
        // GIVEN
        given(noteService.getAllNotes(PAGE_REQUEST))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, null));

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(get(NOTES_URL_VALID)
                                .accept(MediaType.APPLICATION_JSON))
                        // THEN
                        .andExpect(status().isInternalServerError())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid note id
            WHEN performing GET request
            THEN return response with code 200 and note entry
            """)
    void getOneNoteByIdValid() throws Exception {
        // GIVEN
        given(noteService.getNote(NOTE_ID_VALID)).willReturn(GET_NOTE_RESPONSE_VALID);

        // WHEN
        GetNoteResponse actualResponse = fromJson(mockMvc
                        .perform(get(NOTE_URL_VALID, NOTE_ID_VALID)
                                .accept(MediaType.APPLICATION_JSON))

                        // THEN
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                GetNoteResponse.class);

        // AND THEN
        assertThat(actualResponse).isEqualTo(GET_NOTE_RESPONSE_VALID);
    }

    @Test
    @DisplayName("""
            GIVEN invalid note id
            WHEN performing GET request
            THEN return response with code 404 and no note entry
            """)
    void getOneNoteByIdInValid() throws Exception {
        // GIVEN
        given(noteService.getNote(NOTE_ID_INVALID))
                .willThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(get(NOTE_URL_VALID, NOTE_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON))

                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN invalid note id
            WHEN performing GET request
            THEN return response with code 404 and no note entry
            """)
    void getOneNoteByIdInternalServiceError() throws Exception {
        // GIVEN
        given(noteService.getNote(NOTE_ID_VALID))
                .willThrow(new JDBCConnectionException(STORAGE_EXCEPTION_MESSAGE, null));

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(get(NOTE_URL_VALID, NOTE_ID_VALID)
                                .accept(MediaType.APPLICATION_JSON))

                        // THEN
                        .andExpect(status().isInternalServerError())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(STORAGE_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid note id and valid note object
            WHEN performing PUT request
            THEN return response with code 200 and no note entry
            """)
    void replaceNoteByValidId() throws Exception {
        // GIVEN
        doNothing().when(noteService).replaceNote(NOTE_ID_VALID, REPLACE_NOTE_REQUEST);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(put(NOTE_URL_VALID, NOTE_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(REPLACE_NOTE_REQUEST))
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isEmpty();
    }

    @Test
    @DisplayName("""
            GIVEN invalid note id and valid note object
            WHEN performing PUT request
            THEN return response with code 404 and no note entry
            """)
    void replaceNoteByInvalidId() throws Exception {
        // GIVEN
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
                .when(noteService).replaceNote(NOTE_ID_INVALID, REPLACE_NOTE_REQUEST);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(put(NOTE_URL_VALID, NOTE_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(REPLACE_NOTE_REQUEST))
                        )
                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid note id and invalid note object
            WHEN performing PUT request
            THEN return response with code 400 and message "Bad Request"
            """)
    void replaceNoteByValidIdAndInvalidReplaceNoteRequest() throws Exception {
        // GIVEN
        doNothing().when(noteService).replaceNote(NOTE_ID_VALID, REPLACE_NOTE_REQUEST_INVALID);

        // WHEN
        RestContractExceptionResponse actualResponse =
                fromJson(mockMvc
                                .perform(put(NOTE_URL_VALID, NOTE_ID_VALID)
                                        .accept(MediaType.APPLICATION_JSON)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(toJson(REPLACE_NOTE_REQUEST_INVALID))
                                )
                                .andExpect(status().isBadRequest())
                                .andReturn()
                                .getResponse()
                                .getContentAsString(),
                        RestContractExceptionResponse.class);
        //AND THEN
        assertThat(actualResponse.message()).isEqualTo(REPLACE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid note id
            WHEN performing DELETE request
            THEN return response with code 204 and delete entry
            """)
    void deleteNoteByValidId() throws Exception {
        // GIVEN
        doNothing().when(noteService).deleteNote(NOTE_ID_VALID);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(delete(NOTE_URL_VALID, NOTE_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isBlank();
    }

    @Test
    @DisplayName("""
            GIVEN malformed note id
            WHEN performing DELETE request
            THEN return response with code 400 and delete entry
            """)
    void deleteNoteByMalformedId() throws Exception {
        // GIVEN

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(delete(NOTE_URL_VALID, NOTE_ID_MALFORMED)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                        )
                        // THEN
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(DELETE_NOTE_RESPONSE_BAD_REQUEST_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid note id
            WHEN performing DELETE request
            THEN return response with code 204 and delete entry
            """)
    void deleteNoteByInvalidId() throws Exception {
        // GIVEN
        doThrow(new EmptyResultDataAccessException(0))
                .when(noteService).deleteNote(NOTE_ID_INVALID);

        // WHEN
        RestContractExceptionResponse actualResponse = fromJson(mockMvc
                        .perform(delete(NOTE_URL_VALID, NOTE_ID_INVALID)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(REPLACE_NOTE_REQUEST))
                        )
                        // THEN
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                RestContractExceptionResponse.class);

        // AND THEN
        assertThat(actualResponse.message()).isEqualTo(NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("""
            GIVEN valid removal all request
            WHEN performing DELETE all request
            THEN return response with code 204 and delete all entries
            """)
    void deleteAllNotesValid() throws Exception {
        // GIVEN
        doNothing().when(noteService).deleteNote(NOTE_ID_VALID);

        // WHEN
        MockHttpServletResponse actualResponse = mockMvc
                .perform(delete(NOTE_URL_VALID, NOTE_ID_VALID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                // THEN
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse();

        // AND THEN
        assertThat(actualResponse.getContentAsString()).isBlank();
    }

    @SneakyThrows(JsonProcessingException.class)
    private String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T fromJson(String string, Class<T> type) {
        return Objects.nonNull(string) ? objectMapper.readValue(string, type) : null;
    }

    @SneakyThrows(JsonProcessingException.class)
    public <T> T fromJson(String string, TypeReference<T> type) {
        return Objects.nonNull(string) ? objectMapper.readValue(string, type) : null;
    }

}
