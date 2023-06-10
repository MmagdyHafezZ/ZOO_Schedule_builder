package edu.ucalgary.oop;

public class SQLConnectionException extends Exception {

    /** 
     * Constructor for creating a new SQLConnectionException object.
     * @param message The error message associated with the exception.
     * Initializes the exception's message using the "super()" method inherited from the parent "Exception" class.
     */
    public SQLConnectionException(String message) {
        super(message);
    }
}