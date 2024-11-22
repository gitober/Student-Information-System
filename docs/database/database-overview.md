# Database Overview

This document provides an overview of the database schema used in the **Student Information System**. It describes the main tables, their relationships, and the key data stored in each. The database is designed to manage student information, courses, grades, attendance, and user profiles.

## Contents
- [Course and Batch Tables](#course-and-batch-tables)
- [Department and Subjects](#department-and-subjects)
- [Enrollments and Grades](#enrollments-and-grades)
- [Users and Roles](#users-and-roles)
- [Attendance and Registration](#attendance-and-registration)
- [Teacher and Student Details](#teacher-and-student-details)
- [Translations](#translations)

## Course and Batch Tables

### Course
- **Table:** `course`
- **Description:** Stores information about each course, including the **course ID**, **name**, **plan**, and **duration**.

```sql
create table course (
    course_id   bigint auto_increment primary key,
    course_plan varchar(255) not null,
    course_name varchar(255) not null,
    duration    int not null
);
```

### Batch
- **Table:** `batch`
- **Description:** Represents specific course batches. Stores **batch name**, **starting day**, and links to a course via **course ID**.
- **Foreign Key:** `course_id` references the `course` table.

```sql
create table batch (
    batch_id     bigint auto_increment primary key,
    batch_name   varchar(255) not null,
    starting_day date not null,
    course_id    bigint not null,
    constraint batch_ibfk_1 foreign key (course_id) references course (course_id)
        on update cascade on delete cascade
);
```

## Department and Subjects

### Department
- **Table:** `department`
- **Description:** Stores information about academic departments, including **department ID** and **department name**.

```sql
create table department (
    department_id   bigint auto_increment primary key,
    department_name varchar(255) not null unique
);
```

### Subject
- **Table:** `subject`
- **Description:** Represents subjects offered by departments. Stores **subject ID**, **subject name**, and links to a department via **department ID**.
- **Foreign Key:** `department_id` references the `department` table.

```sql
create table subject (
    subject_id    bigint auto_increment primary key,
    subject_name  varchar(255) not null,
    department_id bigint not null,
    constraint subject_ibfk_1 foreign key (department_id) references department (department_id)
        on update cascade on delete cascade
);
```

## Enrollments and Grades

### Enrollments
- **Table:** `enrollments`
- **Description:** Tracks which students are enrolled in which courses. Stores references to **course ID** and **student ID**.

```sql
create table enrollments (
    id         bigint auto_increment primary key,
    course_id  bigint null,
    student_id bigint null
);
```

### Grade
- **Table:** `grade`
- **Description:** Stores students' grades for specific courses. Contains **grade value**, **grading day**, **student number**, and links to the **course**.
- **Foreign Key:** `course_id` references the `course` table.

```sql
create table grade (
    grade_id       int auto_increment primary key,
    grade          varchar(5) null,
    grading_day    date not null,
    student_number bigint not null,
    course_id      bigint not null,
    constraint FK7e8ca7hfmrpruicqhocskjlf2 foreign key (course_id) references course (course_id)
);
```

## Users and Roles

### Users
- **Table:** `users`
- **Description:** Stores user information, including **user ID**, **name**, **email**, **role**, and other personal details. Also includes **department** and **subject** information for teachers.

```sql
create table users (
    user_id         bigint auto_increment primary key,
    first_name      varchar(255) null,
    last_name       varchar(255) null,
    username        varchar(255) not null,
    user_type       varchar(255) not null,
    email           varchar(255) not null unique,
    department_id   bigint null,
    subject_id      bigint null,
    constraint FKc97c37w3y006qko0729lwban1 foreign key (subject_id) references subject (subject_id),
    constraint FKfi832e3qv89fq376fuh8920y4 foreign key (department_id) references department (department_id)
);
```

### User Roles
- **Table:** `user_roles`
- **Description:** Associates users with their respective roles, e.g., **STUDENT**, **TEACHER**, etc.

```sql
create table user_roles (
    user_id bigint not null,
    roles   enum ('STUDENT', 'TEACHER', 'USER') not null,
    primary key (user_id, roles),
    constraint FK_user_roles foreign key (user_id) references users (user_id)
        on update cascade on delete cascade
);
```

## Attendance and Registration

### Attendance
- **Table:** `attendance`
- **Description:** Manages attendance records for students in courses, including **attendance status**, **attendance date**, and references to the **course** and **student number**.

```sql
create table attendance (
    attendance_id     bigint auto_increment primary key,
    attendance_status varchar(255) not null,
    attendance_date   date not null,
    student_number    bigint not null,
    course_id         bigint not null,
    constraint FKbuj0yeeg4rh038nm1x28t0m88 foreign key (student_number) references users (student_number),
    constraint attendance_ibfk_2 foreign key (course_id) references course (course_id)
        on update cascade on delete cascade
);
```

### Registration
- **Table:** `registration`
- **Description:** Manages the registration of students in batches, including **registration date**, **course**, **batch**, and **student details**.
- **Foreign Keys:** References **batch**, **student**, and **course**.

```sql
create table registration (
    registration_id  bigint auto_increment primary key,
    registration_day date not null,
    student_number   bigint not null,
    batch_id         bigint null,
    course_id        bigint null,
    constraint FK1ve0pr9rx0guqq8e49wa97j07 foreign key (student_number) references users (student_number),
    constraint registration_ibfk_2 foreign key (batch_id) references batch (batch_id)
        on update cascade on delete cascade
);
```

## Teacher and Student Details

### Teacher
- **Table:** `teacher`
- **Description:** Stores teacher information, including references to **user**, **department**, and **subject**.

```sql
create table teacher (
    teacher_id    bigint auto_increment primary key,
    address       varchar(255) null,
    user_id       bigint null,
    department_id bigint null,
    subject_id    bigint null,
    constraint FK_teacher_user foreign key (user_id) references users (user_id),
    constraint teacher_ibfk_1 foreign key (department_id) references department (department_id)
        on update cascade on delete set null
);
```

### Student
- **Table:** `student`
- **Description:** Represents students, with references to the **user** and additional student-specific information.

```sql
create table student (
    address       varchar(255) null,
    user_id       bigint null,
    grade         varchar(255) null,
    student_class varchar(255) null,
    constraint student_ibfk_1 foreign key (user_id) references users (user_id)
        on update cascade on delete set null
);
```

## Translations

### Course Translation
- **Table:** `course_translation`
- **Description:** Stores translations for course fields in different languages.
- **Foreign Key:** `course_id` references the `course` table.

```sql
create table course_translation (
    id               bigint auto_increment primary key,
    course_id        bigint not null,
    locale           varchar(5) not null,
    field_name       varchar(50) not null,
    translated_value varchar(255) not null,
    constraint FKo07pv1i5tntp4ovua364ifif9 foreign key (course_id) references course (course_id)
);
```

### User Translation
- **Table:** `user_translation`
- **Description:** Stores translations for user-specific fields in different languages.
- **Foreign Key:** `user_id` references the `users` table.

```sql
create table user_translation (
    id               bigint auto_increment primary key,
    user_id          bigint not null,
    locale           varchar(5) not null,
    field_name       varchar(50) not null,
    translated_value varchar(255) not null,
    constraint fk_user_translation_user foreign key (user_id) references users (user_id)
);
```

## Summary
The database schema for the **Student Information System** efficiently organizes student and teacher information, course management, attendance tracking, and other essential elements needed for a student-centric educational application. Translation tables are included to support multiple languages, ensuring that all users have access to their preferred language. The use of relational foreign keys ensures data consistency and facilitates the management of relationships between entities.

