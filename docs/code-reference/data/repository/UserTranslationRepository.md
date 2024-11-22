# UserTranslationRepository Documentation

## Purpose

This interface represents the `UserTranslationRepository`, which provides methods to perform CRUD operations and custom queries for managing `UserTranslation` entities in the database.

## Inheritance

- **JpaRepository<UserTranslation, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `UserTranslation` entity.

## Custom Query Methods

- **findByUser_IdAndLocale(Long userId, Language locale)**: Retrieves a list of user translations for a specific user and locale.
- **findByLocale(Language locale)**: Retrieves a list of all translations for a given locale.

## Annotations

- **@Repository**: Marks this interface as a Spring Data repository, allowing it to be detected during component scanning and instantiated as a bean by Spring.

## Usage

This repository is used to manage `UserTranslation` entities, providing methods for both basic CRUD operations and custom queries. The custom query methods allow for retrieving user translations by user ID and locale, which are useful for displaying localized user information in different languages.

---

[Back to System Overview](../../system-overview.md)