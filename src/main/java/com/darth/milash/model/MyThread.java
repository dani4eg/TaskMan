package com.darth.milash.model;

import com.darth.milash.MainApp;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by darthMilash on 22.02.2017.
 */
public class MyThread extends Thread{


    private MainApp mainApp;

//    @Override
//    public void run() {
//        while (true) {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Platform.runLater(() -> {
//                mainApp.showAlarmWindow();
//            });
//        }
//    }
//});

    @Override
    public void run() {
        {
            Logger logger = LoggerFactory.getLogger(Tasks.class);
            String formatDate = "dd.MM.yyyy HH.mm.ss";
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate, Locale.ENGLISH);
            TaskList list = new ArrayTaskList();
            String fileName = "files/tFile.txt";
            try {
                TaskIO.read(list, new FileReader(fileName));
            } catch (ParseException e) {
                logger.error("Parse Error");
            } catch (FileNotFoundException e) {
                logger.error("File not found");
            }
            long date;
            Date sdate = new Date();
            Date edate = new Date(sdate.getTime() + (66400000));
            Map<Date, Set<Task>> map = Tasks.calendar(list, sdate, edate);
            for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
                date = pair.getKey().getTime() - (sdate.getTime());
                sdate = pair.getKey();
                System.out.println("Near task done after " + date/1000 + " sec.");
                try {
                    Thread.sleep(date);
                    Platform.runLater(() -> {
                                mainApp.showAlarmWindow();
                            });

                    System.out.println(sdf.format(pair.getKey()));
                    for (Task task : pair.getValue()) {

                        logger.info("The " + task.getTitle() + " is done.");
                        System.out.println("DING DING.......The " + task.getTitle() + " is done.");

                    }
                } catch (InterruptedException e) {
                    logger.error("Blocked");
                }

            }
        }

    }
}
