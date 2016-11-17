package com.example.kien.noteapp.models;

import java.io.Serializable;

/**
 * Created by Kien on 11/11/2016.
 */

public class Note implements Serializable{
    private int mNoteId;
    private String mNoteTitle;
    private String mNoteContent;
    private String mNoteTime;
    private Integer mNoteColor;
    private String mTargetTime;


    public Note(int mNoteId, String mNoteTitle, String mNoteContent, String mNoteTime, Integer mNoteColor) {
        this.mNoteId = mNoteId;
        this.mNoteTitle = mNoteTitle;
        this.mNoteContent = mNoteContent;
        this.mNoteTime = mNoteTime;
        this.mNoteColor = mNoteColor;
    }
    public Note(int mNoteId, String mNoteTitle, String mNoteContent, String mNoteTime, Integer mNoteColor,String mTargetTime) {
        this.mNoteId = mNoteId;
        this.mNoteTitle = mNoteTitle;
        this.mNoteContent = mNoteContent;
        this.mNoteTime = mNoteTime;
        this.mNoteColor = mNoteColor;
        this.mTargetTime = mTargetTime;
    }

    public Note(String mNoteTitle, String mNoteContent, String mNoteTime, Integer mNoteColor) {
        this.mNoteTitle = mNoteTitle;
        this.mNoteContent = mNoteContent;
        this.mNoteTime = mNoteTime;
        this.mNoteColor = mNoteColor;
    }
    public Note(String mNoteTitle, String mNoteContent, String mNoteTime, Integer mNoteColor, String mTargetTime) {
        this.mNoteTitle = mNoteTitle;
        this.mNoteContent = mNoteContent;
        this.mNoteTime = mNoteTime;
        this.mNoteColor = mNoteColor;
        this.mTargetTime =  mTargetTime;
    }

    public int getNoteId() {
        return mNoteId;
    }

    public void setNoteId(int mNoteId) {
        this.mNoteId = mNoteId;
    }

    public String getNoteTitle() {
        return mNoteTitle;
    }

    public void setNoteTitle(String mNoteTitle) {
        this.mNoteTitle = mNoteTitle;
    }

    public String getNoteContent() {
        return mNoteContent;
    }

    public void setNoteContent(String mNoteContent) {
        this.mNoteContent = mNoteContent;
    }

    public Integer getNoteColor() {
        return mNoteColor;
    }

    public void setNoteColor(Integer mNoteColor) {
        this.mNoteColor = mNoteColor;
    }

    public String getNoteTime() {
        return mNoteTime;
    }

    public void setNoteTime(String mNoteTime) {
        this.mNoteTime = mNoteTime;
    }

    public String getTargetTime() {
        return mTargetTime;
    }

    public void setTargetTime(String mTargetTime) {
        this.mTargetTime = mTargetTime;
    }
}

