package test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import src.service.Log;
import src.util.LocalDateTimeUtil;

public class Test {
    public static void main(String args[]) {
        System.out.println("java test");
        Log.info("java test", "test 1");

        // LocalDateTime currentDate = LocalDateTimeUtil.getCurrentDate();
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // System.out.println("currentD = " + currentDate.format(formatter));
    }
}

