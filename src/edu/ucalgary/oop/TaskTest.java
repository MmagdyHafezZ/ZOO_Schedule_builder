package edu.ucalgary.oop;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TaskTest {
    private Task task;

    @Before
    public void setUp() throws TaskNotCorrectException {
        task = new Task(10, "Test task", 3, 30, "Test animal", "Test species", 60, 1);
    }

    @Test
    public void testTaskConstructor() {
        assertEquals(10, task.getStartHour());
        assertEquals("Test task", task.getDescription());
        assertEquals(3, task.getCount());
        assertEquals(30, task.getDuration());
        assertEquals("Test animal", task.getAnimalNick());
        assertEquals("Test species", task.getAnimalSpec());
        assertEquals(60, task.getMaxWindow());
        assertEquals(1, task.getAnimalID());
    }

    @Test
    public void testTaskConstructorWithInvalidDuration() {
        try {
            Task invalidTask = new Task(10, "Invalid task", 3, 70, "Invalid animal", "Invalid species", 60, 2);
            fail("TaskNotCorrectException not thrown for invalid duration");
        } catch (TaskNotCorrectException e) {
            assertEquals("Error in duration or Start Hour", e.getMessage());
        }
    }

    @Test
    public void testTaskConstructorWithInvalidStartHour() {
        try {
            Task invalidTask = new Task(25, "Invalid task", 3, 30, "Invalid animal", "Invalid species", 60, 2);
            fail("TaskNotCorrectException not thrown for invalid start hour");
        } catch (TaskNotCorrectException e) {
            assertEquals("Error in duration or Start Hour", e.getMessage());
        }
    }
    @Test
    public void testGetStartHour() {
        assertEquals(10, task.getStartHour());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test task", task.getDescription());
    }

    @Test
    public void testGetCount() {
        assertEquals(3, task.getCount());
    }

    @Test
    public void testGetDuration() {
        assertEquals(30, task.getDuration());
    }

    @Test
    public void testGetTimeAvailable() {
        assertEquals(30, task.getTimeAvailable());
    }

    @Test
    public void testGetTaskID() {
        assertEquals(12, task.getTaskID());
    }

    @Test
    public void testGetAnimalID() {

        assertEquals(1, task.getAnimalID());
    }

    @Test
    public void testSetStartHour() {
        task.setStartHour(12);
        assertEquals(12, task.getStartHour());
    }

    @Test
    public void testSetDescription() {
        task.setDescription("Updated task description");
        assertEquals("Updated task description", task.getDescription());
    }

    @Test
    public void testSetCount() {
        task.setCount(5);
        assertEquals(5, task.getCount());
    }

    @Test
    public void testSetDuration() {
        task.setDuration(45);
        assertEquals(45, task.getDuration());
    }

    @Test
    public void testSetTimeAvailable() {
        task.setTimeAvailble(60);
        assertEquals(60, task.getTimeAvailable());
    }

    @Test
    public void testSetTaskID() {
        task.setTaskID(4);
        assertEquals(4, task.getTaskID());
    }

    @Test
    public void testSetAnimalID() {
        task.setAnimalID(6);
        assertEquals(6, task.getAnimalID());

    }
    @Test
    public void testSetTreatmentID() {
        task.setTreatmentID(1);
        assertEquals(1, task.getTreatmentID());
    }

    @Test
    public void testSetVolunteerRequiredBoolean() {
        task.setVolunteerRequired(true);
        assertTrue(task.getVolunteerRequired());
    }

    @Test
    public void testSetVolunteerRequiredStringTrue() {
        task.setVolunteerRequired("true");
        assertTrue(task.getVolunteerRequired());
    }

    @Test
    public void testSetVolunteerRequiredStringFalse() {
        task.setVolunteerRequired("false");
        assertFalse(task.getVolunteerRequired());
    }

    @Test
    public void testGetAnimalIDFromNickMatch() {
        int animalID = Task.getAnimalIDFromNick("nick");
        assertEquals(-1, animalID);
    }

    @Test
    public void testGetAnimalIDFromNickNoMatch() {
        int animalID = Task.getAnimalIDFromNick("unknown");
        assertEquals(-1, animalID);
    }

    @Test
    public void testCalcTimeAvailable() {
        int available = Task.calcTimeAvailable(30, task);
        assertEquals(30, available);
    }

    @Test
    public void testCalcTimeAvailableVolunteerRequired() {
        task.setStartHour(8);
        int available = Task.calcTimeAvailable(90, task);
        assertEquals(-30, available);
        assertTrue(task.getVolunteerRequired());
    }

    @Test
    public void testCalcVolunteerTimeAvailable() {
        int available = Task.calcVolunteerTimeAvailable(30, task);
        assertEquals(90, available);
    }
    
    






}
