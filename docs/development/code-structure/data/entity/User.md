# User Entity Documentation

## Purpose

This class represents the `User` entity, which serves as the base class for all users in the application, such as students and teachers. It implements the `UserDetails` interface for Spring Security, allowing it to be used for authentication and authorization purposes.

## Fields

- **id (Long)**: The unique identifier for the user, generated automatically.
- **username (String)**: The unique username of the user, used for login purposes.
- **hashedPassword (String)**: The hashed password of the user, stored securely.
- **roles (Set<Role>)**: The roles assigned to the user, represented as a set of `Role` enums.
- **firstName (String)**: The first name of the user.
- **lastName (String)**: The last name of the user.
- **birthday (LocalDate)**: The date of birth of the user.
- **phoneNumber (String)**: The phone number of the user.
- **email (String)**: The unique email address of the user.
- **studentNumber (Long)**: The student number of the user, if applicable (e.g., for students).
- **userType (String)**: The type of the user (e.g., 'STUDENT', 'TEACHER').

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Table(name = "users")**: Specifies the table name for this entity.
- **@Inheritance(strategy = InheritanceType.JOINED)**: Specifies the inheritance strategy for entities that extend `User`.
- **@Id**: Marks `id` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `id` is generated automatically by the database.
- **@Column(unique = true, nullable = false)**: Maps the `username` and `email` fields to columns in the table, ensuring they are unique and not null.
- **@JsonIgnore**: Ensures that the `hashedPassword` field is not serialized in JSON responses.
- **@Enumerated(EnumType.STRING)**: Specifies that the `roles` field is an enumeration stored as a string.
- **@ElementCollection(fetch = FetchType.EAGER)**: Defines the `roles` field as a collection of elements that should be eagerly fetched.
- **@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))**: Specifies the join table for the `roles` field.

## Methods

- **getRoleAuthorities()**: Returns the set of granted authorities based on the user's roles.
- **getAuthorities()**: Implements the `UserDetails` method to return the user's authorities.
- **getPassword()**: Implements the `UserDetails` method to return the user's hashed password.
- **isAccountNonExpired()**: Returns `true`, indicating that the account is not expired.
- **isAccountNonLocked()**: Returns `true`, indicating that the account is not locked.
- **isCredentialsNonExpired()**: Returns `true`, indicating that the credentials are not expired.
- **isEnabled()**: Returns `true`, indicating that the account is enabled.
- **equals(Object o)**: Compares two instances of `User` for equality based on their ID.
- **hashCode()**: Returns a hash code for the user, using a constant value for simplicity.
- **toString()**: Returns a string representation of the user, including their first and last names.

## Usage

This entity is used to store and manage user information, including authentication credentials, personal details, and roles. It serves as the base class for other user types, such as students and teachers, and integrates with Spring Security for authentication.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
