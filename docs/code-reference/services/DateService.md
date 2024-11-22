# DateService Class Documentation

## Purpose

The `DateService` class is responsible for formatting date ranges based on the current locale. It provides utility methods to format dates in a way that is suitable for different locales.

## Methods

### Public Methods

- **formatDateRange(LocalDate startDate, LocalDate endDate)**: Formats the provided start and end dates into a string based on the current locale.

### Private Methods

- **getFormatterForCurrentLocale()**: Determines the appropriate date format for the current locale and returns a `DateTimeFormatter` instance.
    - **Chinese (ch)**: Formats the date as `yyyy年MM月dd日`.
    - **UK Locale**: Formats the date as `dd/MM/yyyy`.
    - **Finnish (fi-FI)**: Formats the date as `dd.MM.yyyy`.
    - **Russian (ru-RU)**: Formats the date as `dd.MM.yyyy`.
    - **Default (English)**: Formats the date as `dd/MM/yyyy`.

## Annotations

- **@Component**: Marks this class as a Spring component, allowing it to be automatically detected and managed by Spring's application context.

## Usage

The `DateService` class is used for formatting date ranges in the application. It ensures that date formats are adapted based on the current user's locale, providing localized date representations for better user experience.

---

[Back to System Overview](../system-overview.md)
