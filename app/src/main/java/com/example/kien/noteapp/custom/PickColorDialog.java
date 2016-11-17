package com.example.kien.noteapp.custom;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kien.noteapp.PickColorDialogCallback;
import com.example.kien.noteapp.R;

/**
 * Created by Kien on 11/14/2016.
 */

public class PickColorDialog extends DialogFragment implements View.OnClickListener {
    ImageView img_color_1, img_color_2,img_color_3, img_color_4;
    PickColorDialogCallback pickColorDialogCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        setStyle(STYLE_NO_TITLE, 0);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.choose_color, container, false);
        img_color_1 = (ImageView) v.findViewById(R.id.img_color_1);
        img_color_2 = (ImageView) v.findViewById(R.id.img_color_2);
        img_color_3 = (ImageView) v.findViewById(R.id.img_color_3);
        img_color_4 = (ImageView) v.findViewById(R.id.img_color_4);

        img_color_2.setOnClickListener(this);
        img_color_1.setOnClickListener(this);
        img_color_3.setOnClickListener(this);
        img_color_4.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_color_1:{
                pickColorDialogCallback.onOk(0);
                break;
            }
            case R.id.img_color_2:{
                pickColorDialogCallback.onOk(1);
                break;
            }
            case R.id.img_color_3:{
                pickColorDialogCallback.onOk(2);
                break;
            }
            case R.id.img_color_4:{
                pickColorDialogCallback.onOk(3);
                break;
            }
        }

    }

    public void setPickColorDialogCallback(PickColorDialogCallback pickColorDialogCallback) {
        this.pickColorDialogCallback = pickColorDialogCallback;
    }
}
