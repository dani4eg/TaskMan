package com.darth.milash.controller;

import com.darth.milash.MainApp;
import com.darth.milash.model.ArrayTaskList;
import com.darth.milash.model.Task;
import com.darth.milash.model.TaskIO;
import com.darth.milash.model.TaskList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by darthMilash on 24.02.2017.
 */
public class EditController {

    private static String formatDate = "dd.MM.yyyy HH:mm:ss";
    private static String fileName = "files/tFile.txt";
    private static TaskList list = new ArrayTaskList();
    /**
     * Окно для изменения информации об адресате.
     *
     * @author Marco Jakob
     */
    @FXML
    private TextField title;
    @FXML
    private TextField start;
    @FXML
    private TextField end;
    @FXML
    private TextField interval;
    @FXML
    private TextField active;

    private Stage dialogStage;
    private Task task;
    private boolean okClicked = false;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() throws FileNotFoundException, ParseException {
        TaskIO.read(list, new FileReader(fileName));
    }

    /**
     * Устанавливает сцену для этого окна.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Задаёт адресата, информацию о котором будем менять.
     *
     * @param task
     */
    public void setPerson(Task task) {
        this.task = task;

        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
//            title.setText(task.getTitle());
//            start.setText(task.ge);
//            streetField.setText(person.getStreet());
//            postalCodeField.setText(Integer.toString(person.getPostalCode()));
//            cityField.setText(person.getCity());
//            birthdayField.setText(DateUtil.format(person.getBirthday()));
//            birthdayField.setPromptText("dd.mm.yyyy");
        if (task.getInterval() == 0) {
            title.setText(task.getTitle());
            start.setText(sdf.format(task.getTime()));
            end.setText("");
            interval.setText("");
            if (task.isActive()) active.setText("YES");
            else
                active.setText("NO");
        } else {
            title.setText(task.getTitle());
            start.setText(sdf.format(task.getStartTime()));
            end.setText(sdf.format(task.getEndTime()));
            interval.setText(Integer.toString(task.getInterval()));
            if (task.isActive()) active.setText("YES");
            else active.setText("NO");
        }
    }

    /**
     * Returns true, если пользователь кликнул OK, в другом случае false.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке OK.
     */
    @FXML
    private void handleOk() {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        if (isInputValid()) {
            task.setTitle(title.getText());
            try {
                task.setStart(sdf.parse(start.getText()));
            } catch (ParseException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incorrect Date");
                alert.setHeaderText("Date is incorrect");
                alert.setContentText("No valid date. Use the format dd.MM.yyyy HH:mm:ss!");
                alert.showAndWait();
            }

            try {
                task.setInterval(Integer.parseInt(interval.getText()));
                try {
                    task.setEnd(sdf.parse(end.getText()));
                    dialogStage.close();
                } catch (ParseException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Incorrect Date");
                    alert.setHeaderText("Date is incorrect");
                    alert.setContentText("No valid date. Use the format dd.MM.yyyy HH:mm:ss!");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                dialogStage.close();
            }
        }


        if (active.getText().equals("YES") || active.getText().equals("yes") || active.getText().equals("y") || active.getText().equals("Y")) task.setActive(true);
        else task.setActive(false);

//        for (int i = 0; i < list.size() ; i++) {
//            if (list.getTask(i).equals(task)) {
//                list.getTask(i).setTitle("HUY");
//            }
//            TaskIO.writeText(list, new File(fileName));
//        }

        okClicked = true;
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Проверяет пользовательский ввод в текстовых полях.
     *
     * @return true, если пользовательский ввод корректен
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (title.getText() == null || title.getText().length() == 0) {
            errorMessage += "No valid task name!\n";
        }

//        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
//            errorMessage += "No valid birthday!\n";
//        } else {
//            if (!DateUtil.validDate(birthdayField.getText())) {
//                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
//            }
//        }
        if (start.getText() == null || start.getText().length() == 0) {
            errorMessage += "Start date is empty\n";
        }

//        if (end.getText() != null) {
//            if (interval.getText() == null || interval.getText().length() == 0) {
//                errorMessage += "No valid interval\n";
//            } else {
//                try {
//                    Integer.parseInt(interval.getText());
//                } catch (NumberFormatException e) {
//                    errorMessage += "No valid interval (must be an integer)!\n";
//                }
//            }
//        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Показываем сообщение об ошибке.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
}