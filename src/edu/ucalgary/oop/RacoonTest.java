package edu.ucalgary.oop;

import static org.junit.Assert.*;
import org.junit.*;
import edu.ucalgary.oop.Racoon;

public class RacoonTest {
    @Test
    public void testgetCleanTime() {
        Racoon racoon = new Racoon("Rox");
        assertEquals(5, racoon.getCageCleanTime());
    }

    @Test
    public void testgetFoodPrepTime() {
        Racoon racoon = new Racoon("Rox");
        assertEquals(0, racoon.getFoodPrepTime());
    }

    @Test
    public void testgetFoodTime() {
        Racoon racoon = new Racoon("Rox");
        assertEquals(5, racoon.getFeedTime());
    }

    @Test
    public void testCageCleaned() {
        Racoon racoon = new Racoon("Rox");
        assertFalse(racoon.isCageCleaned());
        racoon.setCageCleaned(true);
        assertTrue(racoon.isCageCleaned());
    }

    @Test
    public void testgetNickName() {
        Racoon racoon = new Racoon("Rox");
        assertEquals("Rox", racoon.getNickName());
    }
    
    @Test
    public void testIsNocturnal() {
        Racoon racoon = new Racoon("Rox");
        assertEquals("nocturnal", Racoon.BEHAVIOR);
    }
}
