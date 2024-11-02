package com.studentinfo.controller;

import com.studentinfo.data.entity.SubjectTranslation;
import com.studentinfo.services.TranslationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubjectController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @BeforeEach
    void setUp() {
        // Any necessary setup before each test can go here
    }

    @AfterEach
    void tearDown() {
        // Reset any mock objects or clear context after each test
        reset(translationService);
    }

    @Test
    public void testGetSubjectTranslations_Found() throws Exception {
        // Given
        Long subjectId = 1L;
        String locale = "EN";

        // Using the existing constructor for SubjectTranslation
        SubjectTranslation translation1 = new SubjectTranslation(locale, "Field1", "Value1");
        translation1.setId(1L);

        SubjectTranslation translation2 = new SubjectTranslation(locale, "Field2", "Value2");
        translation2.setId(2L);

        List<SubjectTranslation> translations = Arrays.asList(translation1, translation2);

        when(translationService.getSubjectTranslations(subjectId, locale)).thenReturn(translations);

        // When & Then
        mockMvc.perform(get("/api/subjects/{id}/translations", subjectId).param("locale", locale))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].locale").value(locale))
                .andExpect(jsonPath("$[0].fieldName").value("Field1"))
                .andExpect(jsonPath("$[0].translatedValue").value("Value1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].locale").value(locale))
                .andExpect(jsonPath("$[1].fieldName").value("Field2"))
                .andExpect(jsonPath("$[1].translatedValue").value("Value2"));
    }



    @Test
    public void testGetSubjectTranslations_NotFound() throws Exception {
        // Given
        Long subjectId = 2L;
        String locale = "EN";
        when(translationService.getSubjectTranslations(subjectId, locale)).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/subjects/{id}/translations", subjectId).param("locale", locale))
                .andExpect(status().isNotFound());
    }
}
