-- Create the 'users' table only if it does not exist
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT NOT NULL AUTO_INCREMENT,
                                     birthday DATE DEFAULT NULL,
                                     first_name VARCHAR(255) DEFAULT NULL,
                                     hashed_password VARCHAR(60) DEFAULT NULL,
                                     last_name VARCHAR(255) DEFAULT NULL,
                                     name VARCHAR(255) DEFAULT NULL,
                                     phone_number VARCHAR(255) DEFAULT NULL,
                                     username VARCHAR(255) UNIQUE NOT NULL,  -- Ensuring username is unique and not nullable
                                     user_type VARCHAR(31) NOT NULL,
                                     grade VARCHAR(255) DEFAULT NULL,
                                     student_class VARCHAR(255) DEFAULT NULL,
                                     department_id BIGINT DEFAULT NULL,
                                     subject_id BIGINT DEFAULT NULL,
                                     email VARCHAR(255) UNIQUE NOT NULL,  -- Added email field with unique and not-null constraint
                                     PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'department' table only if it does not exist
CREATE TABLE IF NOT EXISTS department (
                                          id BIGINT NOT NULL AUTO_INCREMENT,
                                          name VARCHAR(255) NOT NULL UNIQUE,
                                          PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'subject' table only if it does not exist
CREATE TABLE IF NOT EXISTS subject (
                                       id BIGINT NOT NULL AUTO_INCREMENT,
                                       name VARCHAR(255) NOT NULL UNIQUE,
                                       department_id BIGINT NOT NULL,
                                       PRIMARY KEY (id),
                                       FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'student' table only if it does not exist
CREATE TABLE IF NOT EXISTS student (
                                       Student_number INT NOT NULL AUTO_INCREMENT,
                                       Firstname VARCHAR(255) NOT NULL,
                                       Lastname VARCHAR(255) NOT NULL,
                                       Email VARCHAR(255) NOT NULL,
                                       Birthday DATE NOT NULL,
                                       Address VARCHAR(255) NOT NULL,
                                       Phone_number VARCHAR(20) NOT NULL,
                                       UserID BIGINT DEFAULT NULL,
                                       grade VARCHAR(255) DEFAULT NULL,
                                       student_class VARCHAR(255) DEFAULT NULL,
                                       PRIMARY KEY (Student_number),
                                       KEY FK_student_users (UserID),
                                       CONSTRAINT FK_student_users FOREIGN KEY (UserID) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'teacher' table only if it does not exist
CREATE TABLE IF NOT EXISTS teacher (
                                       TeacherID INT NOT NULL AUTO_INCREMENT,
                                       Student_name VARCHAR(255) NOT NULL,
                                       Address VARCHAR(255) NOT NULL,
                                       Phone_number VARCHAR(20) NOT NULL,
                                       UserID BIGINT DEFAULT NULL,
                                       department_id BIGINT DEFAULT NULL,
                                       subject_id BIGINT DEFAULT NULL,
                                       PRIMARY KEY (TeacherID),
                                       KEY FK_teacher_users (UserID),
                                       CONSTRAINT FK_teacher_users FOREIGN KEY (UserID) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE,
                                       FOREIGN KEY (department_id) REFERENCES department(id) ON DELETE SET NULL ON UPDATE CASCADE,
                                       FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'user_roles' table only if it does not exist
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          roles ENUM('STUDENT','TEACHER','USER') NOT NULL,  -- Corrected roles to match enum structure
                                          PRIMARY KEY (user_id, roles),
                                          CONSTRAINT FK_user_roles FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'course' table only if it does not exist
CREATE TABLE IF NOT EXISTS course (
                                      CourseID INT NOT NULL AUTO_INCREMENT,
                                      Course_plan VARCHAR(255) NOT NULL,
                                      Course_name VARCHAR(255) NOT NULL,
                                      Duration INT NOT NULL,
                                      PRIMARY KEY (CourseID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'grade' table only if it does not exist
CREATE TABLE IF NOT EXISTS grade (
                                     GradeID INT NOT NULL AUTO_INCREMENT,
                                     Grade VARCHAR(2) NOT NULL,
                                     Grading_day DATE NOT NULL,
                                     Student_number INT NOT NULL,
                                     CourseID INT NOT NULL,
                                     PRIMARY KEY (GradeID),
                                     FOREIGN KEY (Student_number) REFERENCES student(Student_number) ON DELETE CASCADE ON UPDATE CASCADE,
                                     FOREIGN KEY (CourseID) REFERENCES course(CourseID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'attendance' table only if it does not exist
CREATE TABLE IF NOT EXISTS attendance (
                                          AttendanceID INT NOT NULL AUTO_INCREMENT,
                                          Attendance_status ENUM('PRESENT', 'ABSENT') NOT NULL,
                                          Attendance_date DATE NOT NULL,
                                          Student_number INT NOT NULL,
                                          CourseID INT NOT NULL,
                                          PRIMARY KEY (AttendanceID),
                                          FOREIGN KEY (Student_number) REFERENCES student(Student_number) ON DELETE CASCADE ON UPDATE CASCADE,
                                          FOREIGN KEY (CourseID) REFERENCES course(CourseID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'batch' table only if it does not exist
CREATE TABLE IF NOT EXISTS batch (
                                     BatchID INT NOT NULL AUTO_INCREMENT,
                                     Batch_name VARCHAR(255) NOT NULL,
                                     Starting_day DATE NOT NULL,
                                     CourseID INT NOT NULL,
                                     PRIMARY KEY (BatchID),
                                     FOREIGN KEY (CourseID) REFERENCES course(CourseID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'registration' table only if it does not exist
CREATE TABLE IF NOT EXISTS registration (
                                            RegistrationID INT NOT NULL AUTO_INCREMENT,
                                            Registration_day DATE NOT NULL,
                                            Course_payment DECIMAL(10,2) NOT NULL,
                                            Student_number INT NOT NULL,
                                            BatchID INT NOT NULL,
                                            PRIMARY KEY (RegistrationID),
                                            FOREIGN KEY (Student_number) REFERENCES student(Student_number) ON DELETE CASCADE ON UPDATE CASCADE,
                                            FOREIGN KEY (BatchID) REFERENCES batch(BatchID) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'profile' table only if it does not exist
CREATE TABLE IF NOT EXISTS profile (
                                       Class VARCHAR(255) NOT NULL,
                                       Year INT NOT NULL,
                                       Student_number INT NOT NULL,
                                       FOREIGN KEY (Student_number) REFERENCES student(Student_number) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
