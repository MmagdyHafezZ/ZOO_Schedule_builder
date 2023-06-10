package edu.ucalgary.oop;

import static org.junit.Assert.*;
import org.junit.*;
import edu.ucalgary.oop.Coyote;

public class CoyoteTest {
    /**
     * Test for checking the getCleanTime method in Coyote class
     */
    @Test
    public void testgetCleanTime() {
        Coyote coyote = new Coyote("Rox");
        assertEquals(5, coyote.getCageCleanTime());
    }

    /**
     * Test for checking the getFoodPrepTime method in Coyote class
     */
    @Test
    public void testgetFoodPrepTime() {
        Coyote coyote = new Coyote("Rox");
        assertEquals(10, coyote.getFoodPrepTime());
    }

    /**
     * Test for checking the getFeedTime method in Coyote class
     */
    @Test
    public void testgetFoodTime() {
        Coyote coyote = new Coyote("Rox");
        assertEquals(5, coyote.getFeedTime());
    }

    /**
     * Test for checking the setCageCleaned and isCageCleaned methods in Coyote class
     */
    @Test
    public void testCageCleaned() {
        Coyote coyote = new Coyote("Rox");
        assertFalse(coyote.isCageCleaned());
        coyote.setCageCleaned(true);
        assertTrue(coyote.isCageCleaned());
    }


    /**
     * Test for checking the getNickName method in Coyote class
     */
    @Test
    public void testgetNickName() {
        Coyote coyote = new Coyote("Rox");
        assertEquals("Rox", coyote.getNickName());
    }

    /**
     * Test for checking the IsNocturnal method in Coyote class
     */
    @Test
    public void testIsNocturnal() {
        Fox fox = new Fox("Foxy");
        assertEquals("nocturnal", Fox.BEHAVIOR);
    }
}
