package com.studentinfo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class LocalizationConfig implements WebMvcConfigurer {

    // Configure the message source for i18n
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // Assumes `messages_en.properties`, etc.
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false); // Fall back to default messages when specific locale is unavailable
        return messageSource;
    }

    // Use a CookieLocaleResolver to persist the locale in a cookie
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH); // Set default locale to English
        localeResolver.setCookieName("user-lang"); // Set cookie name (deprecated)
        localeResolver.setCookieMaxAge(3600 * 24 * 365); // Set cookie max age to 1 year (deprecated)
        return localeResolver;
    }

    // Interceptor that switches locale based on a "lang" parameter
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // The name of the parameter in the URL that changes the locale
        return interceptor;
    }

    // Add the locale interceptor to Spring's interceptor registry
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
