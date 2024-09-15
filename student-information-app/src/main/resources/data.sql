-- Create the 'users' table only if it does not exist
CREATE TABLE IF NOT EXISTS users (
    id bigint NOT NULL AUTO_INCREMENT,
    birthday date DEFAULT NULL,
    first_name varchar(255) DEFAULT NULL,
    hashed_password varchar(60) DEFAULT NULL,
    last_name varchar(255) DEFAULT NULL,
    name varchar(255) DEFAULT NULL,
    phone_number varchar(255) DEFAULT NULL,
    username varchar(255) UNIQUE NOT NULL,  -- Ensuring username is unique and not nullable
    user_type varchar(31) NOT NULL,
    grade varchar(255) DEFAULT NULL,
    student_class varchar(255) DEFAULT NULL,
    department varchar(255) DEFAULT NULL,
    subject varchar(255) DEFAULT NULL,
    email varchar(255) UNIQUE NOT NULL,  -- Added email field with unique and not-null constraint
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'student' table only if it does not exist
CREATE TABLE IF NOT EXISTS student (
    Student_number int NOT NULL AUTO_INCREMENT,
    Firstname varchar(255) NOT NULL,
    Lastname varchar(255) NOT NULL,
    Email varchar(255) NOT NULL,
    Birthday date NOT NULL,
    Address varchar(255) NOT NULL,
    Phone_number varchar(20) NOT NULL,
    UserID bigint DEFAULT NULL,
    grade varchar(255) DEFAULT NULL,
    student_class varchar(255) DEFAULT NULL,
    PRIMARY KEY (Student_number),
    KEY FK_student_users (UserID),
    CONSTRAINT FK_student_users FOREIGN KEY (UserID) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'teacher' table only if it does not exist
CREATE TABLE IF NOT EXISTS teacher (
    TeacherID int NOT NULL AUTO_INCREMENT,
    Student_name varchar(255) NOT NULL,
    Address varchar(255) NOT NULL,
    Phone_number varchar(20) NOT NULL,
    UserID bigint DEFAULT NULL,
    department varchar(255) DEFAULT NULL,
    subject varchar(255) DEFAULT NULL,
    PRIMARY KEY (TeacherID),
    KEY FK_teacher_users (UserID),
    CONSTRAINT FK_teacher_users FOREIGN KEY (UserID) REFERENCES users (id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create the 'user_roles' table only if it does not exist
CREATE TABLE IF NOT EXISTS user_roles (
    user_id bigint NOT NULL,
    roles enum('STUDENT','TEACHER','USER') NOT NULL,  -- Corrected roles to match enum structure
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

