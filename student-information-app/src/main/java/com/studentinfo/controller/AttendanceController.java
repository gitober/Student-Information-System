package com.studentinfo.controller;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        List<Attendance> attendanceList = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendanceList); // Return 200 OK with list of attendance records
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long attendanceId) {
        return attendanceService.getAttendanceById(attendanceId)
                .map(ResponseEntity::ok) // Return 200 OK with found attendance record
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Return 404 Not Found if no attendance found
    }

    @PostMapping
    public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
        Attendance createdAttendance = attendanceService.createAttendance(attendance);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAttendance); // Return 201 Created with the created attendance record
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long attendanceId, @RequestBody Attendance attendance) {
        Attendance updatedAttendance = attendanceService.updateAttendance(attendanceId, attendance);
        if (updatedAttendance == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found if no attendance found for update
        }
        return ResponseEntity.ok(updatedAttendance); // Return 200 OK with the updated attendance record
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        boolean wasDeleted = attendanceService.deleteAttendance(attendanceId);
        if (!wasDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if attendance not found
        }
        return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
    }

}
