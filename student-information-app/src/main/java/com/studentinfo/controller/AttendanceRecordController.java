package com.studentinfo.controller;

import com.studentinfo.data.entity.AttendanceRecord;
import com.studentinfo.services.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRecordController {

    @Autowired
    private AttendanceService attendanceRecordService;

    @GetMapping
    public ResponseEntity<List<AttendanceRecord>> getAllAttendance() {
        List<AttendanceRecord> attendanceList = attendanceRecordService.getAllAttendance();
        return ResponseEntity.ok(attendanceList);
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<AttendanceRecord> getAttendanceById(@PathVariable Long attendanceId) {
        return attendanceRecordService.getAttendanceById(attendanceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AttendanceRecord> createAttendance(@RequestBody AttendanceRecord attendanceRecord) {
        AttendanceRecord createdAttendance = attendanceRecordService.createAttendance(attendanceRecord);
        return ResponseEntity.status(201).body(createdAttendance);
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<AttendanceRecord> updateAttendance(@PathVariable Long attendanceId, @RequestBody AttendanceRecord attendanceRecord) {
        AttendanceRecord updatedAttendance = attendanceRecordService.updateAttendance(attendanceId, attendanceRecord);
        return ResponseEntity.ok(updatedAttendance);
    }

    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long attendanceId) {
        attendanceRecordService.deleteAttendance(attendanceId);
        return ResponseEntity.noContent().build();
    }
}
