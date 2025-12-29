package org.example.notesrest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = NotesRestApplication.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class NotesRestApplicationTests {

    @Autowired
    private NotesRestApplication application;

    @Test
    @DisplayName("""
            GIVEN application
            WHEN spring context starts
            THEN verify that application has started
            """)
    void applicationContextLoads() {
        // GIVEN

        // WHEN

        // THEN
        assertThat(application).isNotNull();
    }

}
