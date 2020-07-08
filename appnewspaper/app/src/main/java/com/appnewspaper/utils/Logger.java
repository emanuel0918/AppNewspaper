package com.appnewspaper.utils;


import android.util.Log;

import java.util.Date;

public class Logger {
    public static final int ERROR = -1;
    public static final int INFO = 1;

    public static int level = INFO;
    public final static String TAG = "Logger";

    public static final void log(int level, String message){
        if (level<=Logger.level){
            switch(level){
                case ERROR:
                    Log.e(TAG, message);
                    break;
                case INFO:
                    Log.i(TAG, message);
                    break;
            }

        }
    }

    private static String printCurrentDate() {
        return SerializationUtils.dateToString(new Date());
    }
}
