# CourseTranslationRepository Documentation

## Purpose

This interface represents the `CourseTranslationRepository`, which provides methods to perform CRUD operations and custom queries for managing `CourseTranslation` entities in the database.

## Inheritance

- **JpaRepository<CourseTranslation, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `CourseTranslation` entity.

## Custom Query Methods

- **findByCourse_CourseIdAndLocale(Long courseId, String locale)**: Retrieves a list of course translations for a specific course and locale.
- **findByCourse_CourseIdAndLocaleAndFieldName(Long courseId, String locale, String fieldName)**: Retrieves an optional course translation for a specific course, locale, and field name.
- **findByLocale(String locale)**: Retrieves a list of all translations for a given locale.

## Annotations

- **@Repository**: Marks this interface as a Spring Data repository, allowing it to be detected during component scanning and instantiated as a bean by Spring.

## Usage

This repository is used to manage `CourseTranslation` entities, providing methods for both basic CRUD operations and custom queries. The custom query methods allow for retrieving course translations by course ID, locale, and specific field names, which are useful for displaying translated course information in different languages.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
