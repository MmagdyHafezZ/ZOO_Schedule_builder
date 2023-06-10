package edu.ucalgary.oop;

public class Porcupine extends Animal {

    public static final String BEHAVIOR = "crepuscular";
    public static final int[] FEED_TIMES = { 19, 20, 21 };

    /**
     * Creates a new instance of the Porcupine class with the specified name and sets the 
     * cage clean time to 10 and food preparation time to 0 in the constructor as porcupines have 
     * a specific cage cleaning requirement and no food preparation requirement.
     *
     * @param name the name of the porcupine animal.
     */
    public Porcupine(String name) {
        super(name);
        super.setCageCleanTime(10);
        super.setFoodPrepTime(0);
    }

    /**
     * Returns the feed window for the porcupine animal which is the first element in the FEED_TIMES array.
     *
     * @return the feed window for the porcupine animal.
     */
    public int getFeedWindow() {
        return FEED_TIMES[0];
    }

}
