package edu.ucalgary.oop;

import static org.junit.Assert.*;
import org.junit.*;


public class BeaverTest {
    /**
    * Tests the getCageCleanTime method of the Beaver class.
    * It creates a new instance of Beaver and checks if the clean time is equal to 5.
    */
    @Test
    public void testgetCleanTime() {
        Beaver beaver = new Beaver("Rox");
        assertEquals(5, beaver.getCageCleanTime());
    }

    /**
     * Tests the getFoodPrepTime method of the Beaver class.
     * It creates a new instance of Beaver and checks if the food prep time is equal to 5.
     */
    @Test
    public void testgetFoodPrepTime() {
        Beaver beaver = new Beaver("Rox");
        assertEquals(0, beaver.getFoodPrepTime());
    }

    /**
     * Tests the getFeedTime method of the Beaver class.
     * It creates a new instance of Beaver and checks if the feed time is equal to 5.
     */
    @Test
    public void testgetFoodTime() {
        Beaver beaver = new Beaver("Rox");
        assertEquals(5, beaver.getFeedTime());
    }

    /**
     * Tests the isCageCleaned and setCageCleaned methods of the Beaver class.
     * It creates a new instance of Beaver, asserts that its cage is not cleaned, 
     * sets the cage to cleaned, then asserts that it is indeed cleaned.
     */
    @Test
    public void testCageCleaned() {
        Beaver beaver = new Beaver("Rox");
        assertFalse(beaver.isCageCleaned());
        beaver.setCageCleaned(true);
        assertTrue(beaver.isCageCleaned());
    }

    /**
     * Tests the getNickName method of the Beaver class.
     * It creates a new instance of Beaver and checks if the nickname is "Rox".
     */
    @Test
    public void testgetNickName() {
        Beaver beaver = new Beaver("Rox");
        assertEquals("Rox", beaver.getNickName());
    }

    /**
     * Tests the BEHAVIOR constant of the Beaver class.
     * It creates a new instance of Beaver and checks if its behavior is "dinurnal".
     */
    @Test
    public void testIsNocturnal() {
        Beaver beaver = new Beaver("Rox");
        assertEquals("dinurnal", Beaver.BEHAVIOR);
    }

}
