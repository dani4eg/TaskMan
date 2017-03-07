package com.darth.milash.controller;

import com.darth.milash.model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.darth.milash.MainApp;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainController {
    private static String formatDate = "dd.MM.yyyy HH:mm:ss";
    private static String fileName = "files/tFile.txt";
    private static TaskList list;

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private Label title;
    @FXML
    private Label start;
    @FXML
    private Label end;
    @FXML
    private Label interval;
    @FXML
    private Label active;

    private MainApp mainApp;

    Thread thread;

    @FXML
    public void initialize() throws FileNotFoundException, ParseException {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        showTaskDetails(null);

        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTaskDetails(newValue));

        list = new ArrayTaskList();
        TaskIO.read(list, new FileReader(fileName));
        gogo();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        taskTable.setItems(mainApp.getTaskData());
    }

    public void showTaskDetails(Task task) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        if (task==null) {
            title.setText("");
            start.setText("");
            end.setText("");
            interval.setText("");
            active.setText("");
        }
        else if (task.getInterval()==0){
            title.setText(task.getTitle());
            start.setText(sdf.format(task.getTime()));
            end.setText("");
            interval.setText("");
            if (task.isActive()) active.setText("YES");
            else
            active.setText("NO");
        }
        else {
            title.setText(task.getTitle());
            start.setText(sdf.format(task.getStartTime()));
            end.setText(sdf.format(task.getEndTime()));
            interval.setText(task.reInterval(task.getInterval()));
            if (task.isActive()) active.setText("YES");
            else active.setText("NO");
        }
    }

    @FXML
    public void deleteTask() throws FileNotFoundException, ParseException {
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            taskTable.getItems().remove(selectedIndex);
            list.remove(list.getTask(selectedIndex));
            TaskIO.writeText(list, new File(fileName));
        }
        else {
            MyAlerts.chooseAlert(mainApp);
        }
    }

    @FXML
    public void handleNewTask() {
        Task tempTask = new Task("", new Date());
        boolean okClicked = mainApp.showTaskEditDialog(tempTask);
        if (okClicked) {
            mainApp.getTaskData().add(tempTask);
            tempTask.setTitle(tempTask.getTitle());
            tempTask.setStart(tempTask.getStartTime());
            tempTask.setEnd(tempTask.getEndTime());
            tempTask.setInterval(tempTask.getInterval());
            tempTask.setActive(tempTask.isActive());
            list.add(tempTask);
            TaskIO.writeText(list, new File(fileName));
        }
    }

    @FXML
    public void handleEditTask() {
        Task selectedPerson = taskTable.getSelectionModel().getSelectedItem();
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedPerson == null) {
            MyAlerts.chooseAlert(mainApp);
        } else {
            boolean okClicked = mainApp.showTaskEditDialog(selectedPerson);
            if (okClicked) {
                showTaskDetails(selectedPerson);
            }
            list.getTask(selectedIndex).setTitle(selectedPerson.getTitle());
            list.getTask(selectedIndex).setStart(selectedPerson.getStartTime());
            list.getTask(selectedIndex).setEnd(selectedPerson.getEndTime());
            list.getTask(selectedIndex).setInterval(selectedPerson.getInterval());
            list.getTask(selectedIndex).setActive(selectedPerson.isActive());
            taskTable.getItems().set(selectedIndex, selectedPerson);
            TaskIO.writeText(list, new File(fileName));

        }
    }

    @FXML
    public void handleCloneTask() {
        Task selectedPerson = taskTable.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            MyAlerts.chooseAlert(mainApp);
        } else {
            Task tempTask = new Task("", new Date());
            mainApp.getTaskData().add(tempTask);
            tempTask.setTitle(selectedPerson.getTitle()+"_copy");
            tempTask.setStart(selectedPerson.getStartTime());
            tempTask.setEnd(selectedPerson.getEndTime());
            tempTask.setInterval(selectedPerson.getInterval());
            tempTask.setActive(selectedPerson.isActive());
            list.add(tempTask);
            TaskIO.writeText(list, new File(fileName));

        }
    }

    @FXML
    public void handleCalendar() {
        mainApp.showCalendarWindow();
            }

    public void gogo() {
         thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String formatDate = "dd.MM.yyyy HH.mm.ss";
                long date;
                Date sdate = new Date();
                Date edate = new Date(sdate.getTime() + (66400000));
                Map<Date, Set<Task>> map = Tasks.calendar(list, sdate, edate);
                for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
                    date = pair.getKey().getTime() - (sdate.getTime());
                    sdate = pair.getKey();
                    System.out.println("Near task done after " + date / 1000 + " sec.");
                    try {
                        Thread.sleep(date);

                        for (Task task : pair.getValue()) {
                            System.out.println("DING DING.......The " + task.getTitle() + " is done.");
                            Platform.runLater(() -> {
                                mainApp.showAlarmWindow(pair.getKey(), task.getTitle());
                            });
                        }
                    } catch (InterruptedException e) {
                        mainApp.logger.error("Error");
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
