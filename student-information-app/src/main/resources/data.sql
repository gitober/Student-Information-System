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
