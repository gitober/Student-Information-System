package com.studentinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.services.GradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GradeController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService gradeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Grade grade1, grade2;

    @BeforeEach
    void setUp() {
        Course course1 = new Course();
        course1.setCourseId(200L);
        course1.setCourseName("Mathematics");
        course1.setDuration(90);

        Course course2 = new Course();
        course2.setCourseId(201L);
        course2.setCourseName("Physics");
        course2.setDuration(120);

        grade1 = new Grade();
        grade1.setGradeId(1);
        grade1.setGradeValue("A"); // Updated to setGradeValue
        grade1.setGradingDay(LocalDate.of(2024, 9, 30));
        grade1.setStudentNumber(100L);
        grade1.setCourse(course1);

        grade2 = new Grade();
        grade2.setGradeId(2);
        grade2.setGradeValue("B"); // Updated to setGradeValue
        grade2.setGradingDay(LocalDate.of(2024, 10, 1));
        grade2.setStudentNumber(101L);
        grade2.setCourse(course2);
    }

    @AfterEach
    void tearDown() {
        reset(gradeService);
    }

    @Test
    void testGetGradesByStudentNumber() throws Exception {
        List<Grade> grades = Collections.singletonList(grade1);
        given(gradeService.getGradesByStudentNumber(100L)).willReturn(grades);

        mockMvc.perform(get("/api/grades/student/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gradeId").value(1))
                .andExpect(jsonPath("$[0].gradeValue").value("A")) // Updated to gradeValue
                .andExpect(jsonPath("$[0].gradingDay").value("2024-09-30"))
                .andExpect(jsonPath("$[0].studentNumber").value(100L))
                .andExpect(jsonPath("$[0].course.courseId").value(200L));
    }

    @Test
    void testGetGradesByCourseId() throws Exception {
        List<Grade> grades = Collections.singletonList(grade2);
        given(gradeService.getGradesByCourseId(201L)).willReturn(grades);

        mockMvc.perform(get("/api/grades/course/201"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gradeId").value(2))
                .andExpect(jsonPath("$[0].gradeValue").value("B")) // Updated to gradeValue
                .andExpect(jsonPath("$[0].gradingDay").value("2024-10-01"))
                .andExpect(jsonPath("$[0].course.courseId").value(201L));
    }

    @Test
    void testCreateGrade() throws Exception {
        given(gradeService.saveGrade(ArgumentMatchers.any(Grade.class))).willReturn(grade1);

        String gradeJson = objectMapper.writeValueAsString(grade1);

        ResultActions response = mockMvc.perform(post("/api/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gradeJson));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.gradeId").value(1))
                .andExpect(jsonPath("$.gradeValue").value("A")) // Updated to gradeValue
                .andExpect(jsonPath("$.gradingDay").value("2024-09-30"))
                .andExpect(jsonPath("$.studentNumber").value(100L))
                .andExpect(jsonPath("$.course.courseId").value(200L));
    }

    @Test
    void testUpdateGrade() throws Exception {
        given(gradeService.updateGrade(ArgumentMatchers.eq(1), ArgumentMatchers.any(Grade.class))).willReturn(grade1);

        String gradeJson = objectMapper.writeValueAsString(grade1);

        mockMvc.perform(put("/api/grades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gradeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gradeId").value(1))
                .andExpect(jsonPath("$.gradeValue").value("A")) // Updated to gradeValue
                .andExpect(jsonPath("$.gradingDay").value("2024-09-30"))
                .andExpect(jsonPath("$.studentNumber").value(100L))
                .andExpect(jsonPath("$.course.courseId").value(200L));
    }

    @Test
    void testDeleteGrade() throws Exception {
        when(gradeService.deleteGrade(1)).thenReturn(true);

        mockMvc.perform(delete("/api/grades/1"))
                .andExpect(status().isNoContent());
    }
}
