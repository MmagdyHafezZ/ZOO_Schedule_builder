package edu.ucalgary.oop;

import static org.junit.Assert.*;
import org.junit.*;
import edu.ucalgary.oop.Porcupine;

public class PorcupineTest {
    @Test
    public void testgetCleanTime() {
        Porcupine porcupine = new Porcupine("Rox");
        assertEquals(10, porcupine.getCageCleanTime());
    }

    @Test
    public void testgetFoodPrepTime() {
        Porcupine porcupine = new Porcupine("Rox");
        assertEquals(0, porcupine.getFoodPrepTime());
    }

    @Test
    public void testgetFoodTime() {
        Porcupine porcupine = new Porcupine("Rox");
        assertEquals(5, porcupine.getFeedTime());
    }

    @Test
    public void testCageCleaned() {
        Porcupine porcupine = new Porcupine("Rox");
        assertFalse(porcupine.isCageCleaned());
        porcupine.setCageCleaned(true);
        assertTrue(porcupine.isCageCleaned());
    }

    @Test
    public void testgetNickName() {
        Porcupine porcupine = new Porcupine("Rox");
        assertEquals("Rox", porcupine.getNickName());
    }

}
