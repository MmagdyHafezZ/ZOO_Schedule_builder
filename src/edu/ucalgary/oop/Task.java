package edu.ucalgary.oop;

import java.util.*;

/**
 * The Task class represents a task that needs to be done at a specific time by a volunteer.
 * It stores information such as the start hour, description, count, duration, time available,
 * task ID, animal ID, treatment ID, animal nickname, animal species, maximum window, and whether
 * a volunteer is required. It also has methods to get and set these values, as well as a method to 
 * calculate the time available for the task based on the start hour and duration. The class also
 * contains two constructors for creating new Task objects, with one constructor requiring more parameters
 * than the other. Finally, the class has a private static Map for storing information about the animals,
 * tasks, and treatments in the system, as well as sets for storing information about the tasks, animals,
 * and treatments that have already been created.
 */
public class Task {
    // Static Maps and Sets for storing information about the animals, tasks, and treatments in the system
    private static Map<Integer, String[]> animalsMap = SQLConnect.getAnimalsList();
    private static Map<Integer, String[]> tasksMap = SQLConnect.getTasksList();
    private static Map<Integer, int[]> treatmentMap = SQLConnect.getTreatmentsList();
    private static Set<Integer> createdTaskIds = new HashSet<>();
    private static Set<Integer> createdAnimalIds = new HashSet<>();
    private static Set<Integer> createdTreatmentIds = new HashSet<>();

    // Instance variables for the Task object
    private int startHour;
    private String description;
    private int count;
    private int duration;
    private int timeAvailble;
    private int taskID;
    private int animalID;
    private int treatmentID;
    private String animalNick;
    private String animalSpec;
    private int MaxWindow;
    private boolean VolunteerRequired = false;

    /**
     * Constructor for creating a new Task object with the given parameters
     * @param startHour the start hour of the task
     * @param description a description of the task
     * @param count the number of times the task needs to be done
     * @param duration the duration of each instance of the task in minutes
     * @param animalNick the nickname of the animal the task is for
     * @param animalSpec the species of the animal the task is for
     * @param MaxWindow the maximum number of minutes the task can be done after the start time
     * @param animalID the ID of the animal the task is for
     * @throws TaskNotCorrectException if the duration or start hour are invalid
     */
    public Task(int startHour, String description, int count, int duration, String animalNick,
            String animalSpec, int MaxWindow, int animalID) throws TaskNotCorrectException {
        if (duration > 60 || duration < 0 || startHour > 23 || startHour < 0) {
            throw new TaskNotCorrectException("Error in duration or Start Hour");
        }
        this.startHour = startHour;
        this.description = description;
        this.count = count;
        this.duration = duration;
        this.timeAvailble = calcTimeAvailable(duration, this);
        this.animalNick = animalNick;
        this.animalSpec = animalSpec;
        this.MaxWindow = MaxWindow;
        this.taskID = tasksMap.size() + 1;
        this.animalID = animalID;
        this.treatmentID = treatmentMap.size() + 1;
    }

    /**
    Task class represents a single task that needs to be performed on an animal as part of a treatment.
    Each Task object stores various attributes such as treatmentID, taskID, animalID, startHour, description, count, duration,
    animalNick, animalSpec, MaxWindow, and VolunteerRequired. It also provides methods to get and set these attributes.
    @param treatmentID - The ID of the treatment this task belongs to.
    @param taskID - The ID of the task.
    @param animalID - The ID of the animal this task needs to be performed on.
    @param startHour - The hour of the day (0-23) when this task needs to be performed.
    @param description - A description of the task.
    @param count - The number of times this task needs to be performed.
    @param duration - The duration of each instance of this task in minutes.
    @param animalNick - A nickname for the animal.
    @param animalSpec - The species of the animal.
    @param MaxWindow - The maximum time window in minutes for this task.
    @throws TaskNotCorrectException if duration or startHour are not within their allowed range.
    */
    public Task(int treatmentID, int taskID, int animalID, int startHour, String description, int count, int duration,
            String animalNick, String animalSpec, int MaxWindow) throws TaskNotCorrectException {
        if (duration > 60 || duration < 0 || startHour > 23 || startHour < 0) {
            throw new TaskNotCorrectException("Error in duration or Start Hour");
        }
        this.startHour = startHour;
        this.description = description;
        this.count = count;
        this.duration = duration;
        this.timeAvailble = calcTimeAvailable(duration, this);
        this.taskID = taskID;
        this.animalID = animalID;
        this.animalNick = animalNick;
        this.animalSpec = animalSpec;
        this.MaxWindow = MaxWindow;
        this.treatmentID = treatmentID;
        createdTaskIds.add(taskID);
        createdAnimalIds.add(animalID);
        createdTreatmentIds.add(treatmentID);
    }

    /**
     * Start Hour Getter
     * 
     * @return startHour
     */
    public int getStartHour() {
        return startHour;
    }

    /**
    Gets the description of this task.
    @return description - A description of the task.
    */
    public String getDescription() {
        return description;
    }

    /**
    Gets the number of times this task needs to be performed.
    @return count - The count of the task.
    */
    public int getCount() {
        return count;
    }

    /**
    Gets the duration of this task.
    @return duration - The duration of each instance of this task in minutes.
    */
    public int getDuration() {
        return duration;
    }

    /**
    Gets the time available for this task.
    @return timeAvailble - The time available for this task in minutes.
    */
    public int getTimeAvailable() {
        return timeAvailble;
    }

    /**
    Gets the ID of this task.
    @return taskID - The ID of the task.
    */
    public int getTaskID() {
        return taskID;
    }

    /**
    Gets the ID of the animal this task needs to be performed on.
    @return animalID - The ID of the animal.
    */
    public int getAnimalID() {
        return animalID;
    }

    /**
    Returns the ID of the treatment required for this task.
    @return the treatment ID
    */
    public int getTreatmentID() {
        return treatmentID;
    }

    /**
    Returns the maximum window of time for completion of this task.
    @return the maximum window in minutes
    */
    public int getMaxWindow() {
        return MaxWindow;
    }

    /**
    Returns the nickname of the animal for which this task is required.
    @return the animal nickname
    */
    public String getAnimalNick() {
        return animalNick;
    }

    /**
    Returns the species of the animal for which this task is required.
    @return the animal species
    */
    public String getAnimalSpec() {
        return animalSpec;
    }

    /**
    Returns a flag indicating if volunteers are required for this task.
    @return true if volunteers are required, false otherwise
    */
    public boolean getVolunteerRequired() {
        return VolunteerRequired;
    }

    /**
    Sets the start hour for this task.
    @param startHour the start hour in 24-hour format
    */
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }


    /**
    Sets the description for this task.
    @param description the description of the task
    */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
    Sets the count for this task.
    @param count the count of the task
    */
    public void setCount(int count) {
        this.count = count;
    }


    /**
    Sets the duration for this task.
    @param duration the duration in minutes
    */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
    Sets the nickname of the animal for which this task is required.
    @param animalNick the animal nickname
    */
    public void setAnimalNick(String animalNick) {
        this.animalNick = animalNick;
    }

    /**
    Sets the species of the animal for which this task is required.
    @param animalSpec the animal species
    */
    public void setAnimalSpec(String animalSpec) {
        this.animalSpec = animalSpec;
    }

    /**
    Sets the maximum window of time for completion of this task.
    @param MaxWindow the maximum window in minutes
    */
    public void setMaxWindow(int MaxWindow) {
        this.MaxWindow = MaxWindow;
    }

    /**
    Sets the time available for completion of this task.
    @param timeAvailble the time available in minutes
    */
    public void setTimeAvailble(int timeAvailble) {
        this.timeAvailble = timeAvailble;
    }

    /**
    Sets the ID of this task.
    @param taskID the task ID
    */
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    /**
    Sets the ID of the animal for which this task is required.
    @param animalID the animal ID
    */
    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    /**
    Sets the treatment ID of the task.
    @param treatmentID an integer representing the treatment ID of the task.
    */
    public void setTreatmentID(int treatmentID) {
        this.treatmentID = treatmentID;
    }

    /**
    Sets the volunteer requirement of the task.
    @param VolunteerRequired a boolean representing whether volunteers are required for the task.
    */
    public void setVolunteerRequired(boolean VolunteerRequired) {
        this.VolunteerRequired = VolunteerRequired;
    }

    /**
    Sets the volunteer requirement of the task based on a string input.
    @param VolunteerRequired a string representation of whether volunteers are required for the task.
    If the string equals "true", sets VolunteerRequired to true. Otherwise, sets VolunteerRequired to false.
    */
    public void setVolunteerRequired(String VolunteerRequired) {
        if (VolunteerRequired.equals("true")) {
            this.VolunteerRequired = true;
        } else {
            this.VolunteerRequired = false;
        }
    }
    public static int getAnimalIDFromNick(String nick){
        for(Integer animalID : animalsMap.keySet()){
            if(animalsMap.get(animalID)[0].equals(nick)){
                return animalID;
            }
        }
        return -1; // Return -1 if no match is found
    }
    
    /**
    Calculates the time available for a task to be completed.
    @param dur an integer representing the duration of the task.
    @param task a Task object representing the task being considered.
    @return an integer representing the time available for the task to be completed.
    If the time available is negative, sets the task's volunteer requirement to true and returns the maximum time the task can take.
    */
    public static int calcTimeAvailable(int dur, Task task) {
        int available = 60;
        for (Task t : ScheduleGen.completeList) {
            if (t.getStartHour() == task.getStartHour()) {
                available -= t.getDuration();
            }
        }
        available -= dur;
        if (available < 0) {
            task.setVolunteerRequired(true);
            return 60 - dur;
        }
        return available;
    }

    /**
    Calculates the time available for a volunteer to complete a task.
    @param dur an integer representing the duration of the task.
    @param task a Task object representing the task being considered.
    @return an integer representing the time available for a volunteer to complete the task.
    */
    public static int calcVolunteerTimeAvailable(int dur, Task task) {
        int available = 120;
        for (Task t : ScheduleGen.completeList) {
            if (t.getStartHour() == task.getStartHour()) {
                available -= t.getDuration();
            }
        }
        available -= dur;
        return available;
    }
}