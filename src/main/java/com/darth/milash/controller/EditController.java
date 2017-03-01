package com.darth.milash.controller;

import com.darth.milash.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditController {

    private static String formatDate = "dd.MM.yyyy HH:mm:ss";

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

    @FXML
    void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTask(Task task) {
        this.task = task;

        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
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
            interval.setText(Integer.toString(task.getInterval()/1000));
            if (task.isActive()) active.setText("YES");
            else active.setText("NO");
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        Date startDate;
        Date endDate;
        if (isInputValid()) {
            task.setTitle(title.getText());
            try {
                task.setInterval(Integer.parseInt(interval.getText())*1000);
                    try {
                        startDate=sdf.parse(start.getText());
                        endDate = sdf.parse(end.getText());
                        if (startDate.before(endDate)) {
                            task.setStart(startDate);
                            task.setEnd(endDate);
                            dialogStage.close();
                        }
                        else MyAlerts.timeDateAlert();
                    } catch (ParseException e1) {
                        MyAlerts.formatDateAlert();
                    }
                }
                catch (NumberFormatException n) {
                try {
                    task.setStart(sdf.parse(start.getText()));
                    dialogStage.close();
                } catch (ParseException e) {
                    MyAlerts.formatDateAlert();
                }
            }
        }
        if (active.getText().equals("YES") || active.getText().equals("yes") || active.getText().equals("y") || active.getText().equals("Y")) task.setActive(true);
        else task.setActive(false);

        okClicked = true;
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }


    private boolean isInputValid() {
        String errorMessage = "";

        if (title.getText() == null || title.getText().length() == 0) {
            errorMessage += "No valid task name!\n";
        }
        if (start.getText() == null || start.getText().length() == 0) {
            errorMessage += "Start date is empty\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
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