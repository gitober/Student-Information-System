package com.studentinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentinfo.data.entity.Attendance;
import com.studentinfo.services.AttendanceService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttendanceController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
public class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendanceService attendanceService;

    @Autowired
    private ObjectMapper objectMapper;

    private Attendance attendance1, attendance2;

    @BeforeEach
    void setUp() {
        attendance1 = new Attendance();
        attendance1.setAttendanceId(1L);
        attendance1.setAttendanceStatus("Present");

        attendance2 = new Attendance();
        attendance2.setAttendanceId(2L);
        attendance2.setAttendanceStatus("Absent");
    }

    @AfterEach
    void tearDown() {
        reset(attendanceService);
    }

    @Test
    public void testGetAllAttendance() throws Exception {
        given(attendanceService.getAllAttendance()).willReturn(Arrays.asList(attendance1, attendance2));

        mockMvc.perform(get("/api/attendance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].attendanceId").value(1L))
                .andExpect(jsonPath("$[0].attendanceStatus").value("Present"))
                .andExpect(jsonPath("$[1].attendanceId").value(2L))
                .andExpect(jsonPath("$[1].attendanceStatus").value("Absent"));
    }

    @Test
    public void testGetAttendanceById() throws Exception {
        given(attendanceService.getAttendanceById(1L)).willReturn(Optional.of(attendance1));

        mockMvc.perform(get("/api/attendance/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendanceId").value(1L))
                .andExpect(jsonPath("$.attendanceStatus").value("Present"));
    }

    @Test
    public void testCreateAttendance() throws Exception {
        given(attendanceService.createAttendance(ArgumentMatchers.any(Attendance.class))).willReturn(attendance1);

        String attendanceJson = objectMapper.writeValueAsString(attendance1);

        ResultActions response = mockMvc.perform(post("/api/attendance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(attendanceJson));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.attendanceId").value(1L))
                .andExpect(jsonPath("$.attendanceStatus").value("Present"));
    }

    @Test
    public void testUpdateAttendance() throws Exception {
        // Mock finding the attendance by ID to ensure it exists
        given(attendanceService.getAttendanceById(1L)).willReturn(Optional.of(attendance1));

        // Mock the update method to return the updated attendance
        given(attendanceService.updateAttendance(ArgumentMatchers.eq(1L), ArgumentMatchers.any(Attendance.class))).willReturn(attendance1);

        String attendanceJson = objectMapper.writeValueAsString(attendance1);

        mockMvc.perform(put("/api/attendance/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(attendanceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.attendanceId").value(1L))
                .andExpect(jsonPath("$.attendanceStatus").value("Present"));
    }

    @Test
    public void testDeleteAttendance() throws Exception {
        // Mock the service method to return true for successful deletion
        given(attendanceService.deleteAttendance(1L)).willReturn(true);

        mockMvc.perform(delete("/api/attendance/1"))
                .andExpect(status().isNoContent());
    }
}
