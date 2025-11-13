package com.invadermonky.eternallyoverburdened.utils.helpers;

import java.util.Calendar;

@SuppressWarnings("unused")
public class DateHelper {
    static Calendar curTime = Calendar.getInstance();
    static Calendar holidayStart = Calendar.getInstance();
    static Calendar holidayEnd = Calendar.getInstance();

    public static boolean isNewYears() {
        setDate(holidayStart, Calendar.DECEMBER, 30, false);
        setDate(holidayEnd, Calendar.JANUARY, 2, true);
        return dateCheck();
    }

    public static boolean isAprilFools() {
        setDate(holidayStart, Calendar.MARCH, 31, false);
        setDate(holidayEnd, Calendar.APRIL, 2, true);
        return dateCheck();
    }

    public static boolean isHalloween() {
        setDate(holidayStart, Calendar.OCTOBER, 29, false);
        setDate(holidayEnd, Calendar.NOVEMBER, 1, true);
        return dateCheck();
    }

    public static boolean isThanksgiving() {
        setDate(holidayStart, Calendar.NOVEMBER, 24, false);
        setDate(holidayEnd, Calendar.NOVEMBER, 30, true);
        return dateCheck();
    }

    public static boolean isChristmas() {
        setDate(holidayStart, Calendar.DECEMBER, 24, false);
        setDate(holidayEnd, Calendar.DECEMBER, 26, true);
        return dateCheck();
    }

    static void setDate(Calendar cal, int month, int date, boolean endOfDay) {
        cal.clear();

        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, date);

        if(endOfDay) {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
        } else {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        }
    }

    static boolean dateCheck() {
        curTime = Calendar.getInstance();
        return curTime.after(holidayStart) && curTime.before(holidayEnd);
    }
}
