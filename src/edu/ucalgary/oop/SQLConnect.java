package edu.ucalgary.oop;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SQLConnect {
    /*
     * This class is used to connect to the SQL database
     * and add rows to the database
     */
    private static Connection zooConnect;
    private static ResultSet zooResult;
    private static int[] val = {0,0,0};

    /*
     * empty constructor
     */
    public SQLConnect(){
    }

    /**
     * Establishes a connection with SQL DataBase 
     * has no inputs and no return
     * @throws SQLException
     */
    public void createConnection(){
        try{
            zooConnect = DriverManager.getConnection("jdbc:mysql://localhost/EWR","oop", "password");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Adds a row to the specified table in the SQL database.
     * Throws an exception if any of the column values are invalid or if the row already exists.
     * Has no return value, but adds the row to the database.
     * @param tableName The name of the table to insert into.
     * @param columnNames The names of the columns to insert into.
     * @param columnValues The values to insert into the corresponding columns.
     * @throws SQLConnectionException
    */
    public static void addRow(String tableName, String[] columnNames, String[] columnValues) throws SQLConnectionException {
        SQLConnect sql = new SQLConnect();
        sql.createConnection(); //start a connection
        try {
            for (int i = 0; i < columnValues.length; i++) { // Check for invalid values and throw exception if a value is invalid
                if (columnValues[i] == null || columnValues[i].isEmpty()) {
                    throw new SQLConnectionException("Error in Data");
                }
            }
            if (!isDuplicate(tableName, columnNames, columnValues)) { // Check if row already exists if so skips it.
                switch(tableName) {
                    case "ANIMALS": // Increments the AnimalID for the next row
                        columnValues[0] = String.valueOf(++val[0]);
                        break;
                    case "TASKS": // Increments the TaskID for the next row
                        columnValues[0] = String.valueOf(++val[1]);
                        break;
                    case "TREATMENTS":  // Increments the TreatmentID for the next row
                        columnValues[0] = String.valueOf(++val[2]);
                        break;
                }

                Statement zooStatement = zooConnect.createStatement();
                zooStatement.executeUpdate("USE EWR");
                String columnList = String.join(", ", columnNames);
                String valueList = Arrays.stream(columnValues).map(val -> "'" + val + "'").collect(Collectors.joining(", "));
                String query = "INSERT INTO " + tableName + " (" + columnList + ") VALUES (" + valueList + ")";
                // above line is the same as: INSERT INTO tableName (column1, column2, column3) VALUES (value1, value2, value3)
                zooStatement.executeUpdate(query);
            }
        } catch (Exception e) {
            throw new SQLConnectionException("Error inserting into " + tableName.toUpperCase() + " DATABASE");
        }
        sql.close(); // close connection
    }  
    
    /**
     * gets the List of Animals from the database
     * throws an exception if the animalID is negative, the animalNick or animalSpecies is null or empty,
     * or if the animalID already exists
     * returns a Map of the animals in the database with the animalID as the key 
     * and the animalNick and animalSpecies as the value <AnimalID : [animalNickname, animalSpecies]>
     * @throws SQLException
     * @return AnimalsMap
     */
    public static Map<Integer, String[]> getAnimalsList() {
        SQLConnect sql = new SQLConnect();
        sql.createConnection(); //start a connection
        Map<Integer, String[]> AnimalsMap = new HashMap<>(); // create a map to store the animals
        try {
            Statement zooStatement = zooConnect.createStatement();
            zooResult = zooStatement.executeQuery("SELECT * FROM ANIMALS");
            while (zooResult.next()) {
                int AnimalID = zooResult.getInt("AnimalID"); // get the animalID
                String AnimalNickname = zooResult.getString("AnimalNickname"); // get the animalNickname
                String AnimalSpecies = zooResult.getString("AnimalSpecies"); // get the animalSpecies
                String[] taskValues = {AnimalNickname, AnimalSpecies}; // create an array to store the animalNickname and animalSpecies
                AnimalsMap.put(AnimalID, taskValues); // add the animalID and the array to the map
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        sql.close(); // close connection
        return AnimalsMap;
    }

    /**
     * gets the List of Tasks from the database
     * throws an exception if the taskID is negative, the description is null or empty,
     * the duration or maxWindow is negative, or if the taskID already exists
     * returns a Map of the tasks in the database with the taskID as the key 
     * and the description, duration, and maxWindow as the value <TaskID : [description, duration, maxWindow]>
     * @throws SQLException
     * @return tasksMap
     */
    public static Map<Integer, String[]> getTasksList(){
        SQLConnect sql = new SQLConnect();
        sql.createConnection(); //start a connection
        Map<Integer, String[]> tasksMap = new HashMap<>(); // create a map to store the tasks
        try {
            Statement zooStatement = zooConnect.createStatement();
            zooResult = zooStatement.executeQuery("SELECT * FROM TASKS"); 
            while (zooResult.next()) {
                int taskId = zooResult.getInt("TaskID"); // get the taskID
                String description = zooResult.getString("Description"); // get the description
                int duration = zooResult.getInt("Duration"); // get the duration
                int maxWindow = zooResult.getInt("MaxWindow"); // get the maxWindow
                String[] taskValues = {description, String.valueOf(duration), String.valueOf(maxWindow)}; // create an array to store the description, duration, and maxWindow
                tasksMap.put(taskId, taskValues); // add the taskID and the array to the map
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        sql.close(); // close connection
        return tasksMap;
    }

    /**
     * gets the List of Treatments from the database
     * throws an exception if the taskID, animalID, or startHour is negative, or if the treatment already exists
     * returns a List of the treatments in the database with the taskID, animalID, and startHour as the value 
     * <TreatmentID : [taskID, animalID, startHour]>
     * @throws SQLConnectionException
     * @return sqltreatment
     */
    public static Map<Integer, int[]> getTreatmentsList(){
        SQLConnect sql = new SQLConnect();
        sql.createConnection(); //start a connection
        Map<Integer, int[]> sqltreatment = new HashMap<>(); // create a map to store the treatments
        try {
            Statement zooStatement = zooConnect.createStatement();
            zooResult = zooStatement.executeQuery("SELECT * FROM TREATMENTS"); 
            while (zooResult.next()) {
                int TreatmentID = zooResult.getInt("TreatmentID"); // get the treatmentID
                int AnimalID = zooResult.getInt("AnimalID"); // get the animalID
                int TaskID = zooResult.getInt("TaskID"); // get the taskID
                int StartHour = zooResult.getInt("StartHour"); // get the startHour
                int[] taskValues = {TaskID, AnimalID, StartHour}; // create an array to store the taskID, animalID, and startHour
                sqltreatment.put(TreatmentID, taskValues); // add the treatmentID and the array to the map
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        sql.close(); // close connection
        return sqltreatment;
    }
    
    /**
     * Deletes a Row from the specified table.
     * @param tableName
     * @param IDColumn
     * @throws SQLConnectionException
     */
    public static void deleteRow(String tableName, String IDColumn) throws SQLConnectionException {
        SQLConnect sql = new SQLConnect();
        sql.createConnection(); //start a connection
        try {
            tableName = tableName.toUpperCase(); // make sure the table name is uppercase
            Statement zooStatement = zooConnect.createStatement(); // create a statement
            String query = "DELETE FROM " + tableName + " WHERE " + IDColumn + " > 0";  
            // the query above is basically "DELETE FROM TABLENAME WHERE IDCOLUMN > 0" essentially clearing out the database
            zooStatement.executeUpdate(query);
        } catch (Exception e) {
            throw new SQLConnectionException("Error deleting from " + tableName.toUpperCase() + " DATABASE"); 
        }
        sql.close(); // close connection
    }

    /**
     * Checks if a row with the given column values already exists in the specified table.
     * @param tableName The name of the table to check.
     * @param columnNames The names of the columns to check.
     * @param columnValues The values to check in the corresponding columns.
     * @return True if a row with the given column values already exists, false otherwise.
     * @throws SQLException
     */
    private static boolean isDuplicate(String tableName, String[] columnNames, String[] columnValues) throws SQLException {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < columnNames.length; i++) { // start at 1 to skip the ID column
            sb.append(columnNames[i] + "='" + columnValues[i] + "'"); // create the query
            if (i < columnNames.length - 1) { // if there are more columns to check, add an "AND" to the query
                sb.append(" AND "); 
            }
        }
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE " + sb.toString(); 
    // the query is basically "SELECT COUNT(*) FROM TABLENAME WHERE columnNames[1] = columnValues[1] AND ... AND columnNames[n] = columnValues[n]"
        Statement statement = zooConnect.createStatement();
        ResultSet rs = statement.executeQuery(query);
        rs.next(); // move to the next row
        return rs.getInt(1) > 0; 
    }

    /**
     * closes the connection with the SQL database
     * @throws SQLException
     */
    public void close() {
        try {
            if (zooResult != null) {
                zooResult.close();
            }
            if (zooConnect != null) {
                zooConnect.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}