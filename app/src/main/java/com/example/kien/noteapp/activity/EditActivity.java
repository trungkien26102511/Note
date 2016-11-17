package com.example.kien.noteapp.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kien.noteapp.AlarmReceiver;
import com.example.kien.noteapp.PickColorDialogCallback;
import com.example.kien.noteapp.R;
import com.example.kien.noteapp.Utilites.StringUtil;
import com.example.kien.noteapp.custom.PickColorDialog;
import com.example.kien.noteapp.database.MyDatabase;
import com.example.kien.noteapp.models.Note;
import com.example.kien.noteapp.models.Notes;
import com.example.kien.noteapp.models.TargetTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditActivity extends BaseActivity {
    Toolbar toolbar;
    private boolean needRefresh;
    private EditText edt_title, edt_content;
    private TextView tv_time;
    private ImageView img_previous,img_next,img_delete,img_share;

    private ImageView img_del_alarm;
    private TextView tv_show_alarm;
    private LinearLayout ll_alarm;

    private LinearLayout ll_bottom;

    private RelativeLayout rlt_all;
    private int mColor = 0;

    private Notes notes;
    private ArrayList<Note> mNoteList = new ArrayList<Note>();
    private int mPosition;

    private Spinner spn_day, spn_time;
    private ArrayList<String> mAlarmDay = new ArrayList<String>();
    private ArrayList<String> mAlarmTime =  new ArrayList<String>();
    private int year, month, day, hour, minute;
    private int mYearTerm, mMonthTerm, mDayTerm, mHourTerm, mMinuteTerm;

    private DatePicker datePicker;
    private Calendar calendar;
    private ArrayAdapter<String> mSpinnerAdapterDay;
    private ArrayAdapter<String> mSpinnerAdapterTime;

    private TargetTime targetTime;
    private boolean isFromNotification = false;
    private boolean isAlarmOn = false;
//    private Color[] mBackground = {new Color(255,255,255),new Color(255,187,34),new Color(0,0,255)};
//            ,R.color.colorC
//            ,R.color.colorX
//            ,R.color.colorXnb};
//    private String[] mColors = {String.valueOf(getResources().getColor(R.color.colorX))
//        ,String.valueOf(getResources().getColor(R.color.colorX))
//        ,String.valueOf(getResources().getColor(R.color.colorX))
//        ,String.valueOf(getResources().getColor(R.color.colorX))};
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private int mode;
    private MyDatabase db;

    MainActivity mMainActivity;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        resetTargetTime();
        setSupportActionBar(toolbar);
        initHeader(1);
        initView();
        initData();
        initDataSpinner();
        initControl();


        Intent intent = this.getIntent();
        this.note = (Note) intent.getSerializableExtra("note");
        this.targetTime = (TargetTime) intent.getSerializableExtra("targetTime");
        this.mPosition = intent.getIntExtra("position",100000);
        if (note == null) {
            this.mode = MODE_CREATE;
            isAlarmOn = false;
            img_option.setVisibility(View.GONE);
            ll_bottom.setVisibility(View.GONE);
            String time = getCurrentTime();
            tv_time.setText(time);
        } else {
            this.mode = MODE_EDIT;
            img_option.setVisibility(View.VISIBLE);
            this.tv_time.setText(note.getNoteTime());
            this.edt_title.setText(note.getNoteTitle());
            this.edt_content.setText(note.getNoteContent());
            tv_title.setText(StringUtil.trimTitle(note.getNoteTitle()));
            int[] arr = StringUtil.convertToTargetTimeElementArray(note.getTargetTime());
            day = arr[0];
            month = arr[1];
            year = arr[2];
            hour = arr[3];
            minute = arr[4];
            if(day == 0 && month == 0 && year == 0 && hour ==0 && minute == 0){
                isAlarmOn = false;
            }else{
                showAlarmView();
                isAlarmOn = true;
            }
            if(note.getNoteColor() != null){
                mColor = note.getNoteColor();
                changeColor(note.getNoteColor());
            }

        }
        if(targetTime != null){
            ll_alarm.setVisibility(View.VISIBLE);
            tv_show_alarm.setVisibility(View.GONE);
            isAlarmOn = true;
            mAlarmDay.remove(mAlarmDay.size() -1);
            mAlarmDay.add(targetTime.getmDay() + "/" + (targetTime.getmMonth() + 1) + "/" + targetTime.getmYear());
            spn_day.setSelection(mAlarmDay.size()-1);
            //================
            mAlarmTime.remove(mAlarmTime.size() -1);
            mAlarmTime.add(targetTime.getmHour() + ":" + targetTime.getmMinute());
            spn_time.setSelection(mAlarmTime.size()-1);
            //==============
            isFromNotification = true;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isFromNotification = false;
            }
        }, 1000);

    }

    private void showAlarmView() {
        ll_alarm.setVisibility(View.VISIBLE);
        tv_show_alarm.setVisibility(View.GONE);
        isAlarmOn =true;
        mAlarmDay.remove(mAlarmDay.size() -1);
        mAlarmDay.add(StringUtil.convertToDateFormat(year,month + 1,day));
        spn_day.setSelection(mAlarmDay.size()-1);
        //================
        mAlarmTime.remove(mAlarmTime.size() -1);
        mAlarmTime.add(StringUtil.convertToTimeFormat(hour,minute));
        spn_time.setSelection(mAlarmTime.size()-1);
        //==============
        isFromNotification = true;
    }

    private void initDataSpinner() {
        calendar = Calendar.getInstance();
        mYearTerm = calendar.get(Calendar.YEAR);

        mMonthTerm = calendar.get(Calendar.MONTH);
        mDayTerm = calendar.get(Calendar.DAY_OF_MONTH);
        //===============================
        mAlarmDay.add("Today");
        mAlarmDay.add("Tomorrow");
        mAlarmDay.add("Next Monday");
        mAlarmDay.add("Other...");
        mSpinnerAdapterDay = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mAlarmDay);
        mSpinnerAdapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_day.setAdapter(mSpinnerAdapterDay);
        //===========================
        mAlarmTime.add("09:00");
        mAlarmTime.add("13:00");
        mAlarmTime.add("17:00");
        mAlarmTime.add("20:00");
        mAlarmTime.add("Other...");
        mSpinnerAdapterTime = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mAlarmTime);
        mSpinnerAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_time.setAdapter(mSpinnerAdapterTime);

    }

    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void initData() {
        db = new MyDatabase(this);
        mNoteList = db.getAllNote();
    }


    private void initView() {
        rlt_all = (RelativeLayout)findViewById(R.id.rlt_all);
        tv_time = (TextView)findViewById(R.id.tv_time);
        edt_content = (EditText)findViewById(R.id.edt_content);
        edt_title = (EditText)findViewById(R.id.edt_title);
        img_previous = (ImageView)findViewById(R.id.img_previous);
        img_next = (ImageView)findViewById(R.id.img_next);
        img_delete = (ImageView)findViewById(R.id.img_delete);
        img_share = (ImageView)findViewById(R.id.img_share);
        ll_bottom = (LinearLayout)findViewById(R.id.ll_bottom);
        spn_day = (Spinner)findViewById(R.id.spn_day);
        spn_time = (Spinner)findViewById(R.id.spn_time);
        tv_show_alarm = (TextView)findViewById(R.id.tv_show_alrm);
        img_del_alarm = (ImageView)findViewById(R.id.img_del_alarm);
        ll_alarm = (LinearLayout)findViewById(R.id.ll_alarm);

    }

    private void initControl() {
        setOkOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
        setBackOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainActivity();
            }
        });
        tv_show_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_alarm.setVisibility(View.VISIBLE);
                year = month = day = hour = minute = 1;
                tv_show_alarm.setVisibility(View.GONE);
                isAlarmOn = true;
            }
        });
        img_del_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_alarm.setVisibility(View.GONE);
                resetTargetTime();
                tv_show_alarm.setVisibility(View.VISIBLE);
                isAlarmOn = false;
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete();
            }
        });
        img_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditActivity.this,EditActivity.class));
            }
        });
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });
        img_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPrevious();
            }
        });
        setColorOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickColor();
            }
        });
        spn_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                isFromNotification = false;
                String selection = spn_day.getItemAtPosition(position).toString();
                Toast.makeText(EditActivity.this, selection, Toast.LENGTH_SHORT).show();
                if(selection.equals("Other...")){
                    setDate();
                }else{
                    if(isFromNotification != true){
                        mAlarmDay.remove(mAlarmDay.size() -1);
                        mAlarmDay.add("Other...");
                        mSpinnerAdapterDay.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                isFromNotification = false;
                String selection = spn_time.getItemAtPosition(position).toString();
                Toast.makeText(EditActivity.this, selection, Toast.LENGTH_SHORT).show();
                if(selection.equals("Other...")){
                    setTime();
                }else{
                    if(isFromNotification != true){
                        mAlarmTime.remove(mAlarmTime.size() -1);
                        mAlarmTime.add("Other...");
                        mSpinnerAdapterTime.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setTime() {
        Calendar mcurrentTime = Calendar.getInstance();
        mHourTerm = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        mMinuteTerm = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String newTime = selectedHour + ":" + selectedMinute;
                hour = selectedHour;
                minute = selectedMinute;
                mAlarmTime.remove(mAlarmTime.size() -1);
                mAlarmTime.add(newTime);
                mSpinnerAdapterTime.notifyDataSetChanged();
                spn_time.setSelection(mAlarmTime.size()-1);

            }
        }, mHourTerm, mMinuteTerm, true);//Yes, 24 hour time
        mTimePicker.setTitle("Choose Time");
        mTimePicker.show();
    }

    private void setDate() {
        showDialog(999);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, mYearTerm, mMonthTerm, mDayTerm);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year // arg2 = month // arg3 = day
            year = arg1;
            month = arg2;
            day = arg3;
            String newDate = arg3 + "/" + (arg2 + 1) +"/" + arg1;
            mAlarmDay.remove(mAlarmDay.size() -1);
            mAlarmDay.add(newDate);
            mSpinnerAdapterDay.notifyDataSetChanged();
            spn_day.setSelection(mAlarmDay.size()-1);
//            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//            String formattedDate = df.format(c.getTime());
            Toast.makeText(EditActivity.this, " " +arg1 +" " + arg2 +" " +arg3, Toast.LENGTH_SHORT).show();
        }
    };

    private void onPickColor() {
        PickColorDialog dialog = new PickColorDialog();
        FragmentManager fm = getSupportFragmentManager();
        dialog.show(fm, "pick color");
        dialog.setPickColorDialogCallback(new PickColorDialogCallback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onOk(int color) {
               changeColor(color);
                mColor = color;
            }
        });
    }
    public void changeColor(int color){
        switch (color){
            case 0:{
                rlt_all.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                break;
            }
            case 1:{
                rlt_all.setBackgroundColor(getResources().getColor(R.color.colorC));
                break;
            }
            case 2:{
                rlt_all.setBackgroundColor(getResources().getColor(R.color.colorX));
                break;
            }
            case 3:{
                rlt_all.setBackgroundColor(getResources().getColor(R.color.colorXnb));
                break;
            }
        }
    }

    private void onPrevious() {
        if(mPosition >0 && mPosition< mNoteList.size()){
            img_previous.setEnabled(true);
            Intent i = new Intent(EditActivity.this, EditActivity.class);
            i.putExtra("note",mNoteList.get(mPosition - 1));
            i.putExtra("position",mPosition - 1);
            startActivity(i);
        }else{
            img_previous.setEnabled(false);
        }
    }

    private void onNext() {
        if(mPosition >=0 && mPosition< mNoteList.size() - 1){
            img_next.setEnabled(true);
            Intent i = new Intent(EditActivity.this, EditActivity.class);
            i.putExtra("note",mNoteList.get(mPosition + 1));
            i.putExtra("position",mPosition + 1);
            startActivity(i);
        }else{
            img_next.setEnabled(false);
        }
    }

    private void onDelete() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.view_delete_item)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteNote(note);
                        gotoMainActivity();
                    }
                }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(true)
                .create();
        dialog.show();
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }


    public void onSave() {
        String title = this.edt_title.getText().toString();
        String content = this.edt_content.getText().toString();

        if (title.equals("") || content.equals("")) {
            Toast.makeText(getApplicationContext(),
                    "Please enter title & content", Toast.LENGTH_LONG).show();
            return;
        }
        String time = getCurrentTime();

//        if (mode == MODE_CREATE) {
//            checkDateAndTime();
//            String t = StringUtil.convertToTargetTimeOfNote(year,month,day,hour,minute);
//            this.note = new Note(title, content, time,mColor,t);
//            db.addNote(note);
//        }
//        this.needRefresh = true;
//        if(!isAlarmOn){
//            resetTargetTime();
//            gotoMainActivity();
//
//        }else {
//            checkDateAndTime();
//            covertToCal(year,month,day,hour,minute);
//
//            Calendar current = Calendar.getInstance();
//            Calendar cal = Calendar.getInstance();
//            cal.set(year, month, day, hour, minute, 00);
//            if(cal.compareTo(current) <= 0){
//                Toast.makeText(getApplicationContext(),
//                        "      Invalid Date/Time\n Please fix the alarm time",
//                        Toast.LENGTH_LONG).show();
//
//            }else{
//                if(mode == MODE_EDIT) {
//                    String t = StringUtil.convertToTargetTimeOfNote(year,month,day,hour,minute);
//                    Log.e("VIewActivity", "onSave"  + title + content);
//                    this.note.setNoteTitle(title);
//                    this.note.setNoteContent(content);
//                    this.note.setNoteTime(time);
//                    this.note.setNoteColor(mColor);
//                    this.note.setTargetTime(t);
//                    db.updateNote(note);
//
//                }
//                gotoMainActivity();
//            }
//        }
        if(mode == MODE_CREATE && !isAlarmOn){
            resetTargetTime();
            String t = StringUtil.convertToTargetTimeOfNote(year,month,day,hour,minute);
            this.note = new Note(title, content, time,mColor,t);
            db.addNote(note);
            this.gotoMainActivity();

        }else if(mode == MODE_CREATE && isAlarmOn){
            checkDateAndTime();
            covertToCal(year,month,day,hour,minute);

            Calendar current = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hour, minute, 00);
            if(cal.compareTo(current) <= 0){
                Toast.makeText(getApplicationContext(),
                        "      Invalid Date/Time\n Please fix the alarm time",
                        Toast.LENGTH_LONG).show();
                return;

            }else {
                String t = StringUtil.convertToTargetTimeOfNote(year,month,day,hour,minute);
                this.note = new Note(title, content, time,mColor,t);
                needRefresh = true;
                db.addNote(note);
                this.gotoMainActivity();
            }

        }else if(mode == MODE_EDIT && !isAlarmOn){
            resetTargetTime();
            String t = StringUtil.convertToTargetTimeOfNote(year,month,day,hour,minute);
            this.note.setNoteTitle(title);
            this.note.setNoteContent(content);
            this.note.setNoteTime(time);
            this.note.setNoteColor(mColor);
            this.note.setTargetTime(t);
            db.updateNote(note);
            this.gotoMainActivity();

        }else if(mode == MODE_EDIT && isAlarmOn){
            checkDateAndTime();
            covertToCal(year,month,day,hour,minute);

            Calendar current = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hour, minute, 00);
            if(cal.compareTo(current) <= 0){
                Toast.makeText(getApplicationContext(),
                        "      Invalid Date/Time\n Please fix the alarm time",
                        Toast.LENGTH_LONG).show();
                return;

            }else {
                String t = StringUtil.convertToTargetTimeOfNote(year,month,day,hour,minute);
                this.note.setNoteTitle(title);
                this.note.setNoteContent(content);
                this.note.setNoteTime(time);
                this.note.setNoteColor(mColor);
                this.note.setTargetTime(t);
                db.updateNote(note);
                this.gotoMainActivity();
            }
        }
//        this.onBackPressed();
    }


    private void checkDateAndTime() {
        if(year == 0 && month == 0 && day == 0 && hour == 0 && minute ==0 ){
            return;
        }
        String checkDay = spn_day.getSelectedItem().toString();
        Calendar calendar = Calendar.getInstance();
        if(checkDay.equals("Today")){
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }else if(checkDay.equals("Tomorrow")){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }else if(checkDay.equals("Next Monday")){
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if (weekday != Calendar.MONDAY)
            {
               int days = (Calendar.SATURDAY - weekday + 2) % 7;
               calendar.add(Calendar.DAY_OF_YEAR, days);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }
        }else if(checkDay.substring(6,8).equals("20")){
            int[] arr = StringUtil.convertToTargetDateElementArray(checkDay);
            day = arr[0];
            month = arr[1] - 1;
            year = arr[2];
        }

        //===========
        String checkTime = spn_time.getSelectedItem().toString();
        if(checkTime.equals("09:00")){
            hour = 9;
            minute = 0;
        }else if(checkTime.equals("13:00")){
            hour = 13;
            minute = 0;
        }else if(checkTime.equals("17:00")){
            hour = 17;
            minute = 0;
        }else if(checkTime.equals("20:00")){
            hour = 20;
            minute = 0;
        }
    }


    private void covertToCal(int yearC, int monthC, int dayC, int hourC, int minuteC) {
        if(yearC == 0 && monthC == 0 && dayC == 0 && hourC == 0 && minuteC ==0 ){
            return ;
        }
        Calendar current = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        cal.set(yearC, monthC, dayC, hourC, minuteC, 00);

        if(cal.compareTo(current) <= 0){
            resetTargetTime();
//            Toast.makeText(getApplicationContext(),
//                    "Invalid Date/Time",
//                    Toast.LENGTH_SHORT).show();

        }else{
            setAlarm(cal);
        }
    }

    private void setAlarm(Calendar targetCal) {
        TargetTime t = new TargetTime(year,month,day,hour,minute);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("data",note);
        intent.putExtra("targetTime",t);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }
    private void resetTargetTime() {
        year = month =day =hour = minute = 0;
    }


    @Override
    public void finish() {
        Log.e("DIARY_APP", "connected finish()");
        Intent data = new Intent();
        data.putExtra("needRefresh", needRefresh);
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }

}

