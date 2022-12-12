package com.wolin.warehouseapp.utils.common;

import java.text.SimpleDateFormat;

public class TimeFormatter {

    public static String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        //default should never happen
        return "JAN";
    }

    public static String makeDateString(int day, int month, int year)
    {
        return TimeFormatter.getMonthFormat(month) + " " + day + " " + year;
    }

    public static SimpleDateFormat toDateFormat(String date) {
        //TODO
        return null;
    }
}
