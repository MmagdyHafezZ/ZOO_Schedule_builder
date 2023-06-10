package edu.ucalgary.oop;

public class TaskNotCorrectException extends Exception {

/**
 * Constructs a new TaskNotCorrectException with the specified detail message.
 *
 * @param message the detail message.
 */
    public TaskNotCorrectException(String message) {
        super(message);
    }
}
