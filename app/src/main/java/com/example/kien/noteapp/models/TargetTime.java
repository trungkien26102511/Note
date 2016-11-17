package com.example.kien.noteapp.models;

import java.io.Serializable;

/**
 * Created by Kien on 11/14/2016.
 */

public class TargetTime implements Serializable {
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    public TargetTime(int mYear, int mMonth, int mDay, int mHour, int mMinute) {
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mHour = mHour;
        this.mMinute = mMinute;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public int getmHour() {
        return mHour;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public int getmMinute() {
        return mMinute;
    }

    public void setmMinute(int mMinute) {
        this.mMinute = mMinute;
    }
}
