# UserTranslation Entity Documentation

## Purpose

This class represents the `UserTranslation` entity, used for storing translated values of user fields for different locales.

## Fields

- **id (Long)**: The unique identifier for the user translation, generated automatically.
- **user (User)**: The user associated with this translation, mapped with a many-to-one relationship.
- **locale (Language)**: The locale code for the translation, using the `Language` enum (e.g., 'EN', 'FI').
- **fieldName (String)**: The name of the field being translated, with a maximum length of 50 characters.
- **translatedValue (String)**: The translated value of the field, with a maximum length of 255 characters.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Table(name = "user_translation")**: Specifies the table name for this entity.
- **@Id**: Marks `id` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `id` is generated automatically by the database.
- **@ManyToOne(fetch = FetchType.LAZY)**: Defines a many-to-one relationship with the `User` entity for the `user` field.
- **@JoinColumn(name = "user_id", nullable = false)**: Specifies the foreign key column for the `user` field.
- **@Enumerated(EnumType.STRING)**: Specifies that the `locale` field is an enumeration stored as a string.
- **@Column(name = "locale", nullable = false, length = 5)**: Maps the `locale` field to the `locale` column in the table.
- **@Column(name = "field_name", nullable = false, length = 50)**: Maps the `fieldName` field to the `field_name` column in the table.
- **@Column(name = "translated_value", nullable = false, length = 255)**: Maps the `translatedValue` field to the `translated_value` column in the table.

## Constructors

- **UserTranslation()**: Default constructor required by JPA.
- **UserTranslation(User user, Language locale, String fieldName, String translatedValue)**: Constructor to create a user translation with specified values (excluding `id`).

## Usage

This entity is used to persist translations for user fields, allowing user information to be displayed in different languages as per the user's locale.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
