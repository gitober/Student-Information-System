package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AbstractEntityTest {

    private AbstractEntity entity1;
    private AbstractEntity entity2;

    @BeforeEach
    void setUp() {
        // Create anonymous subclasses of AbstractEntity for testing
        entity1 = new AbstractEntity() {};
        entity2 = new AbstractEntity() {};

        // Set IDs and version for testing
        entity1.setId(1001L);
        entity2.setId(1002L);
    }

    @AfterEach
    void tearDown() {
        entity1 = null;
        entity2 = null;
    }

    @Test
    void testGetId() {
        // Check if getId() returns the correct ID
        assertEquals(1001L, entity1.getId());
        assertEquals(1002L, entity2.getId());
    }

    @Test
    void testSetId() {
        // Set a new ID and check if it is updated correctly
        entity1.setId(2001L);
        assertEquals(2001L, entity1.getId());
    }

    @Test
    void testHashCode() {
        // Check if hashCode() is based on the ID
        assertEquals(entity1.getId().hashCode(), entity1.hashCode());
    }

    @Test
    void testEquals() {
        // Check if two entities with the same ID are equal
        entity2.setId(1001L);
        assertEquals(entity1, entity2);
    }

    @Test
    void testNotEquals() {
        // Check if two entities with different IDs are not equal
        assertNotEquals(entity1, entity2);
    }
}
