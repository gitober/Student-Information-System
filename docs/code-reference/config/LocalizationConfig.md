# LocalizationConfig Documentation

## Purpose
This class configures internationalization (i18n) for the application, enabling support for multiple languages.

## Features
- Configures a `MessageSource` for resolving multilingual messages.
- Stores user locale preferences in a cookie (`user-lang`).
- Allows dynamic language switching via the `lang` parameter in the URL.

## Key Methods
- **messageSource()**: Loads messages from resource bundles.
- **localeResolver()**: Configures a `CookieLocaleResolver`.
- **localeChangeInterceptor()**: Enables locale switching.

## Notes
- Ensure `messages_{locale}.properties` files exist for supported languages.
- Example URL for switching locale: `http://example.com?lang=fi`.
