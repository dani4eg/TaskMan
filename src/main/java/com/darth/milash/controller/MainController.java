package com.darth.milash.controller;

import com.darth.milash.model.ArrayTaskList;
import com.darth.milash.model.Task;
import com.darth.milash.model.TaskIO;
import com.darth.milash.model.TaskList;
import com.darth.milash.util.DateUtil;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import com.darth.milash.MainApp;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainController {
    private static String formatDate = "dd.MM.yyyy HH:mm:ss";
    private static String fileName = "files/tFile.txt";
    private static TaskList list = new ArrayTaskList();

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> titleColumn;
//
//
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

    // Ссылка на главное приложение.
    private MainApp mainApp;
//
//    /**
//     * Конструктор.
//     * Конструктор вызывается раньше метода initialize().
//     */
//    public MainController() {
//    }
//
    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() throws FileNotFoundException, ParseException {
        // Инициализация таблицы адресатов с двумя столбцами.
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitlePropetry());
        showPersonDetails(null);

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об адресате.
        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
        TaskIO.read(list, new FileReader(fileName));
    }
//
    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Добавление в таблицу данных из наблюдаемого списка
        taskTable.setItems(mainApp.getPersonData());
    }

    public void showPersonDetails(Task task) {
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
            interval.setText(task.reInterval(task.getInterval()*1000));
            if (task.isActive()) active.setText("YES");
            else active.setText("NO");
        }
    }

    @FXML
    private void deleteTask() throws FileNotFoundException, ParseException {
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            taskTable.getItems().remove(selectedIndex);
            list.remove(list.getTask(selectedIndex));
            TaskIO.writeText(list, new File(fileName));
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Task Selected");
            alert.setContentText("Please select a task in the table.");
            alert.showAndWait();
        }
    }

//    @FXML
//    private void handleNewPerson() {
//        Task tempTask = new Task();
//        boolean okClicked = mainApp.showPersonEditDialog(tempTask);
//        if (okClicked) {
//            mainApp.getPersonData().add(tempTask);
//        }
//    }

    @FXML
    private void handleEditPerson() {
        Task selectedPerson = taskTable.getSelectionModel().getSelectedItem();
        int selectedIndex = taskTable.getSelectionModel().getSelectedIndex();
        if (selectedPerson != null) {

            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }
            list.getTask(selectedIndex).setTitle(selectedPerson.getTitle());
            list.getTask(selectedIndex).setStart(selectedPerson.getStartTime());
            list.getTask(selectedIndex).setEnd(selectedPerson.getEndTime());
            list.getTask(selectedIndex).setInterval(selectedPerson.getInterval()*1000);
            list.getTask(selectedIndex).setActive(selectedPerson.isActive());
            taskTable.getItems().set(selectedIndex, selectedPerson);
            TaskIO.writeText(list, new File(fileName));



        } else {
            // Ничего не выбрано.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");

            alert.showAndWait();
        }
    }
}
