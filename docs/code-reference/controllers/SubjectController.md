# SubjectController Documentation

## Purpose

This class handles HTTP requests for managing subject translations, providing an endpoint for retrieving translations for a specific subject in a specified language.

## Features

- Retrieve translations for a specific subject by subject ID and locale.

## Key Methods

- **getSubjectTranslations(Long id, String locale)**: Retrieves a list of translations for a specific subject using its ID and the specified locale.

## Notes

### HTTP Status Codes:

- **200 OK**: Success.
- **404 Not Found**: No translations found for the given subject ID and locale.

### Service Dependency

- Utilizes **TranslationService** to handle translation-related business logic.

## Example Endpoint

- **Get subject translations**: `GET /api/subjects/{id}/translations?locale={locale}`

---

[Back to System Overview](../system-overview.md)
