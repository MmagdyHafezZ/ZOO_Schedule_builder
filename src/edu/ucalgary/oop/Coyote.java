package edu.ucalgary.oop;

public class Coyote extends Animal {

    public static final String BEHAVIOR = "crepuscular";
    public static final int[] FEED_TIMES = { 19, 20, 21 };

    /**
     * Creates a new instance of the Coyote class with the specified name and sets the 
     * food preparation time to 10 since coyotes require some special food preparation.
     * @param name the name of the coyote animal.
     */
    public Coyote(String name) {
        super(name);
        super.setFoodPrepTime(10);

    }

    /**
     * Returns the feed window for the coyote animal which is the first element in the FEED_TIMES array.
    * @return the feed window for the coyote animal.
    */
    public int getFeedWindow() {
        return FEED_TIMES[0];
    }

}
