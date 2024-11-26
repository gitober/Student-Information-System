# AbstractEntity Documentation

## Purpose

This class is an abstract entity used as a base for other entities in the application, providing common fields such as `id` and `version`, as well as utility methods for equality and hash code generation.

## Fields

- **id (Long)**: The unique identifier for the entity, generated using a sequence generator with an initial value of 1000.
- **version (int)**: Used for optimistic locking to handle concurrent updates.

## Annotations

- **@Getter**: Generates getter methods for all fields.
- **@Setter**: Generates setter methods for the `id` field.
- **@MappedSuperclass**: Specifies that this class is a superclass that can be inherited by JPA entity classes.
- **@Id**: Marks `id` as the primary key.
- **@GeneratedValue**: Specifies that `id` is generated using a sequence strategy.
- **@SequenceGenerator**: Configures the sequence generator used for generating the `id` value.
- **@Version**: Marks the `version` field for optimistic locking.

## Methods

- **hashCode()**: Generates a hash code based on the `id` field, if it is not null.
- **equals(Object obj)**: Compares two instances of `AbstractEntity` for equality based on the `id` field.

## Usage

This abstract entity is intended to be extended by other entity classes, providing shared functionality and common fields for persistence.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
