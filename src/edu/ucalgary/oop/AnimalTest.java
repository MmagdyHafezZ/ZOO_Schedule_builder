package edu.ucalgary.oop;
import org.junit.Test;
import static org.junit.Assert.*;


public class AnimalTest {

    /**
     * This method tests the set and get methods for the cage cleaned attribute of the Animal class.
     */
    @Test
    public void testSetAndGetCageCleaned() {
        Animal animal = new Animal("test");
        assertFalse(animal.isCageCleaned());
        animal.setCageCleaned(true);
        assertTrue(animal.isCageCleaned());
    }

    /**
     * This method tests the set and get methods for the cage clean time attribute of the Animal class.
     */
    @Test
    public void testSetAndGetCageCleanTime() {
        Animal animal = new Animal("test");
        assertEquals(5, animal.getCageCleanTime());
        animal.setCageCleanTime(10);
        assertEquals(10, animal.getCageCleanTime());
    }

    /**
     * This method tests the set and get methods for the food preparation time attribute of the Animal class.
     */
    @Test
    public void testSetAndGetFoodPrepTime() {
        Animal animal = new Animal("test");
        assertEquals(0, animal.getFoodPrepTime());
        animal.setFoodPrepTime(2);
        assertEquals(2, animal.getFoodPrepTime());
    }

    /**
     * This method tests the set and get methods for the feed time attribute of the Animal class.
     */
    @Test
    public void testSetAndGetFeedTime() {
        Animal animal = new Animal("test");
        assertEquals(5, animal.getFeedTime());
        animal.setFeedTime(10);
        assertEquals(10, animal.getFeedTime());
    }

    /**
     * This method tests the set and get methods for the nickname attribute of the Animal class.
     */
    @Test
    public void testSetAndGetNickName() {
        Animal animal = new Animal("test");
        assertEquals("test", animal.getNickName());
        animal.setNickName("newtest");
        assertEquals("newtest", animal.getNickName());
    }

    /**
     * This method tests the getAnimalDetails method of the Animal class.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAnimalDetailsWithInvalidName() {
        Animal.getAnimalDetails("invalidAnimal");
    }

    /**
     * This method tests the getAnimalDetails method of the Animal class.
     */
    @Test
    public void testGetAnimalDetailsWithValidNames() {
        int[] beaverDetails = Animal.getAnimalDetails("beaver");
        assertArrayEquals(new int[]{5, 0, 5, 8}, beaverDetails);

        int[] coyoteDetails = Animal.getAnimalDetails("coyote");
        assertArrayEquals(new int[]{5, 10, 5, 19}, coyoteDetails);

        int[] foxDetails = Animal.getAnimalDetails("fox");
        assertArrayEquals(new int[]{5, 5, 5, 0}, foxDetails);

        int[] porcupineDetails = Animal.getAnimalDetails("porcupine");
        assertArrayEquals(new int[]{10, 0, 5, 19}, porcupineDetails);

        int[] racoonDetails = Animal.getAnimalDetails("racoon");
        assertArrayEquals(new int[]{5, 0, 5, 0}, racoonDetails);
    }
}
