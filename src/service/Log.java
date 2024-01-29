package src.service;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import src.util.LocalDateTimeUtil;

public class Log {
    private static LocalDateTime currentLocalDateTime = LocalDateTimeUtil.getCurrentDate();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static String currentDateTime = currentLocalDateTime.format(formatter);

    private static void all(String info) {
        try {
            FileWriter fw = new FileWriter("src/log/all.log", true);
            fw.write(info + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String methodName, String description) {
        try {
            FileWriter fw = new FileWriter("src/log/info.log", true);

            String content = currentDateTime + " - INFO - " + methodName + " - " + description;

            fw.write(content + "\n");
            fw.close();

            all(content);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void error(String methodName, String description) {
        try {
            FileWriter fw = new FileWriter("src/log/error.log", true);
            
            String content = currentDateTime + " - ERROR - " + methodName + " - " + description;
            
            fw.write(content + "\n");
            fw.close();

            all(content);
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
