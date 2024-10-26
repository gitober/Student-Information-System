package com.studentinfo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {
    private Integer gradeId;
    private String grade;
    private LocalDate gradingDay;
    private Integer studentNumber;
    private Integer courseId;
}
