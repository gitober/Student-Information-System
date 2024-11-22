# DepartmentController Documentation

## Purpose

This class handles HTTP requests for managing department translations, providing an endpoint for retrieving department names in different languages.

## Features

- Retrieve translations for department names by locale.

## Key Methods

- **getDepartmentTranslations(String locale)**: Retrieves a list of department translations for the specified locale.

## Notes

### HTTP Status Codes:

- **200 OK**: Success.

### Service Dependency

- Utilizes **TranslationService** to handle translation-related business logic.

## Example Endpoints

- **Get department translations**: `GET /api/departments/translations?locale={locale}`

---

[Back to System Overview](../system-overview.md)

