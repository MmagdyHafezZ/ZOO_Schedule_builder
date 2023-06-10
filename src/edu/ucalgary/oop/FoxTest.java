package edu.ucalgary.oop;

import static org.junit.Assert.*;
import org.junit.*;
import edu.ucalgary.oop.Fox;

public class FoxTest {
    @Test
    public void testgetCleanTime() {
        Fox fox = new Fox("Rox");
        assertEquals(5, fox.getCageCleanTime());
    }

    @Test
    public void testgetFoodPrepTime() {
        Fox fox = new Fox("Rox");
        assertEquals(5, fox.getFoodPrepTime());
    }

    @Test
    public void testgetFoodTime() {
        Fox fox = new Fox("Rox");
        assertEquals(5, fox.getFeedTime());
    }

    @Test
    public void testCageCleaned() {
        Fox fox = new Fox("Rox");
        assertFalse(fox.isCageCleaned());
        fox.setCageCleaned(true);
        assertTrue(fox.isCageCleaned());
    }

    @Test
    public void testgetNickName() {
        Fox fox = new Fox("Rox");
        assertEquals("Rox", fox.getNickName());
    }

    @Test
    public void testIsNocturnal() {
        Fox fox = new Fox("Foxy");
        assertEquals("nocturnal", Fox.BEHAVIOR);
    }
}
