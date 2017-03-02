package com.darth.milash.controller;

import com.darth.milash.MainApp;
import com.darth.milash.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by darthMilash on 28.02.2017.
 */
public class CalendarController {

    private static String formatDate = "dd.MM.yyyy HH:mm:ss";
    private static String fileName = "files/tFile.txt";
    private static TaskList list = new ArrayTaskList();
    private Stage dialogStage;
    private boolean okClicked = false;



    @FXML
    private TableView<Map.Entry<Date, Set<Task>>> calendar;


    @FXML
    private TableColumn<Map.Entry<Date, Set<Task>>, String> dateColumn;



    @FXML
    private TableColumn<Map.Entry<Date, Set<Task>>, String> tasksColumn;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() throws IOException, ParseException {
        TaskIO.read(list, new FileReader(fileName));
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        dialogStage.close();
    }

    public void calendar() throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
        long date;
        Date sdate = new Date();
        Date edate = new Date(sdate.getTime() + (66400000));
        Map<Date, Set<Task>> map = Tasks.calendar(list, sdate, edate);

        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Date, Set<Task>>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Date, Set<Task>>, String> param) {
                return new SimpleStringProperty(sdf.format(param.getValue().getKey()));
            }
        });

        tasksColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Date, Set<Task>>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Date, Set<Task>>, String> param) {
                return new SimpleStringProperty(param.getValue().getValue().toString());
            }
        });

       ObservableList<Map.Entry<Date, Set<Task>>> mapList = FXCollections.observableArrayList(map.entrySet());
        System.out.println(mapList);
        calendar = new TableView<>(mapList);
        System.out.println(dateColumn.getColumns());

    }

}
