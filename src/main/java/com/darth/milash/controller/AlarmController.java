package com.darth.milash.controller;

import com.darth.milash.model.ArrayTaskList;
import com.darth.milash.model.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * Created by darthMilash on 05.03.2017.
 */
public class AlarmController {

    @FXML
    private Label alarmTask;
    @FXML
    private Label alarmDate;

    private boolean okClicked = false;
    private static String formatDate = "dd.MM.yyyy HH:mm:ss";
    private Task task;
    private Stage dialogStage;




    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    public void handleOk() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void alarmTask() {
//        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
//        ArrayTaskList list = new ArrayTaskList();
////        for (Task task : set) {
////            this.task = task;
////            list.add(task);
////        }
//        String txt="";
////        for (int i = 0; i < list.size(); i++) {
////            txt+=list.getTask(i).getTitle();
////        }
//        alarmTask.setText(txt);
//
//        alarmDate.setText("lala");

    }
}
