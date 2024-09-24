-- Create the 'users' table only if it does not exist
CREATE TABLE IF NOT EXISTS users (
                                     user_id BIGINT NOT NULL AUTO_INCREMENT,
                                     birthday DATE DEFAULT NULL,
                                     first_name VARCHAR(255) DEFAULT NULL,
                                     last_name VARCHAR(255) DEFAULT NULL,
                                     full_name VARCHAR(255) DEFAULT NULL,
                                     phone_number VARCHAR(255) DEFAULT NULL,
                                     username VARCHAR(255) UNIQUE NOT NULL,
                                     user_type VARCHAR(255) NOT NULL,
                                     grade VARCHAR(255) DEFAULT NULL,
                                     student_class VARCHAR(255) DEFAULT NULL,
                                     department_id BIGINT DEFAULT NULL,
                                     subject_id BIGINT DEFAULT NULL,
                                     email VARCHAR(255) UNIQUE NOT NULL,
                                     address VARCHAR(255) DEFAULT NULL,
                                     student_number BIGINT DEFAULT NULL,
                                     dtype VARCHAR(31) DEFAULT NULL,
                                     hashed_password VARCHAR(60) DEFAULT NULL,
                                     PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'department' table only if it does not exist
CREATE TABLE IF NOT EXISTS department (
                                          department_id BIGINT NOT NULL AUTO_INCREMENT,
                                          department_name VARCHAR(255) UNIQUE NOT NULL,
                                          PRIMARY KEY (department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'subject' table only if it does not exist
CREATE TABLE IF NOT EXISTS subject (
                                       subject_id BIGINT NOT NULL AUTO_INCREMENT,
                                       subject_name VARCHAR(255) NOT NULL,
                                       department_id BIGINT DEFAULT NULL,
                                       PRIMARY KEY (subject_id),
                                       FOREIGN KEY (department_id) REFERENCES department(department_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'student' table only if it does not exist
CREATE TABLE IF NOT EXISTS student (
                                       student_number BIGINT NOT NULL,
                                       first_name VARCHAR(255) NOT NULL,
                                       last_name VARCHAR(255) NOT NULL,
                                       email VARCHAR(255) NOT NULL,
                                       birthday DATE NOT NULL,
                                       address VARCHAR(255) NOT NULL,
                                       phone_number VARCHAR(20) NOT NULL,
                                       user_id BIGINT DEFAULT NULL,
                                       grade VARCHAR(255) DEFAULT NULL,
                                       student_class VARCHAR(255) DEFAULT NULL,
                                       PRIMARY KEY (student_number),
                                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'teacher' table only if it does not exist
CREATE TABLE IF NOT EXISTS teacher (
                                       teacher_id BIGINT NOT NULL AUTO_INCREMENT,
                                       address VARCHAR(255) NOT NULL,
                                       phone_number VARCHAR(20) NOT NULL,
                                       user_id BIGINT DEFAULT NULL,
                                       department_id BIGINT DEFAULT NULL,
                                       subject_id BIGINT DEFAULT NULL,
                                       PRIMARY KEY (teacher_id),
                                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
                                       FOREIGN KEY (department_id) REFERENCES department(department_id) ON DELETE SET NULL ON UPDATE CASCADE,
                                       FOREIGN KEY (subject_id) REFERENCES subject(subject_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'user_roles' table only if it does not exist
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          roles ENUM('STUDENT', 'TEACHER', 'USER') NOT NULL,
                                          PRIMARY KEY (user_id, roles),
                                          FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'course' table only if it does not exist
CREATE TABLE IF NOT EXISTS course (
                                      course_id BIGINT NOT NULL AUTO_INCREMENT,
                                      course_plan VARCHAR(255) NOT NULL,
                                      course_name VARCHAR(255) NOT NULL,
                                      duration INT NOT NULL,
                                      PRIMARY KEY (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'grade' table only if it does not exist
CREATE TABLE IF NOT EXISTS grade (
                                     grade_id BIGINT NOT NULL AUTO_INCREMENT,
                                     grade VARCHAR(2) NOT NULL,
                                     grading_day DATE NOT NULL,
                                     student_number BIGINT NOT NULL,
                                     course_id BIGINT NOT NULL,
                                     PRIMARY KEY (grade_id),
                                     FOREIGN KEY (student_number) REFERENCES student(student_number) ON DELETE CASCADE ON UPDATE CASCADE,
                                     FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'attendance' table only if it does not exist
CREATE TABLE IF NOT EXISTS attendance (
                                          attendance_id BIGINT NOT NULL AUTO_INCREMENT,
                                          attendance_status VARCHAR(255) NOT NULL,
                                          attendance_date DATE NOT NULL,
                                          student_number BIGINT NOT NULL,
                                          course_id BIGINT NOT NULL,
                                          PRIMARY KEY (attendance_id),
                                          FOREIGN KEY (student_number) REFERENCES student(student_number) ON DELETE CASCADE ON UPDATE CASCADE,
                                          FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'batch' table only if it does not exist
CREATE TABLE IF NOT EXISTS batch (
                                     batch_id BIGINT NOT NULL AUTO_INCREMENT,
                                     batch_name VARCHAR(255) NOT NULL,
                                     starting_day DATE NOT NULL,
                                     course_id BIGINT NOT NULL,
                                     PRIMARY KEY (batch_id),
                                     FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'registration' table only if it does not exist
CREATE TABLE IF NOT EXISTS registration (
                                            registration_id BIGINT NOT NULL AUTO_INCREMENT,
                                            registration_day DATE NOT NULL,
                                            course_payment DECIMAL(10, 2) NOT NULL,
                                            student_number BIGINT NOT NULL,
                                            batch_id BIGINT NOT NULL,
                                            PRIMARY KEY (registration_id),
                                            FOREIGN KEY (student_number) REFERENCES student(student_number) ON DELETE CASCADE ON UPDATE CASCADE,
                                            FOREIGN KEY (batch_id) REFERENCES batch(batch_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create the 'profile' table only if it does not exist
CREATE TABLE IF NOT EXISTS profile (
                                       student_class VARCHAR(255) NOT NULL,
                                       year INT NOT NULL,
                                       student_number BIGINT NOT NULL,
                                       PRIMARY KEY (student_number),
                                       FOREIGN KEY (student_number) REFERENCES student(student_number) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
