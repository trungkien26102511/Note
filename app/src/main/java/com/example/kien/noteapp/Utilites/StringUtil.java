package com.example.kien.noteapp.Utilites;

/**
 * Created by Kien on 11/14/2016.
 */

public class StringUtil {
    public static String cutStringTime(String t){
        String time;
        if(t.length() > 10) {
            time = t.substring(0, 5) + " " + t.substring(11, 16);
        }else{
            time = t;
        }
        return time;

    }
    public static String convertToTargetTimeOfNote(int year, int month, int day, int hour, int minute) {
        String time = "";
//        int seconds = miliseconds / 1000;
//        int minutes = seconds / 60;
//        int second = seconds % 60;

        if (day <= 9) {
            time = time + "0" + day;
        } else {
            time = time + day;
        }

        time = time + "/";

        if (month <= 9) {
            time = time + "0" + month;
        } else {
            time = time + month;
        }
        time = time + "/";
        if (year <= 9) {
            time = time + "000" + year;
        } else {
            time = time + year;
        }
//        time = time + year;
        time = time + " ";
        if (hour <= 9) {
            time = time + "0" + hour;
        } else {
            time = time + hour;
        }
        time = time + ":";
        if (minute <= 9) {
            time = time + "0" + minute;
        } else {
            time = time + minute;
        }

        return time;
    }
    public static String convertToDateFormat(int year, int month, int day) {
        String time = "";
//        int seconds = miliseconds / 1000;
//        int minutes = seconds / 60;
//        int second = seconds % 60;

        if (day <= 9) {
            time = time + "0" + day;
        } else {
            time = time + day;
        }

        time = time + "/";

        if (month <= 9) {
            time = time + "0" + month;
        } else {
            time = time + month;
        }
        time = time + "/";
        if (year <= 9) {
            time = time + "000" + year;
        } else {
            time = time + year;
        }
//
        return time;
    }
    public static String convertToTimeFormat(int hour, int minute) {
        String time = "";

        time = time + " ";
        if (hour <= 9) {
            time = time + "0" + hour;
        } else {
            time = time + hour;
        }
        time = time + ":";
        if (minute <= 9) {
            time = time + "0" + minute;
        } else {
            time = time + minute;
        }

        return time;
    }
    public static int[] convertToTargetTimeElementArray(String targetTime) {

        int[] myIntArray = {Integer.parseInt(targetTime.substring(0,2)),
                            Integer.parseInt(targetTime.substring(3,5)),
                            Integer.parseInt(targetTime.substring(6,10)),
                            Integer.parseInt(targetTime.substring(11,13)),
                            Integer.parseInt(targetTime.substring(14,16))};
        return myIntArray;
    }
    public static int[] convertToTargetDateElementArray(String date) {

        int[] myIntArray = {Integer.parseInt(date.substring(0,2)),
                Integer.parseInt(date.substring(3,5)),
                Integer.parseInt(date.substring(6,10))};
        return myIntArray;
    }
    public static String trimTitle(String text){
        String result = "";
        if(text.length() > 14){
            for (int i = 0; i < 14;i++){
                Character c = text.charAt(i);
                result += c;
            }

        }else{
            result = text;
        }
        return result;
    }
    public static String trimContent(String text){
        String result = "";
        if(text.length() < 60){
            result = text;

        }else{
            for(int i = 0; i < 60; i++){
                Character c = text.charAt(i);
                result += c;
            }
            result = result + "...";
        }
        return result;
    }
    public static boolean checkValidTarget(int year, int month, int day, int hour, int minute){
        if(year == 0 && month == 0 && day == 0 && hour ==0 && minute ==0){
            return  false;
        }else {
            return true;
        }
    }
}
