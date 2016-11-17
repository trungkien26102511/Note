package com.example.kien.noteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kien.noteapp.models.Note;

import java.util.ArrayList;

/**
 * Created by Kien on 11/11/2016.
 */

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE = "Diary Database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_DIARY = " Diary";
    private static final String COLUMN_DIARY_ID = "id";
    private static final String COLUMN_DIARY_TYPE = "type";
    private static final String COLUMN_DIARY_TITLE = "title";
    private static final String COLUMN_DIARY_CONTENT = "content";
    private static final String COLUMN_DIARY_TIME = "time";
    private static final String COLUMN_DIARY_COLOR = "color";
    private static final String COLUMN_DIARY_TARGET_TIME = "targettime";


    public MyDatabase(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String diary_create = "CREATE TABLE " + TABLE_DIARY
                + "( " + COLUMN_DIARY_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_DIARY_TITLE + " TEXT, "
                + COLUMN_DIARY_CONTENT + " TEXT, "
                + COLUMN_DIARY_TIME + " TEXT, "
                + COLUMN_DIARY_COLOR +" INTEGER, "
                + COLUMN_DIARY_TARGET_TIME + " TEXT )";
        db.execSQL(diary_create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXSIST " + TABLE_DIARY);
        onCreate(db);

    }
    public void createDefaultNotesIfNeed()  {
        int count = this.getNoteCount();
        if(count ==0 ) {
            Note note1 = new Note("Note Example 1","to do 1",
                    "12/12/2016",0,"00/00/0000 00:00");
            Note note2 = new Note("Note Example 2","to do 2",
                    "11/03/2016",1,"00/00/0000 00:00");
            Note note3 = new Note("Note Example 3","to do 3",
                    "19/05/2016",2,"00/00/0000 00:00");
            this.addNote(note1);
            this.addNote(note2);
            this.addNote(note3);
        }
    }


    public void addNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DIARY_TITLE, note.getNoteTitle());
        contentValues.put(COLUMN_DIARY_CONTENT, note.getNoteContent());
        contentValues.put(COLUMN_DIARY_TIME, note.getNoteTime());
        contentValues.put(COLUMN_DIARY_COLOR, note.getNoteColor());
        contentValues.put(COLUMN_DIARY_TARGET_TIME, note.getTargetTime());
        long check = db.insert(TABLE_DIARY, null, contentValues);
        if (check == -1) {
            Log.i("TAG", "insert error");
        }
        db.close();
    }

    public Note getNote(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_DIARY, new String[]
                {COLUMN_DIARY_ID, COLUMN_DIARY_TYPE, COLUMN_DIARY_TITLE, COLUMN_DIARY_CONTENT,COLUMN_DIARY_TIME,COLUMN_DIARY_COLOR,COLUMN_DIARY_TARGET_TIME}
                , COLUMN_DIARY_ID + " =?", new String[]{String.valueOf(id)}
                , null
                , null
                , null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Note result = new Note(Integer.parseInt(cursor.getString(0))
                ,cursor.getString(1)
                ,cursor.getString(2)
                ,cursor.getString(3)
                ,Integer.parseInt(cursor.getString(4))
                ,cursor.getString(5));

        return result;
    }
    public ArrayList<Note> getAllNote(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Note> notes = new ArrayList<Note>();
        String all_query = "SELECT * FROM " + TABLE_DIARY;
        Cursor cursor = db.rawQuery(all_query,null);
        if (cursor.moveToFirst()){
            do{
                Note obj = new Note(Integer.parseInt(cursor.getString(0))
                        ,cursor.getString(1)
                        ,cursor.getString(2)
                        ,cursor.getString(3)
                        ,Integer.parseInt(cursor.getString(4))
                        ,cursor.getString(5));
                notes.add(obj);

            }while(cursor.moveToNext());
        }
        return notes;
    }
    public int getNoteCount(){
        String count_query = "SELECT * FROM " + TABLE_DIARY;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(count_query,null);
        int count = cursor.getCount();
        db.close();

        return count;
    }
    public void updateNote(Note note){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DIARY_TITLE,note.getNoteTitle());
        contentValues.put(COLUMN_DIARY_CONTENT,note.getNoteContent());
        contentValues.put(COLUMN_DIARY_TIME,note.getNoteTime());
        contentValues.put(COLUMN_DIARY_COLOR,note.getNoteColor());
        contentValues.put(COLUMN_DIARY_TARGET_TIME,note.getTargetTime());
        int check = db.update(TABLE_DIARY,contentValues,COLUMN_DIARY_ID + " =?",new String[]{String.valueOf(note.getNoteId())});
        Log.i("MyDatabaseHelper"," update in row " + check);
        db.close();
    }
    public void deleteNote(Note note){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_DIARY,COLUMN_DIARY_ID + " = ?",new String[]{String.valueOf(note.getNoteId())});
        db.close();
    }

}

