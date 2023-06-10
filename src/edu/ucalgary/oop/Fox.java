package edu.ucalgary.oop;

public class Fox extends Animal {

    public static final String BEHAVIOR = "nocturnal";
    public static final int[] FEED_TIMES = { 0, 1, 2 };

    /**
     * Creates a new instance of the Fox class with the specified name and sets the 
     * food preparation time to 5 since foxes require some special food preparation.
     *
     * @param name the name of the fox animal.
     */
    public Fox(String name) {
        super(name);
        super.setFoodPrepTime(5);
    }

    /**
     * Returns the feed window for the fox animal which is the first element in the FEED_TIMES array.
     *
     * @return the feed window for the fox animal.
     */
    public int getFeedWindow() {
        return FEED_TIMES[0];
    }

}
