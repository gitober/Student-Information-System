package com.studentinfo.services;

import com.studentinfo.data.entity.AttendanceRecord;
import com.studentinfo.data.repository.AttendanceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;

    public List<AttendanceRecord> getAllAttendance() {
        return attendanceRecordRepository.findAll();
    }

    public Optional<AttendanceRecord> getAttendanceById(Long attendanceId) {
        return attendanceRecordRepository.findById(attendanceId);
    }

    public AttendanceRecord createAttendance(AttendanceRecord attendanceRecord) {
        return attendanceRecordRepository.save(attendanceRecord);
    }

    public AttendanceRecord updateAttendance(Long attendanceId, AttendanceRecord attendanceRecord) {
        // Check if attendance record exists
        if (!attendanceRecordRepository.existsById(attendanceId)) {
            throw new RuntimeException("Attendance record not found");
        }
        attendanceRecord.setAttendanceId(attendanceId);
        return attendanceRecordRepository.save(attendanceRecord);
    }

    public void deleteAttendance(Long attendanceId) {
        attendanceRecordRepository.deleteById(attendanceId);
    }
}
