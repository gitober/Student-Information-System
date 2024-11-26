# UserController Documentation

## Purpose

This class handles HTTP requests for managing user records, providing RESTful endpoints for CRUD operations, including secure password management.

## Features

- Retrieve all users or a specific user by ID.
- Create new users.
- Update existing users by ID, including password encryption.
- Delete users by ID.

## Key Methods

- **getAllUsers()**: Retrieves a list of all users.
- **getUserById(Long id)**: Retrieves a specific user by their ID.
- **createUser(User user)**: Creates a new user record.
- **updateUser(Long id, User user)**: Updates an existing user by their ID, modifying provided fields, including hashing the password if provided.
- **deleteUser(Long id)**: Deletes a user by their ID.

## Notes

### HTTP Status Codes:

- **200 OK**: Success.
- **201 Created**: New record created.
- **204 No Content**: Record deleted.
- **404 Not Found**: User not found.

### Service Dependencies

- Utilizes **UserService** to handle user-related business logic.
- Utilizes **PasswordEncoder** to hash passwords when creating or updating users.

## Example Endpoints

- **Get all users**: `GET /users`
- **Get user by ID**: `GET /users/{id}`
- **Create a new user**: `POST /users`
- **Update user by ID**: `PUT /users/{id}`
- **Delete user by ID**: `DELETE /users/{id}`

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)

