package src.util;

import java.util.Random;

public class IntegerUtil {
    public static int random(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}