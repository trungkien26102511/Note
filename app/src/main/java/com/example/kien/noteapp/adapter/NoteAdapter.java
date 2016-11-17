package com.example.kien.noteapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kien.noteapp.R;
import com.example.kien.noteapp.Utilites.StringUtil;
import com.example.kien.noteapp.models.Note;

import java.util.ArrayList;

/**
 * Created by Kien on 11/11/2016.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    Context context;
    ArrayList<Note> mNoteList = new ArrayList<Note>();

    public NoteAdapter(Context context, ArrayList<Note> list){
        this.context = context;
        this.mNoteList = list;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note,parent,false);

        return new ViewHolder(view);
    }
//        @Override
//    public int getItemViewType(int position) {
//        return mNoteList.get(position);
//    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mNoteList.get(position).getNoteColor() != null){
            int check = mNoteList.get(position).getNoteColor();
            switch (check){
                case 0:{
                    holder.rlt_item_note.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                    break;
                }
                case 1:{
                    holder.rlt_item_note.setBackgroundColor(context.getResources().getColor(R.color.colorC));
                    break;
                }
                case 2:{
                    holder.rlt_item_note.setBackgroundColor(context.getResources().getColor(R.color.colorX));
                    break;
                }
                case 3:{
                    holder.rlt_item_note.setBackgroundColor(context.getResources().getColor(R.color.colorXnb));
                    break;
                }
            }
        }
        holder.tv_title.setText(StringUtil.trimTitle(mNoteList.get(position).getNoteTitle()));
        String time = StringUtil.cutStringTime(mNoteList.get(position).getNoteTime());
        holder.tv_time.setText(time);
        holder.tv_content.setText(StringUtil.trimContent(mNoteList.get(position).getNoteContent()));

    }


    @Override
    public int getItemCount() {
        return mNoteList != null ? mNoteList.size() : 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_time,tv_content;
        RelativeLayout rlt_item_note;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            tv_time = (TextView)itemView.findViewById(R.id.tv_time);
            tv_content = (TextView)itemView.findViewById(R.id.tv_content);
            rlt_item_note = (RelativeLayout)itemView.findViewById(R.id.rlt_item_note);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    context.startActivity(new Intent(context, EditActivity.class));
//                }
//            });
        }
    }

}
