package edu.ucalgary.oop;

public class Beaver extends Animal {

    public static final String BEHAVIOR = "dinurnal";
    public static final int[] FEED_TIMES = { 8, 9, 10 };
    
    /**
    * Creates a new instance of the Beaver class with the specified name and sets the 
    * food preparation time to 0 since beavers do not require any special food preparation.
    * @param name the name of the beaver animal.
    */
    public Beaver(String name) {
        super(name);
        super.setFoodPrepTime(0);
    }

    /**
    * Returns the feed window for the beaver animal which is the first element in the FEED_TIMES array.
    * @return the feed window for the beaver animal.
    */
    public int getFeedWindow() {
        return FEED_TIMES[0];
    }

}
