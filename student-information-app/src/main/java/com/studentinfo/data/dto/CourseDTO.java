package com.studentinfo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long courseId;
    private String courseName;
    private String coursePlan;
    private int duration;

    // Additional fields for displaying formatted dates
    private LocalDate startDate;
    private LocalDate endDate;

    // Method to get the formatted date range as a string
    public String getFormattedDateRange(String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }
}
