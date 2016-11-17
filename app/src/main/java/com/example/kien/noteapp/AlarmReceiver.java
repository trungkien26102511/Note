package com.example.kien.noteapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.kien.noteapp.activity.EditActivity;
import com.example.kien.noteapp.models.Note;
import com.example.kien.noteapp.models.TargetTime;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Kien on 11/14/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

        Note note = (Note)intent.getSerializableExtra("data");
        TargetTime time = (TargetTime)intent.getSerializableExtra("targetTime");
        Intent i = new Intent(context, EditActivity.class);
        i.putExtra("note", note);
        i.putExtra("targetTime",time);
        PendingIntent pIntent = PendingIntent.getActivity(context,
                0,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(note.getNoteTitle())
                .setContentText(note.getNoteContent())
//                .addAction(R.mipmap.ic_launcher, "Action Button", pIntent);
                .setContentIntent(pIntent)
                .setAutoCancel(true);
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(0, builder.build());
    }
}
