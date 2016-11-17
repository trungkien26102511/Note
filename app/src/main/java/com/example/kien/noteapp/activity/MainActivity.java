package com.example.kien.noteapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.kien.noteapp.R;
import com.example.kien.noteapp.adapter.NoteAdapter;
import com.example.kien.noteapp.database.MyDatabase;
import com.example.kien.noteapp.models.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    public MainActivity mMainActivity;
    Toolbar toolbar;
    private GridLayoutManager glm;
    private NoteAdapter adapter;
    private RecyclerView mRecyclerView;
    ArrayList<Note> mNoteList =  new ArrayList<Note>();
    private static final int MY_REQUEST_CODE = 1000;
    private static final int MY_CHECK = 11111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initHeader(0);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView)findViewById(R.id.rcv_note);
        mRecyclerView.setHasFixedSize(true);
        glm=new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(glm);
        adapter = new NoteAdapter(this,mNoteList);
        mRecyclerView.setAdapter(adapter);
        MyDatabase db = new MyDatabase(this);
        db.createDefaultNotesIfNeed();
        List<Note> list = db.getAllNote();
        this.mNoteList.addAll(list);
        mRecyclerView.addOnItemTouchListener(new RecycleOnTouchListener(this, mRecyclerView, new ItemClick() {
            @Override
            public void onClick(View view, int position) {
                final Note selectedNote = (Note) mNoteList.get(position);
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("note", selectedNote);
                intent.putExtra("position",position);
                Toast.makeText(MainActivity.this, "item clicked", Toast.LENGTH_SHORT).show();
                startActivityForResult(intent, MY_REQUEST_CODE);
            }

            @Override
            public void longClick(View view, int position) {

            }
        }));
        initControl();

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            glm=new GridLayoutManager(this,3);
            mRecyclerView.setLayoutManager(glm);
            adapter.notifyDataSetChanged();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            glm=new GridLayoutManager(this,2);
            mRecyclerView.setLayoutManager(glm);
            adapter.notifyDataSetChanged();

        }
    }

    private void initControl() {
        setAddOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });
    }

    public MainActivity getmMainActivity() {
        return mMainActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mNoteList.clear();
        MyDatabase db = new MyDatabase(this);
        List<Note> list = db.getAllNote();
        this.mNoteList.addAll(list);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }
    private class RecycleOnTouchListener implements RecyclerView.OnItemTouchListener {

        private ItemClick itemClick;
        private GestureDetector gestureDetector;


        public RecycleOnTouchListener(Context context, final RecyclerView recyclerView,
                                      final ItemClick clickListener) {

            this.itemClick = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && itemClick != null) {
                        itemClick.longClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && itemClick != null && gestureDetector.onTouchEvent(e)) {
                itemClick.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private interface ItemClick {
        void onClick(View view, int position);

        void longClick(View view, int position);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            if (needRefresh) {
                this.mNoteList.clear();
                MyDatabase db = new MyDatabase(this);
                List<Note> list = db.getAllNote();
                this.mNoteList.addAll(list);
                this.adapter.notifyDataSetChanged();
            }
        }
    }
}
