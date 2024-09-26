package com.studentinfo.data.dto;

import java.time.LocalDate;

public class AttendanceDTO {

    private Long attendanceId;
    private String attendanceStatus;
    private LocalDate attendanceDate;
    private Integer studentNumber;
    private Long courseId;

    // Default constructor
    public AttendanceDTO() {}

    // Constructor with fields
    public AttendanceDTO(Long attendanceId, String attendanceStatus, LocalDate attendanceDate, Integer studentNumber, Long courseId) {
        this.attendanceId = attendanceId;
        this.attendanceStatus = attendanceStatus;
        this.attendanceDate = attendanceDate;
        this.studentNumber = studentNumber;
        this.courseId = courseId;
    }

    // Getters and Setters
    public Long getAttendanceId() {
        return attendanceId;
    }
    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }
    public String getAttendanceStatus() {
        return attendanceStatus;
    }
    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }
    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }
    public Integer getStudentNumber() {
        return studentNumber;
    }
    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
