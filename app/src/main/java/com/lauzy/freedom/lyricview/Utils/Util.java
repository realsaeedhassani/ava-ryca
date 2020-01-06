package com.lauzy.freedom.lyricview.Utils;

import java.util.Random;

public class Util {

    public static Integer getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }
}
