package com.darth.milash;


import com.darth.milash.controller.MainController;
import com.darth.milash.model.ArrayTaskList;
import com.darth.milash.model.Task;
import com.darth.milash.model.TaskIO;
import com.darth.milash.model.TaskList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class MainApp extends Application {

    private static TaskList list = new ArrayTaskList();
    private static String fileName = "files/tFile.txt";
    private static String formatDate = "dd.MM.yyyy HH.mm.ss";
    private Stage primaryStage;
    private BorderPane rootLayout;
    private ObservableList<Task> taskData = FXCollections.observableArrayList();

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("TaskMan");

        initRootWindowt();

        initTaskWindow();
    }

    /**
     * Инициализирует корневой макет.
     */
    public void initRootWindowt() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/RootWindow.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показывает в корневом макете сведения об адресатах.
     */
    public void initTaskWindow() {
        try {
            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/TaskWindow.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            // Помещаем сведения об адресатах в центр корневого макета.
            rootLayout.setCenter(personOverview);

            // Даём контроллеру доступ к главному приложению.
            MainController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает главную сцену.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public ObservableList<Task> getPersonData() {
        return taskData;
    }

    public MainApp() throws FileNotFoundException, ParseException {
        TaskIO.read(list, new FileReader(fileName));
        for (int i = 0; i < list.size(); i++) {
            taskData.add(list.getTask(i));
        }



    }
}