# Main Layout Documentation

## Overview

The `MainLayout` class serves as the primary layout component for the application, providing a consistent header throughout different pages. It utilizes the `RouterLayout` interface, enabling other views to be displayed within the layout's structure. The main purpose of this layout is to determine and display an appropriate header based on the user's authentication status.

### Main Features

1. **Dynamic Header Display**:
    - Displays either a full header (for logged-in users) or a minimal header (for public pages).
    - Uses the `AuthenticatedUser` instance to determine whether the user is logged in.

2. **Router Layout Integration**:
    - Implements the `RouterLayout` interface, allowing child views to be displayed within this layout.
    - Provides a consistent layout across the entire application by embedding a `HeaderView`.

### Implementation Details

- **Header Management**:
    - If the user is authenticated (`authenticatedUser.get().isPresent()`), the layout displays a full header (`HeaderView`) with additional links for navigation (e.g., to courses, grades, profile).
    - For unauthenticated users, a minimal version of the header is displayed, which only includes basic information like the app logo and language flags.

- **Spring Annotations**:
    - **`@SpringComponent`**: Marks the class as a Spring-managed component.
    - **`@RouteScope`**: Ensures the component is scoped to the route lifecycle, allowing it to persist across page navigation within the same route.

- **Constructor Injection**:
    - Uses constructor injection for `AuthenticatedUser` and `MessageSource`, ensuring that dependencies are provided by Spring and making it easier to test and maintain.

- **Content Setup**:
    - The header is added to the main content layout (`Div`), which forms the base container for the layout.

### Usage

The `MainLayout` is a crucial component that ensures a consistent user experience across the application. It dynamically adjusts the header based on the user's authentication status, providing the appropriate navigation options and appearance. It is typically used as a parent layout for other views, making sure the header and navigation are unified across the entire application.

This class enhances the application's UX by maintaining a familiar navigation bar throughout the user's journey, whether they are logged in or browsing as a guest.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)