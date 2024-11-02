package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DepartmentTranslationTest {

    private DepartmentTranslation departmentTranslation;

    @BeforeEach
    void setUp() {
        // Create a DepartmentTranslation instance with sample data
        departmentTranslation = new DepartmentTranslation(1L, "EN", "departmentName", "Translated Department Name");
        departmentTranslation.setId(1L);
    }

    @AfterEach
    void tearDown() {
        departmentTranslation = null;
    }

    @Test
    void testGetId() {
        // Check if getId() returns the correct ID
        assertEquals(1L, departmentTranslation.getId());
    }

    @Test
    void testGetDepartmentId() {
        // Check if getDepartmentId() returns the correct department ID
        assertEquals(1L, departmentTranslation.getDepartmentId());
    }

    @Test
    void testGetLocale() {
        // Check if getLocale() returns the correct locale
        assertEquals("EN", departmentTranslation.getLocale());
    }

    @Test
    void testGetFieldName() {
        // Check if getFieldName() returns the correct field name
        assertEquals("departmentName", departmentTranslation.getFieldName());
    }

    @Test
    void testGetTranslatedValue() {
        // Check if getTranslatedValue() returns the correct translated value
        assertEquals("Translated Department Name", departmentTranslation.getTranslatedValue());
    }

    @Test
    void testSetters() {
        // Test the setters
        departmentTranslation.setDepartmentId(2L);
        departmentTranslation.setLocale("FI");
        departmentTranslation.setFieldName("description");
        departmentTranslation.setTranslatedValue("Käännetty osaston nimi");

        assertEquals(2L, departmentTranslation.getDepartmentId());
        assertEquals("FI", departmentTranslation.getLocale());
        assertEquals("description", departmentTranslation.getFieldName());
        assertEquals("Käännetty osaston nimi", departmentTranslation.getTranslatedValue());
    }
}
