package com.studentinfo.services;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.repository.AttendanceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Reset the mock repository after each test
        reset(attendanceRepository);
    }

    @Test
    void testGetAllAttendance() {
        // Arrange
        Attendance attendance1 = new Attendance();
        Attendance attendance2 = new Attendance();
        when(attendanceRepository.findAll()).thenReturn(Arrays.asList(attendance1, attendance2));

        // Act
        List<Attendance> attendanceList = attendanceService.getAllAttendance();

        // Assert
        assertEquals(2, attendanceList.size());
        verify(attendanceRepository, times(1)).findAll();
    }

    @Test
    void testGetAttendanceById() {
        // Arrange
        Attendance attendance = new Attendance();
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));

        // Act
        Optional<Attendance> result = attendanceService.getAttendanceById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(attendance, result.get());
        verify(attendanceRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateAttendance() {
        // Arrange
        Attendance attendance = new Attendance();
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        // Act
        Attendance createdAttendance = attendanceService.createAttendance(attendance);

        // Assert
        assertNotNull(createdAttendance);
        verify(attendanceRepository, times(1)).save(attendance);
    }

    @Test
    void testUpdateAttendance() {
        // Arrange
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(1L);
        when(attendanceRepository.existsById(1L)).thenReturn(true);
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        // Act
        Attendance updatedAttendance = attendanceService.updateAttendance(1L, attendance);

        // Assert
        assertNotNull(updatedAttendance);
        assertEquals(1L, updatedAttendance.getAttendanceId());
        verify(attendanceRepository, times(1)).existsById(1L);
        verify(attendanceRepository, times(1)).save(attendance);
    }

    @Test
    void testDeleteAttendance() {
        // Arrange
        when(attendanceRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = attendanceService.deleteAttendance(1L);

        // Assert
        assertTrue(result);
        verify(attendanceRepository, times(1)).existsById(1L);
        verify(attendanceRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAttendanceByCourseId() {
        // Arrange
        Attendance attendance1 = new Attendance();
        Attendance attendance2 = new Attendance();
        when(attendanceRepository.findByCourse_CourseId(1L)).thenReturn(Arrays.asList(attendance1, attendance2));

        // Act
        List<Attendance> attendanceList = attendanceService.getAttendanceByCourseId(1L);

        // Assert
        assertEquals(2, attendanceList.size());
        verify(attendanceRepository, times(1)).findByCourse_CourseId(1L);
    }
}
