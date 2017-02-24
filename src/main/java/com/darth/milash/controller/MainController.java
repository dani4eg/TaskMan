package com.darth.milash.controller;

import com.darth.milash.model.Task;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import com.darth.milash.MainApp;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainController {
    private static String formatDate = "dd MMM yyyy HH:mm:ss";
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
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitlePropetry());
        showPersonDetails(null);

        // Слушаем изменения выбора, и при изменении отображаем
        // дополнительную информацию об адресате.
        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
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
            // Если Person = null, то убираем весь текст.
            title.setText(task.getTitle());
            start.setText(sdf.format(task.getStartTime()));
            end.setText(sdf.format(task.getEndTime()));
            interval.setText(Integer.toString(task.getInterval()));
            if (task.isActive()) active.setText("YES");
            else active.setText("NO");
        }
    }
}
