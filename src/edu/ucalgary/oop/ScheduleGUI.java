package edu.ucalgary.oop;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*; 
import java.awt.event.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*; 
import java.util.List; 
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.*;


public class ScheduleGUI {
    /*
     * This class is the GUI for the Schedule class. It is responsible for
     * displaying the GUI and handling user input.
     */
    private JFrame frame;
    private JLabel titleLabel;
    private JCheckBox confirmCheckBox;
    private JLabel shiftLabel;
    private JTable shiftTable;
    private JButton submitButton;
    private Map<String, String> animalMap = new HashMap<>();
    private List<String> taskListT = new ArrayList<>();
    private static List<Task> task;
    private static List<Task> completeList = new ArrayList<>();
    private int addCount = 0;

    public ScheduleGUI() {
        createUI();
    }
    public static DefaultTableModel removeDuplicates(String[] columnNames, Object[][] data) {
        List<Object[]> rows = new ArrayList<Object[]>(Arrays.asList(data));
        Set<List<Object>> uniqueRows = new HashSet<List<Object>>();
        List<Integer> duplicateIndices = new ArrayList<Integer>();
    
        for (int i = 0; i < rows.size(); i++) {
            List<Object> row = Arrays.asList(rows.get(i));
            if (uniqueRows.contains(row)) {
                duplicateIndices.add(i);
            } else {
                uniqueRows.add(row);
            }
        }
    
        if (duplicateIndices.isEmpty()) {
            return new DefaultTableModel(data, columnNames);
        } else {
            Object[][] newData = new Object[rows.size() - duplicateIndices.size()][8];
            int offset = 0;
    
            for (int i = 0; i < rows.size(); i++) {
                if (duplicateIndices.contains(i)) {
                    offset++;
                } else {
                    newData[i - offset] = rows.get(i);
                }
            }
    
            return new DefaultTableModel(newData, columnNames);
        }
    }
    
    private void createUI() {
        
        // Create the main window
        frame = new JFrame("Schedule Creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Create the title label
        titleLabel = new JLabel("Schedule Creator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        // Create the shift table and label
        shiftLabel = new JLabel("Select medical events to shift if schedule cannot be created:");

        String[] columnNames = {"Time", "Task", "Animal Name", "Time Spent", "Time Available", "Volunteer required","Qty", "Animal Species" }; // these are just examples
        Object[][] data = new Object[task.size()][8];
        for (int i = 0; i < task.size(); i++) {
            Object[] row = new Object[8];
            int startHour = task.get(i).getStartHour();
            int hour12 = startHour % 12;
            if (hour12 == 0) {
                hour12 = 12;
            }
            String time = String.format("%d:00 %s", hour12, startHour < 12 ? "AM" : "PM");
            row[0] = time;
            row[1] = task.get(i).getDescription();
            row[2] = task.get(i).getAnimalNick();
            row[3] = task.get(i).getDuration();
            row[4] = task.get(i).getTimeAvailable();
            row[5] = task.get(i).getVolunteerRequired();
            row[6] = task.get(i).getCount();
            row[7] = task.get(i).getAnimalSpec();
            data[i] = row;
        };
        // Add animal names and species to the map
        animalMap.put("Loner", "coyote");
        animalMap.put("Biter","coyote");
        animalMap.put("Pencil","coyote");
        animalMap.put("Bitter","coyote");
        animalMap.put("Eraser","coyote");
        animalMap.put("Annie, Oliver and Mowgli","fox");
        animalMap.put("Slinky","fox");
        animalMap.put("Spike","porcupine");
        animalMap.put("Javelin","porcupine");
        animalMap.put("Gatekeeper","porcupine");
        animalMap.put("Sunshine","porcupine");
        animalMap.put("Shadow","porcupine");
        animalMap.put("Boots","coyote");
        animalMap.put("Spin","coyote");
        animalMap.put("Spot","coyote");
        // Add tasks to the list
        taskListT.add("Kit feeding");
        taskListT.add("Rebandage leg wound");
        taskListT.add("Apply burn ointment back");
        taskListT.add("Administer antibiotics");
        taskListT.add("Flush neck wound");
        taskListT.add("Give fluid injection");
        taskListT.add("Give vitamin injection");
        taskListT.add("Mange treatment");
        taskListT.add("Eyedrops");
        taskListT.add("Inspect broken leg");
        

        
        DefaultTableModel model = removeDuplicates(columnNames, data);
        // Set preferred column width for each column
        shiftTable = new JTable(model);
        shiftTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        shiftTable.setBackground(Color.WHITE);
        JScrollPane shiftScrollPane = new JScrollPane(shiftTable);
        JPanel shiftPanel = new JPanel(new BorderLayout());
        shiftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        shiftPanel.add(shiftLabel, BorderLayout.NORTH);
        shiftPanel.add(shiftScrollPane, BorderLayout.CENTER);
        shiftScrollPane.setPreferredSize(new Dimension(1170, 500)); // set the preferred size of the scroll pane to a larger size
        TableColumnModel columnModel = shiftTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(260);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(120);
        columnModel.getColumn(6).setPreferredWidth(50);
        columnModel.getColumn(7).setPreferredWidth(70);
        // Add drag-and-drop functionality to the table
        shiftTable.setDragEnabled(true);
        shiftTable.setDropMode(DropMode.INSERT_ROWS);
        shiftTable.setTransferHandler(new TransferHandler() {
            public int getSourceActions(JComponent c) {
                return TransferHandler.COPY_OR_MOVE;
            }

            public Transferable createTransferable(JComponent c) {
                JTable table = (JTable) c;
                int[] rows = table.getSelectedRows();
                List<String> selectedEvents = new ArrayList<>();
                for (int row : rows) {
                    String event = (String) table.getValueAt(row, 1); // get the event description
                    selectedEvents.add(event);
                }
                String data = String.join(",", selectedEvents);
                return new StringSelection(data);
            }
            
        });
        // Create a TableCellEditor for column 2 and 7
        DefaultCellEditor comboBoxEditor = new DefaultCellEditor(new JComboBox());
        DefaultCellEditor comboBoxEditor7 = new DefaultCellEditor(new JComboBox()){
            @Override
            public boolean isCellEditable(EventObject anEvent) {
                return false; // Make column 7 not editable
            }
        };
        shiftTable.getColumnModel().getColumn(2).setCellEditor(comboBoxEditor);
        // Create a JComboBox for column 2 and 7
        JComboBox<String> comboBox2 = new JComboBox<String>();
        JComboBox<String> comboBox7 = new JComboBox<String>();
        JComboBox<String> comboBox1 = new JComboBox<String>();

        // Populate the JComboBox with names from animalMap for column 2
        for (String key : animalMap.keySet()) {
            comboBox2.addItem(key);
        }
        for (String task : taskListT) {
            comboBox1.addItem(task.toString()); // or task.getTaskName() depending on your Task class implementation
        }
        
        // Create a custom TableCellEditor for column 2
        TableColumn column2 = shiftTable.getColumnModel().getColumn(2);
        column2.setCellEditor(new DefaultCellEditor(comboBox2));
        // Create a custom TableCellEditor for column 1
        TableColumn column1 = shiftTable.getColumnModel().getColumn(1);
        column1.setCellEditor(new DefaultCellEditor(comboBox1));


        // Add action listener to comboBox2 to update comboBox7 with corresponding values
        comboBox2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedName = (String) comboBox2.getSelectedItem();
                String selectedTask = (String) comboBox1.getSelectedItem();
                String selectedAnimalSpecies = animalMap.get(selectedName);
                comboBox7.setSelectedItem(selectedAnimalSpecies);
                
                // Update the value in column 7
                int selectedRow = shiftTable.getSelectedRow();
                if (selectedRow >= 0) {
                    shiftTable.setValueAt(selectedAnimalSpecies, selectedRow, 7);
                    shiftTable.setValueAt(selectedTask, selectedRow, 1);
                }
            }
        });
        shiftTable.getColumnModel().getColumn(7).setCellEditor(comboBoxEditor7);
        shiftTable.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    JTable table = (JTable) evt.getDropTargetContext().getComponent();
                    Transferable transferable = evt.getTransferable();
                    String data = (String) transferable.getTransferData(DataFlavor.stringFlavor);
        
                    // Split the data and get the selected events
                    String[] events = data.split(",");
                    List<String> selectedEvents = new ArrayList<>();
                    for (String event : events) {
                        selectedEvents.add(event.trim());
                    }
        
                    // Get the drop location and row index
                    JTable.DropLocation dropLocation = table.getDropLocation();
                    int rowIndex = dropLocation.getRow();
        
                    // Insert the selected events into the table
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    for (String event : selectedEvents) {
                        Object[] rowData = {model.getRowCount() + 1, event, "-", "-"};
                        model.insertRow(rowIndex, rowData);
                        rowIndex++;
                    }
                    evt.dropComplete(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    evt.rejectDrop();
                }
            }
        });
        frame.add(shiftPanel, BorderLayout.WEST);
        // Create the add row buttons
        JButton addRowButton = new JButton("Add Row");
        addRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) shiftTable.getModel();
                    Object[] rowData = {"12:00 AM", "-", "-", "1", "1", "false", "1","-"};
                    model.addRow(rowData);
                    try {
                        task.add(new Task(0, "-", 1, 0,"-","-",0, 1));
                        addCount++;

                    } catch (TaskNotCorrectException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        // Create the remove row button
        JButton removeRowButton = new JButton("Remove Row");
        removeRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) shiftTable.getModel();
                int[] rows = shiftTable.getSelectedRows();
                for (int i = rows.length - 1; i >= 0; i--) {
                    int row = rows[i];
                    model.removeRow(row);
                    task.remove(row);
                }
                
            }
        });
        // Create the print button
        JButton printButton = new JButton("Print Schedule");
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the current date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = new Date();
                String dateStr = dateFormat.format(currentDate);
        
                // Get the current schedule from the table
                StringBuilder sb = new StringBuilder();
                sb.append("Schedule for ").append(dateStr).append("\n\n");
                TableModel model = shiftTable.getModel();
                String prevTime = "";
                for (int row = 0; row < model.getRowCount(); row++) {
                    String time = model.getValueAt(row, 0).toString();
                    String tasks = model.getValueAt(row, 1).toString();
                    String animalInfo = model.getValueAt(row, 2).toString();
                    boolean isBackupVolunteerNeeded = Boolean.parseBoolean(model.getValueAt(row, 5).toString());
                    
                    // Format time as "hh:mm"
                    String formattedTime = time;
                    
                    if (prevTime.equals(formattedTime)) {
                        sb.append("*").append(tasks).append(" (").append(animalInfo).append(")");
                        if (isBackupVolunteerNeeded) {
                            sb.append(" [+ backup volunteer]");
                        }
                        sb.append("\n");
                    } else {
                        if (!prevTime.isEmpty()) {
                            sb.append("\n"); // Add new line after last line of grouped time
                        }
                        sb.append(formattedTime).append("\n").append(tasks).append(" (").append(animalInfo).append(")");
                        if (isBackupVolunteerNeeded) {
                            sb.append(" [+ backup volunteer]");
                        }
                        sb.append("\n");
                    }
                    
                    prevTime = formattedTime;
                }
                // Save the schedule to a file
                try {
                    File file = new File("schedule.txt");
                    FileWriter writer = new FileWriter(file);
                    writer.write(sb.toString());
                    writer.close();
                    JOptionPane.showMessageDialog(frame, "Schedule saved to schedule.txt");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Create the confirm checkbox and label
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JLabel confirmLabel = new JLabel("Confirm The Need For Additional Volunteers");
        confirmCheckBox = new JCheckBox();
        buttonPanel.add(confirmLabel);
        buttonPanel.add(confirmCheckBox);
        buttonPanel.add(addRowButton);
        buttonPanel.add(removeRowButton);
        buttonPanel.add(printButton);
        shiftPanel.add(buttonPanel, BorderLayout.SOUTH);
        // Create the submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (confirmCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(frame, "You have confirmed the need for additional volunteers.");
            }
            // Retrieve the data from the table
            DefaultTableModel model = (DefaultTableModel) shiftTable.getModel();
            int rowCount = model.getRowCount();
            Object[][] data = new Object[rowCount][8];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < 8; j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        if (j == 3 || j == 4 || j == 6) {
                            data[i][j] = Integer.parseInt(value.toString());
                        } else if (j == 5) {
                            data[i][j] = Boolean.parseBoolean(value.toString());
                        } else {
                            data[i][j] = value.toString();
                        }
                    } else {
                        data[i][j] = null;
                    }
                }
            }
            // Check time format for each row
            String timeRegex = "(0?[1-9]|1[0-2]):[0-5][0-9] [APM]{2}";
            for (int i = 0; i < rowCount; i++) {
                Object timeObj = model.getValueAt(i, 0);
                String time = null;
                if (timeObj instanceof String) {
                    time = (String) timeObj;
                } else if (timeObj instanceof Integer) {
                    time = Integer.toString((int) timeObj);
                }
                if (!time.matches(timeRegex)) {
                    JOptionPane.showMessageDialog(frame, "Invalid time format in row " + (i + 1));
                    return;
                }
                // if animal is not in animal list 
                try{
                    if (!animalMap.containsKey(data[i][2])){
                        throw new Exception();
                    }
                } catch (Exception e1){
                    JOptionPane.showMessageDialog(frame, "Invalid animal at time " + time);
                    return;
                }
                String animalName = data[i][2].toString();
                String animalSpecies = animalMap.get(animalName);
                if (!animalSpecies.equals(data[i][7])) {
                    JOptionPane.showMessageDialog(frame, "Incorrect animal species at time " + time);
                    return;
                }
                // if task is null
                for (int j = 0; j < 8; j++) {
                    if (data[i][j].equals("")) {
                        JOptionPane.showMessageDialog(frame, "Please fill in all the fields in row " + (i + 1));
                        return;
                    }
                    if (data[i][j] == null) {
                        JOptionPane.showMessageDialog(frame, "Please fill in all the fields in row " + (i + 1));
                        return;
                    } 
                }
            }
            model.fireTableDataChanged();
            // Create a new window to display the sorted data
            JFrame resultFrame = new JFrame("Final Schedule");
            resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            resultFrame.setResizable(true);
    
            // Create the table to display the sorted data
            JTable resultTable = new JTable(data, columnNames);
            resultTable.setBackground(Color.WHITE);
            JScrollPane resultScrollPane = new JScrollPane(resultTable);
            JPanel resultPanel = new JPanel(new BorderLayout());
            resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            resultPanel.add(resultScrollPane, BorderLayout.CENTER);
            resultFrame.add(resultPanel);
            ArrayList<String> additionalVolunteerList = new ArrayList<>();
            //Update the task list    
            for (int i = 0; i < rowCount; i++) {
                Object[] row = data[i];
                Task currentTask = task.get(i);
                String[] temp = new String[row.length];
                for (int j = 0; j < row.length; j++) {
                    if (row[j] != null) {
                        temp[j] = row[j].toString();
                    }
                }
                if (temp[1].equals("-")){
                    continue;
                }
                
                String check = currentTask.getDescription().toLowerCase().replace(" ", "");
                if(check.equals("cleaning") || check.equals("feeding") || check.equals("foodpreparation")){
                    continue;
                }
                // Convert 12-hour format back to 24-hour format
                String time = temp[0]; // Assuming the time is stored as a string in the format "hh:mm AM/PM"
                String[] parts = time.split(":");
                int hour = Integer.parseInt(parts[0]);
                String amPm = parts[1].substring(3);
                if (hour == 12 && amPm.equalsIgnoreCase("AM")) {
                    hour = 0;
                } else if (hour != 12 && amPm.equalsIgnoreCase("PM")) {
                    hour += 12;
                }
                if (hour == 24) {
                    hour = 0;
                }
                if (addCount > 0){
                    int animalID =Task.getAnimalIDFromNick(temp[2]);
                    currentTask.setAnimalID(animalID);
                }
                int startHour = hour; // Set the hour directly as 24-hour format
                currentTask.setStartHour(startHour);
                currentTask.setDescription(temp[1]);
                currentTask.setAnimalNick(temp[2]);
                currentTask.setTimeAvailble(Integer.parseInt(temp[4]));
                currentTask.setVolunteerRequired(temp[5]);
                currentTask.setCount(Integer.parseInt(temp[6]));
                currentTask.setAnimalSpec(temp[7]);
                // Calculate the duration of the task
                if(currentTask.getCount() == 0){
                    currentTask.setDuration((Integer.parseInt(temp[3])));
                }else{
                    currentTask.setDuration((Integer.parseInt(temp[3]))/currentTask.getCount());
                }

                if ("true".equals(temp[5])) { 
                    // Add the description and animal nickname to the list
                    String message = "Task: " + currentTask.getDescription() + " for Animal: " + currentTask.getAnimalNick();
                    additionalVolunteerList.add(message);
                }
            
                // Apply regex check to AnimalNick field
                String animalNick = currentTask.getAnimalNick();
                if (animalNick != null && !animalNick.isEmpty()) {
                    String regex = "^(\\S+)\\s+(\\S+\\s\\S+)$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(animalNick);
                    if (matcher.matches()) {
                        String newAnimalNick = matcher.group(1) + " " + matcher.group(2);
                        currentTask.setAnimalNick(newAnimalNick);
                    }
                }
            
                if (currentTask.getDescription() != null && !currentTask.getDescription().isEmpty() && currentTask.getCount() >= 0 ) {
                    completeList.add(currentTask);
                } else {
                    JOptionPane.showMessageDialog(frame, "This task "+ currentTask.getDescription()  +" is not valid " + currentTask.getDescription());
                    model.removeRow(rowCount - 1);
                    return;
                }
            }
            if (!additionalVolunteerList.isEmpty()) {
                String message = "Additional volunteers are needed for the following tasks:\n";
                for (String taskInfo : additionalVolunteerList) {
                    message += "- " + taskInfo + "\n";
                }
                JOptionPane.showMessageDialog(frame, message);
            }
            //sort the complete list based on the start time
            Collections.sort(completeList, new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getStartHour() - o2.getStartHour();
                }
            });                
            try {
                ScheduleGen.updateDataBase(completeList);
            } catch (TaskNotCorrectException | SQLConnectionException e1) {
                e1.printStackTrace();
            }
            

            // Add sorting functionality to the table
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(resultTable.getModel());
            resultTable.setRowSorter(sorter);
            Comparator<Integer> intComparator = new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            };
            Comparator<String> stringComparator = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            };
            Comparator<Boolean> booleanComparator = new Comparator<Boolean>() {
                @Override
                public int compare(Boolean o1, Boolean o2) {
                    return o1.compareTo(o2);
                }
            };
            Comparator<String> timeComparator = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
                        Date time1 = format.parse(o1);
                        Date time2 = format.parse(o2);
                        return time1.compareTo(time2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            };
            sorter.setComparator(0, timeComparator);
            sorter.setComparator(1, stringComparator);
            sorter.setComparator(2, stringComparator);
            sorter.setComparator(3, intComparator);
            sorter.setComparator(4, intComparator);
            sorter.setComparator(5, booleanComparator);
            sorter.setComparator(6, intComparator);
            resultTable.setRowSorter(sorter);
            resultFrame.pack();
            resultFrame.setSize(1170,800);
            resultFrame.setLocationRelativeTo(null);
            resultFrame.setVisible(true);
                
        }
    });
            
        frame.add(submitButton, BorderLayout.SOUTH);
        // Resize and show the window
        frame.pack();
        frame.setSize(1200,800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    public static void main(String[] args) throws NumberFormatException, SQLConnectionException {
        connectCreateTasksDB();
        new ScheduleGUI();
    }
    public static void connectCreateTasksDB() throws NumberFormatException, SQLConnectionException{
        try {
            task = ScheduleGen.completeList();
        } catch (TaskNotCorrectException e) {
            e.printStackTrace();
        }

    }
}
