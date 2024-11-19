# LogoutController Documentation

## Purpose

This class handles HTTP requests for user logout, providing an endpoint to log out the authenticated user and redirect them to the login page.

## Features

- Log out the current user.
- Redirect to the login page after logout.

## Key Methods

- **logout(HttpServletRequest request, HttpServletResponse response)**: Logs out the current user and redirects them to the login page.

## Notes

### HTTP Status Codes:

- **302 Found**: Redirects to the login page after a successful logout.

### Security Dependency

- Utilizes **SecurityContextLogoutHandler** to handle the logout process.

## Example Endpoint

- **Log out the user**: `GET /logout`