package com.wolin.warehouseapp.utils.common;

import java.time.LocalDate;

public class TimeFormatter {

    public static String makeDateString(int day, int month, int year)
    {
        String dayF = String.format("%02d", day);
        String monthF = String.format("%02d", month);
        String yearF = String.format("%04d", year);
        String dateStr = dayF + "/" + monthF + "/" + yearF;
        return dateStr;
    }
}
