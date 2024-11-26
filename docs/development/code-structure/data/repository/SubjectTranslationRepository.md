# SubjectTranslationRepository Documentation

## Purpose

This interface represents the `SubjectTranslationRepository`, which provides methods to perform CRUD operations and custom queries for managing `SubjectTranslation` entities in the database.

## Inheritance

- **JpaRepository<SubjectTranslation, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `SubjectTranslation` entity.

## Custom Query Methods

- **findBySubjectIdAndLocale(Long subjectId, String locale)**: Retrieves a list of subject translations for a specific subject and locale.
- **findByLocale(String locale)**: Retrieves a list of all translations for a given locale.

## Annotations

- **@Repository**: Marks this interface as a Spring Data repository, allowing it to be detected during component scanning and instantiated as a bean by Spring.

## Usage

This repository is used to manage `SubjectTranslation` entities, providing methods for both basic CRUD operations and custom queries. The custom query methods allow for retrieving subject translations by subject ID and locale, which are useful for displaying localized subject information in different languages.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
