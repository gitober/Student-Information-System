# TeacherTranslationRepository Documentation

## Purpose

This interface represents the `TeacherTranslationRepository`, which provides methods to perform CRUD operations and custom queries for managing `TeacherTranslation` entities in the database.

## Inheritance

- **JpaRepository<TeacherTranslation, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `TeacherTranslation` entity.

## Custom Query Methods

- **findByTeacherIdAndLocale(Long teacherId, String locale)**: Retrieves a list of teacher translations for a specific teacher and locale.
- **findByLocale(String locale)**: Retrieves a list of all translations for a given locale.

## Annotations

- **@Repository**: Marks this interface as a Spring Data repository, allowing it to be detected during component scanning and instantiated as a bean by Spring.

## Usage

This repository is used to manage `TeacherTranslation` entities, providing methods for both basic CRUD operations and custom queries. The custom query methods allow for retrieving teacher translations by teacher ID and locale, which are useful for displaying localized teacher information in different languages.

