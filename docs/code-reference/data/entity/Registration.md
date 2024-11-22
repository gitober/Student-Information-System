# Registration Entity Documentation

## Purpose

This class represents the `Registration` entity, used for storing information about student registrations for courses.

## Fields

- **registrationId (Long)**: The unique identifier for the registration, generated automatically.
- **registrationDay (LocalDate)**: The date when the registration took place.
- **coursePayment (Double)**: The payment amount for the course.
- **studentNumber (Long)**: The unique number of the student associated with this registration.
- **student (Student)**: The student associated with this registration, mapped with a many-to-one relationship.
- **batchId (Long)**: The identifier of the batch associated with the registration, if applicable.
- **courseId (Long)**: The identifier of the course associated with the registration.
- **user (User)**: The user associated with the registration, mapped with a many-to-one relationship.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Table(name = "registration")**: Specifies the table name for this entity.
- **@Id**: Marks `registrationId` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `registrationId` is generated automatically by the database.
- **@Column(name = "registration_day")**: Maps the `registrationDay` field to the `registration_day` column in the table.
- **@Column(name = "course_payment")**: Maps the `coursePayment` field to the `course_payment` column in the table.
- **@Column(name = "student_number")**: Maps the `studentNumber` field to the `student_number` column in the table.
- **@ManyToOne**: Defines a many-to-one relationship with the `Student` and `User` entities.
- **@JoinColumn(name = "student_number", referencedColumnName = "student_number", insertable = false, updatable = false)**: Specifies the foreign key column for the `student` field, making it read-only.
- **@Column(name = "batch_id", nullable = true)**: Maps the `batchId` field to the `batch_id` column in the table.
- **@Column(name = "course_id")**: Maps the `courseId` field to the `course_id` column in the table.
- **@JoinColumn(name = "user_id", referencedColumnName = "user_id")**: Specifies the foreign key column for the `user` field.

## Usage

This entity is used to persist registration information for students enrolling in courses, including registration details, course payments, and associations with students, courses, and batches.

---

[Back to System Overview](../../system-overview.md)
