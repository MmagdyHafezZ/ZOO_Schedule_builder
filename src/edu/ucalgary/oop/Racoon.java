package edu.ucalgary.oop;

public class Racoon extends Animal {

    public static final String BEHAVIOR = "nocturnal";
    public static final int[] FEED_TIMES = { 0, 1, 2 };

    /** 
     * Constructor for creating a new Racoon object.
     * @param name The name of the raccoon.
     * Initializes the food preparation time to 0 using the "setFoodPrepTime()" method inherited from the parent "Animal" class.
     */
    public Racoon(String name) {
        super(name);
        super.setFoodPrepTime(0);
    }

    /** 
     * Returns the start of the feeding window for the raccoon.
     * @return The first element of the "FEED_TIMES" integer array.
     */
    public int getFeedWindow() {
        return FEED_TIMES[0];
    }

}
