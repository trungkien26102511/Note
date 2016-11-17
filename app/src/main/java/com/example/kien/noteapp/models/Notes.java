package com.example.kien.noteapp.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Kien on 11/11/2016.
 */

public class Notes implements Serializable{
    private ArrayList<Note> mNoteList =  new ArrayList<>();

    public Notes(ArrayList<Note> mNoteList) {
        this.mNoteList = mNoteList;
    }

    public ArrayList<Note> getmNoteList() {
        return mNoteList;
    }

    public void setmNoteList(ArrayList<Note> mNoteList) {
        this.mNoteList = mNoteList;
    }
}
