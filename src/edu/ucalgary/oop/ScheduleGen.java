package edu.ucalgary.oop;

import java.util.*;

public class ScheduleGen {
    /*
     * This class is the class that will generate the schedule for the day
     * It will take the information from the database and create the tasks
     * It will also check if there are any conflicts in the schedule
     */

    private static Map<Integer, String[]> animalsMap = SQLConnect.getAnimalsList(); // key: animalID, value: [nickname, species]
    private static Map<Integer, String[]> tasksMap = SQLConnect.getTasksList(); // key: taskID, value: [description, duration, maxWindow]
    private static Map<Integer, int[]> treatmentMap = SQLConnect.getTreatmentsList(); // key: treatmentID, value: [taskID, animalID, startHour]
    protected final static List<Task> completeList = new ArrayList<>(); // list of all the tasks in the database

    /**
     * This method is the method that connects all of the different pieces of the task generation together
     * 
     * @throws TaskNotCorrectException
     * @return completeList
     */
    public static List<Task> completeList() throws TaskNotCorrectException {
        createTasksFromDB(); // this method creates the tasks from the database
        feedingList(); // this method creates the tasks for feeding the animals
        cleaningAndPrepList(); // this method creates the tasks for cleaning the cages and preparing the food
        checkVolunteers(); // this method checks if there is a task with volunteer 
                           //turned on and if there is, it will turn the rest of the hour into volunteer mode
        for(Task task : completeList){
            if(task.getVolunteerRequired()){ // checks if there is a volunteer for any of the tasks, if there is 
                if(task.getMaxWindow() > 1){  // a function is called that will have a last chance at causing no time conflicts
                    forcefullRelocation(task);
                }
            }
        }
        checkVolunteers();
        return completeList;
    }

    /**
     * this method is used to create the tasks from the database and add them to the completeList
     * it also sorts the list based on the start hour of the task
     * @throws TaskNotCorrectException
     */
    public static void createTasksFromDB() throws TaskNotCorrectException {
        Set<String> check = new HashSet<>();
        for (int i : treatmentMap.keySet()) { // loop over all the treatments
            int dup = 1;

            int taskID = treatmentMap.get(i)[0];
            int animalID = treatmentMap.get(i)[1];
            int startHour = treatmentMap.get(i)[2];
            String description = tasksMap.get(taskID)[0];

            for (int j : treatmentMap.keySet()) { // check for duplicates
                if (i != j && treatmentMap.get(i)[2] == treatmentMap.get(j)[2]
                        && treatmentMap.get(i)[0] == treatmentMap.get(j)[0]) {
                    dup++;
                }
            }
            if((check.contains(String.valueOf(taskID) + String.valueOf(startHour)))){
                dup = 0;
            }
            int temp = startingHour(Integer.parseInt(tasksMap.get(taskID)[1]), startHour, // get the startingHour for the task
                                    Integer.parseInt(tasksMap.get(taskID)[2]));
            
            Task task = new Task(i, taskID, animalID, startHour, description, dup, //create an object with a temp start hour
                    Integer.parseInt(tasksMap.get(taskID)[1]),animalsMap.get(animalID)[0],
                    animalsMap.get(animalID)[1], Integer.parseInt(tasksMap.get(taskID)[2]));
            if(temp == -1){
                task.setStartHour(startHour);
                task.setVolunteerRequired(true);
            }else{
                task.setStartHour(temp);
                task.setVolunteerRequired(false); // set the volunteer required to false if the task is not a volunteer task
            }
            check.add(String.valueOf(taskID) + String.valueOf(startHour));
            task.setDuration(task.getDuration()*task.getCount());
            task.setTimeAvailble(Task.calcTimeAvailable(task.getDuration(), task));
            completeList.add(task); 
            
        }
        Collections.sort(completeList, new Comparator<Task>() { // sort the list based on the start hour
            @Override
            public int compare(Task task1, Task task2) {
                return Integer.compare(task1.getStartHour(), task2.getStartHour());
            }
        });
    }

    /**
     * this method is used to create tasks of feeding the animals
     * @throws TaskNotCorrectException
     */
    public static void feedingList() throws TaskNotCorrectException{
        for(int id: animalsMap.keySet()){   // iterate over all animals
            if(animalsMap.get(id)[0].equals("Annie, Oliver and Mowgli") ){// kittens are excluded from the feeding list
                continue;
            }
            int[] details = Animal.getAnimalDetails(animalsMap.get(id)[1]); // Call animal and get the details [cageCleanTime, 
                                                                            //foodPrepTime, feedTime, feedWindow]

            for(int i = 0; i < details.length; i++){ // make sure values are correct
                if(details[i] > 60 || details[i] < 0 ){
                    throw new TaskNotCorrectException("Task details are not correct");
                }
            }

            int feedHour = startingHour(details[2], details[3], 3); // calculate the starting hour for the feeding task

            Task hourFeed = new Task(0, "Feeding", 1, details[2], // create the task for the feeding of an animal
            animalsMap.get(id)[0], animalsMap.get(id)[1], 3, id);

            hourFeed.setStartHour(feedHour); // set the start hour for the task
            if( feedHour == -1){ 
                hourFeed.setStartHour(details[3]);
                hourFeed.setVolunteerRequired(true);
            }
            hourFeed.setTimeAvailble(Task.calcTimeAvailable(details[2], hourFeed)); // calculate the time available for the task
            completeList.add(hourFeed);

            Collections.sort(completeList, new Comparator<Task>() { // sort the list based on the start hour
                @Override
                public int compare(Task task1, Task task2) {
                    return Integer.compare(task1.getStartHour(), task2.getStartHour());
                }
            });
        }
        
    }

    /**
     * this method is used to create tasks of cleaning the cages and preparing the food
     * @throws TaskNotCorrectException
     */
    public static void cleaningAndPrepList() throws TaskNotCorrectException{
        for(int id: animalsMap.keySet()){   // iterate over all animals in the database
            int[] details = Animal.getAnimalDetails(animalsMap.get(id)[1]); // call animal and get the details
            for(int i = 0; i < details.length; i++){ // make sure values are correct
                if(details[i] > 60 || details[i] < 0 ){
                    throw new TaskNotCorrectException("Task details are not correct");
                }
            }
            int cleanHour = startingHour(details[0], 0, 24); // calculate the starting hour (it can be at any time of the day)
            if(cleanHour != -1){
                Task hourClean = new Task(cleanHour, "Cleaning", 1, details[0],  // add the cleaning to the list of tasks
                animalsMap.get(id)[0], animalsMap.get(id)[1], 0, id);
                completeList.add(hourClean);
            } else{
                Task hourClean = new Task(0, "Cleaning", 1, details[0], 
                animalsMap.get(id)[0], animalsMap.get(id)[1], 0, id);
                hourClean.setVolunteerRequired(true);
                completeList.add(hourClean);
            }
            if(!animalsMap.get(id)[0].equals("Annie, Oliver and Mowgli") ){ // kittens are excluded from the food preparation list
                int prepHour = startingHour(details[1], 0, 24);
                if(prepHour != -1){
                    Task hourPrep = new Task(prepHour, "Food Preparation", 1, details[1], 
                    animalsMap.get(id)[0], animalsMap.get(id)[1], 0, id);
                    completeList.add(hourPrep);
                }else{
                    Task hourPrep = new Task(0, "Food Preparation", 1, details[1],  // add the food preparation to the list of tasks
                    animalsMap.get(id)[0], animalsMap.get(id)[1], 0, id);
                    hourPrep.setVolunteerRequired(true);
                    completeList.add(hourPrep);
                }
            }
        }
        Collections.sort(completeList, new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                return Integer.compare(task1.getStartHour(), task2.getStartHour());
            }
        });
    }

    /**
     * this method is a last resort to relocate a task, it works by checking any task that can be moved backwards
     * @param task
     * @throws TaskNotCorrectException
     */
    public static void forcefullRelocation(Task task){
        int temp2 = task.getStartHour();  // store the start hour of the task
        for(int i = task.getStartHour() - 1; i > task.getStartHour() - task.getMaxWindow(); i--){ // iterate over the possible hours in descending order
            for(int j = 0; j < completeList.size(); j++){ // iterate over the list of tasks at that time
                if(completeList.get(j).getStartHour() == i){  // look for the tasks that have the same start hour only
                    if(completeList.get(j).getTimeAvailable() - task.getDuration() >= 0){ // check if the task can be moved if not will look for another task
                        task.setStartHour(i);  // if a spot was found, the start hour is set
                        task.setTimeAvailble(Task.calcTimeAvailable(0, task)); // time available is calculated
                        task.setVolunteerRequired(false); // the task is no longer required a volunteer
                        for(Task temp : completeList){ // iterate over the list of tasks in the last start hour
                            if(temp.getStartHour() == temp2){
                                temp.setVolunteerRequired(false); // set the volunteer required to false
                                task.setVolunteerRequired(false); // set the volunteer required to false
                            }
                            
                        }
                        return; // if no change can be made do nothing
                    }
                    
                }
            }
        }
    }

    /**
     * this method is used to check if there is a volunteer 
     * and set all of the volunteers in the same hour to true
     */
    public static void checkVolunteers() {
        for (int i = 0; i < 24; i++) { // loop over all hours of day
            int normDur = 60;
            int availble = 120;
            boolean volunteerFound = false;
            for (Task task : completeList) { // loop over all tasks
                if (task.getStartHour() == i) { // check if the task is in the same hour
                    normDur -= task.getDuration(); // calculate the time available
                    if (task.getVolunteerRequired() || normDur < 0) { // check if the task requires a volunteer
                        volunteerFound = true;
                        break;
                    }
                }
            }
            if (volunteerFound) { // if a volunteer is found set all tasks in the same hour to true
                for (Task task : completeList) {
                    if (task.getStartHour() == i) {
                        task.setVolunteerRequired(true);
                    }
                }
            }
            for(Task temp : completeList){ // set volunteer mode on
                if(temp.getVolunteerRequired()){
                    if(temp.getStartHour() == i){
                        temp.setTimeAvailble(availble -= temp.getDuration());
                    }
                }
            }
        }

    }

    /**
     * this method is used to update the database with the new schedule it deletes the database
     * and then adds the new schedule to the database with the correct values.
     * @param taskList
     * @throws TaskNotCorrectException,SQLConnectionException
     */
    public static void updateDataBase(List<Task> taskList) throws TaskNotCorrectException, SQLConnectionException {
        SQLConnect.deleteRow("ANIMALS", "AnimalID");
        SQLConnect.deleteRow("TASKS", "TaskID");
        SQLConnect.deleteRow("TREATMENTS", "TreatmentID"); // at this point the database is empty
        int treatID = 0;

        for(Task task : taskList){ // set the duration of the task if it is 0 to the duration in the database
            if(task.getDuration() == 0){
                task.setDuration(Integer.parseInt(tasksMap.get(task.getTaskID())[1]));
            }

        }

        for(int animalID: animalsMap.keySet()) { // add the animals to the database
            SQLConnect.addRow("ANIMALS", new String[] { "AnimalID", "AnimalNickname", "AnimalSpecies" },
            new String[] { " ", animalsMap.get(animalID)[0], animalsMap.get(animalID)[1] });
        }

        for (Task task : taskList) { // add the tasks to the database based on the list recieved from GUI
            task.setTreatmentID(++treatID);
            SQLConnect.addRow("TASKS", new String[] { "TaskID", "Description", "Duration", "MaxWindow" },
                new String[] { " ", task.getDescription(), String.valueOf(task.getDuration()),
                        String.valueOf(task.getMaxWindow()) });

            SQLConnect.addRow("TREATMENTS", new String[] { "TreatmentID", "AnimalID", "TaskID", "StartHour" },
                    new String[] { String.valueOf(task.getTreatmentID()), String.valueOf(task.getAnimalID()), String.valueOf(task.getTaskID()), 
                            String.valueOf(task.getStartHour()) });

        }
        return;
    }   

    /**
     * This function will return the correct start hour for the cleaning times
     * it iterates over all 24 hours of the day and should be able to calculate the correct start hour
     * @param dur
     * @return StartHour
     */
    public static int startingHour(int dur, int start, int maxWindow) {
        for (int i = start; i < start + maxWindow; i++) { // iterate over all 24 hours or check the hours after the start hour specified

            int temp = 0, min_ava = 60;
            boolean check = true;
            if (!contains(i)) {
                return i;
            }
            
            for(Task task : completeList){ // the number of tasks with the same start hour and get least time availble
                if(task.getStartHour() == i){

                    min_ava = task.getTimeAvailable();
                    temp++;
                    
                }
            }
            if(temp > 4){    // if there are more than 4 tasks in the same hour go to a different start hour
                check = false;
            }
            if(check){
                if(min_ava - dur >= 0){ // if the least time availble is less than 0 go to a different starting hour
                    return i;
                }
            }
        }
        for(int i = start; start != 0 && i > start - maxWindow ; i--){  // same as function above but checks the hours before the start hour specified
            int temp = 0, min_ava = 60;
            boolean check = true;
            if (!contains(i)) {
                return i;
            }
            for(Task task : completeList){
                if(task.getStartHour() == i){
                    if (min_ava > task.getTimeAvailable()){
                        min_ava = task.getTimeAvailable();
                        temp++;
                    }
                }
            }

            if(temp > 4){
                check = false;
            }
            if(check){
                if(min_ava - dur >= 0){
                    return i;
                }
            }   
        }
        return -1; 
    }

    /**
     * This function is used to check if there is a task in the same hour
     * @param i
     * @return boolean
     */
    public static boolean contains(int i){
        for(int j = 0; j < completeList.size(); j++){
            if(completeList.get(j).getStartHour() == i){
                return true;
            }
        }
        return false;
    }
}
