# DepartmentTranslation Entity Documentation

## Purpose

This class represents the `DepartmentTranslation` entity, used for storing translated values of department fields for different locales.

## Fields

- **id (Long)**: The unique identifier for the department translation, generated automatically.
- **departmentId (Long)**: The identifier of the department associated with this translation.
- **locale (String)**: The locale code (e.g., 'en', 'fi') for the translation, with a maximum length of 5 characters.
- **fieldName (String)**: The name of the field being translated, with a maximum length of 50 characters.
- **translatedValue (String)**: The translated value of the field, with a maximum length of 255 characters.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Table(name = "department_translation")**: Specifies the table name for this entity.
- **@Id**: Marks `id` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `id` is generated automatically by the database.
- **@Column(name = "department_id", nullable = false)**: Maps the `departmentId` field to the `department_id` column in the table.
- **@Column(name = "locale", nullable = false, length = 5)**: Maps the `locale` field to the `locale` column in the table.
- **@Column(name = "field_name", nullable = false, length = 50)**: Maps the `fieldName` field to the `field_name` column in the table.
- **@Column(name = "translated_value", nullable = false, length = 255)**: Maps the `translatedValue` field to the `translated_value` column in the table.

## Constructors

- **DepartmentTranslation()**: Default constructor required by JPA.
- **DepartmentTranslation(Long departmentId, String locale, String fieldName, String translatedValue)**: Constructor to create a department translation with specified values (excluding `id`).

## Usage

This entity is used to persist translations for department fields, allowing department information to be displayed in different languages as per the user's locale.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
