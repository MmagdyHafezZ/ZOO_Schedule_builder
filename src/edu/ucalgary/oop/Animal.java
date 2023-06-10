package edu.ucalgary.oop;

public class Animal {
    // Declare private instance variables
    private boolean cageCleaned = false;
    private int cageCleanTime = 5;
    private int foodPrepTime = 0;
    private int feedTime = 5;
    private String nickName;

    /**
     * Animal constructor takes a nickname parameter.
     * @param nickName
     */
    public Animal(String nickName) {
        // Set the instance variable nickName to the value of the nickname parameter
        this.nickName = nickName;
    }

    /**
     * a method to check if the cage has been cleaned
     * @return cageCleaned
     */
    public boolean isCageCleaned() {
        return cageCleaned;
    }

    /**
     * Constructor for cage cleaned, takes a boolean parameter.
     * @param cageCleaned
     */
    public void setCageCleaned(boolean cageCleaned) {
        this.cageCleaned = cageCleaned;
    }

    /**
     * Getter for cageCleanTime instance variable
     * @return cageCleanTime
     */
    public int getCageCleanTime() {
        return cageCleanTime;
    }

    /**
     * Setter for an integer parameter cageCleanTime.
     * @param cageCleanTime
     */
    public void setCageCleanTime(int cageCleanTime) {
        this.cageCleanTime = cageCleanTime;
    }

    /**
     * Getter for foodPrepTime instance variable
     * @return foodPrepTime
     */
    public int getFoodPrepTime() {
        return foodPrepTime;
    }


    /**
     * Constructor for food time prepreation with an integer parameter foodPrepTime
     */
    public void setFoodPrepTime(int foodPrepTime) {
        this.foodPrepTime = foodPrepTime;
    }

    /**
     * Getter for feedTime instance variable
     * @return feedTime
     */
    public int getFeedTime() {
        return feedTime;
    }

    /**
     * Setter for setting the feed time int
     * @param feedTime
     */
    public void setFeedTime(int feedTime) {
        this.feedTime = feedTime;
    }

    /**
     * Getter method for nickName instance variable
     * @return nickname
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Setter for setting a nickname string
     * @param nickName
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * A static method that takes an animal name as input and returns an integer array containing
     * details about the animal's feeding, cage cleaning and food preparation times.
     * If the animal name is not one of the supported types, an IllegalArgumentException is thrown.
     * The method supports the following animal types: beaver, coyote, fox, porcupine, racoon.
     * @param animalName the name of the animal for which details are to be retrieved
     * @return an integer array containing details about the animal's feeding,
     * cage cleaning and food preparation times. The array is in the format 
     * { cageCleanTime, foodPrepTime, feedTime, feedWindow }
     * where feedWindow is the time window in which the animal must be fed.
     * @throws IllegalArgumentException if the animal name is not one of the supported types.
     */

    static int[] getAnimalDetails(String animalName) throws IllegalArgumentException {
        Animal animal;
        int feedWindow; // the time window in which the animal must be fed
        if (animalName.equalsIgnoreCase("beaver")) {
            animal = new Beaver(animalName);
            feedWindow = Beaver.FEED_TIMES[0];
        } else if (animalName.equalsIgnoreCase("coyote")) {
            animal = new Coyote(animalName);
            feedWindow = Coyote.FEED_TIMES[0];
        } else if (animalName.equalsIgnoreCase("fox")) {
            animal = new Fox(animalName);
            feedWindow = Fox.FEED_TIMES[0];
        } else if (animalName.equalsIgnoreCase("porcupine")) {
            animal = new Porcupine(animalName);
            feedWindow = Porcupine.FEED_TIMES[0];
        } else if (animalName.equalsIgnoreCase("racoon")) {
            animal = new Racoon(animalName);
            feedWindow = Racoon.FEED_TIMES[0];
        } else {
            throw new IllegalArgumentException("Invalid animal name: " + animalName);
        }
        int feedTime = animal.getFeedTime();
        int cageCleanTime = animal.getCageCleanTime();
        int foodPrepTime = animal.getFoodPrepTime();

        return new int[] { cageCleanTime, foodPrepTime, feedTime, feedWindow };
    }

}
