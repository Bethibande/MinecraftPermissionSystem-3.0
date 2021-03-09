package de.bethibande.bperms.utils;

import java.util.Calendar;

public class TimeUtils {

    public static long getTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long getTimeInSeconds() {
        return Calendar.getInstance().getTimeInMillis()/1000;
    }

}
