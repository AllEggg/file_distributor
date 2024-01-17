package ru.oleaghue.file_distributor.util;

import java.util.Calendar;

public class DateFormatter {
    private String year;
    private String month;
    private String day;
    private String hours;

    public DateFormatter(String date) {
        String[] separateDateKey = date.split("_");
        this.year = separateDateKey[0];
        this.month = getMonthName(separateDateKey[1]);
        this.day = separateDateKey[2];
        this.hours = getHourName(separateDateKey[3], separateDateKey[4]);
    }

    private static String getMonthName(String defaultValue) {
        switch (defaultValue) {
            case ("0") -> {
                return "Январь";
            }
            case ("1") -> {
                return "Февраль";
            }
            case ("2") -> {
                return "Март";
            }
            case ("3") -> {
                return "Апрель";
            }
            case ("4") -> {
                return "Май";
            }
            case ("5") -> {
                return "Июнь";
            }
            case ("6") -> {
                return "Июль";
            }
            case ("7") -> {
                return "Август";
            }
            case ("8") -> {
                return "Сентябрь";
            }
            case ("9") -> {
                return "Октябрь";
            }
            case ("10") -> {
                return "Ноябрь";
            }
            case ("11") -> {
                return "Декабрь";
            }
            default -> {
                return "Неизвестно";
            }
        }
    }

    public static String getHourName(String dayPart, String hour) {
        if (Integer.parseInt(dayPart) == Calendar.AM) {
            return hour + "-" + (Integer.parseInt(hour) + 1);
        } else if (Integer.parseInt(dayPart) == Calendar.PM) {
            int firstHour = Integer.parseInt(hour) + 12;
            if (firstHour == 23) {
                return "23-00";
            } else {
                return firstHour + "-" + (firstHour + 1);
            }
        } else {
            return "Unknown";
        }
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
