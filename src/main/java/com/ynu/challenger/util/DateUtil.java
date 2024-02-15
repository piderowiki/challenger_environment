package com.ynu.challenger.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
    public static int getThisYear(){
        LocalDate date = LocalDate.now();
        return date.getYear();
    }
    public static int getThisMonth(){
        LocalDate date = LocalDate.now();
        return date.getMonthValue();
    }
    public static int getThisWeek(){
        LocalDate date = LocalDate.now();
        DayOfWeek week = date.getDayOfWeek();
        return week.getValue();
    }
    public static int getThisDay(){
        LocalDate date = LocalDate.now();
        return date.getDayOfMonth();
    }

    // 下面的是用来处理Date的年
    public static int getDateYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    // Calendar系列居然都是从0开始的
    public static int getDateMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDateDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getNowHour(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        return hour;
    }

    public static int getDateDayOfWeek(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfWeek(int year,int month,int day){
        LocalDate date = LocalDate.of(year,month,day);
        return date.getDayOfWeek().getValue();
    }

    public static int inSevenDay(int year,int month,int day){
        LocalDate date = LocalDate.now();
        LocalDate dateWeekBefore = LocalDate.now();
        LocalDate checkDate = LocalDate.of(year,month,day);
        dateWeekBefore.minusWeeks(1);

        // 在本周内的话
        if(checkDate.isAfter(dateWeekBefore) && checkDate.isBefore(date)){
            return checkDate.getDayOfWeek().getValue();
        }else return 7;
     }

    public static LocalDate getDayBeforeXday(int year,int month,int day,int X){
        return LocalDate.of(year,month,day).minusDays(X);
     }

    public static boolean isFuture(int year,int month,int day){
        LocalDate date = LocalDate.now();
        LocalDate checkDate = LocalDate.of(year,month,day);

        return checkDate.isAfter(date);
    }

    public static String printLocalDate(LocalDate date){
        return date.getYear() + ":" + date.getMonthValue() +":" +date.getDayOfMonth();
    }

    public static boolean isToday(int year, int month, int day) {
        LocalDate date = LocalDate.now();
        return (year == date.getYear() &&
                month == date.getMonthValue() &&
                day == date.getDayOfMonth());
    }

    public static boolean isThisMonth(int year, int month) {
        LocalDate date = LocalDate.now();
        return (year == date.getYear() &&
                month == date.getMonthValue());
    }

    public static int getDateHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    public static int getMaxMonthDay(int year, int month) {
        LocalDate localDate = LocalDate.of(year,month,1);
        int daysInMonth = localDate.lengthOfMonth();
        return daysInMonth;
    }
}
