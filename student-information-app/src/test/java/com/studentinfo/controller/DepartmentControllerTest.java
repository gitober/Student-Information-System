package com.studentinfo.controller;

import com.studentinfo.data.entity.DepartmentTranslation;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(DepartmentController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security for testing
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @BeforeEach
    void setUp() {
        // Initializing mock data or setting default behaviors
        when(translationService.getDepartmentTranslationsByLocale("EN"))
                .thenReturn(Arrays.asList(
                        new DepartmentTranslation(1L, "EN", "IT Department", "Description1"),
                        new DepartmentTranslation(2L, "EN", "HR Department", "Description2")
                ));
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test to ensure no interaction carries over
        reset(translationService);
    }

    @Test
    void testGetDepartmentTranslations() throws Exception {
        // Given
        DepartmentTranslation translation1 = new DepartmentTranslation();
        translation1.setId(1L);
        translation1.setLocale("EN");
        translation1.setFieldName("IT Department");
        translation1.setTranslatedValue("Description1");

        DepartmentTranslation translation2 = new DepartmentTranslation();
        translation2.setId(2L);
        translation2.setLocale("EN");
        translation2.setFieldName("HR Department");
        translation2.setTranslatedValue("Description2");

        List<DepartmentTranslation> translations = Arrays.asList(translation1, translation2);

        String locale = "EN";

        // When
        when(translationService.getDepartmentTranslationsByLocale(locale)).thenReturn(translations);

        // Then
        mockMvc.perform(get("/api/departments/translations").param("locale", locale))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].locale").value("EN"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].locale").value("EN"));
    }
}
