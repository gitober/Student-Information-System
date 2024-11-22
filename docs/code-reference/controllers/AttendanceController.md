# AttendanceController Documentation

## Purpose

This class handles HTTP requests for managing attendance records, providing RESTful endpoints for CRUD operations.

## Features

- Retrieve all attendance records or a specific record by ID.
- Create new attendance records.
- Update existing attendance records by ID.
- Delete attendance records by ID.

## Key Methods

- **getAllAttendance()**: Fetches a list of all attendance records.
- **getAttendanceById(Long attendanceId)**: Retrieves a specific attendance record using its ID.
- **createAttendance(Attendance attendance)**: Creates a new attendance record.
- **updateAttendance(Long attendanceId, Attendance attendance)**: Updates an existing attendance record by its ID.
- **deleteAttendance(Long attendanceId)**: Deletes an attendance record by its ID.

## Notes

### HTTP Status Codes:

- **200 OK**: Success.
- **201 Created**: New record created.
- **204 No Content**: Record deleted.
- **404 Not Found**: Record not found.

### Service Dependency

- Utilizes **AttendanceService** to handle business logic.

## Example Endpoints

- **Get all records**: `GET /api/attendance`
- **Get record by ID**: `GET /api/attendance/{attendanceId}`

---

[Back to System Overview](../system-overview.md)
