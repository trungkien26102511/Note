package com.example.kien.noteapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kien.noteapp.R;

public abstract class BaseActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView img_back,img_symbol,img_color,img_pic,img_ok,img_add,img_option;
    TextView tv_title;
    RelativeLayout rlt_back;

    public void initHeader(int type) {
        toolbar = getToolbar();
        rlt_back = (RelativeLayout)toolbar.findViewById(R.id.rlt_back);
        img_back = (ImageView)toolbar.findViewById(R.id.img_back);
        img_symbol = (ImageView)toolbar.findViewById(R.id.img_symbol);
        img_color = (ImageView)toolbar.findViewById(R.id.img_color);
        img_pic = (ImageView)toolbar.findViewById(R.id.img_pic);
        img_ok = (ImageView)toolbar.findViewById(R.id.img_ok);
        tv_title = (TextView)toolbar.findViewById(R.id.tv_title);
        img_add = (ImageView)toolbar.findViewById(R.id.img_add);
        img_option = (ImageView)toolbar.findViewById(R.id.img_option);
        if (type == 0) {
            img_ok.setVisibility(View.GONE);
            img_option.setVisibility(View.GONE);
            img_add.setVisibility(View.VISIBLE);
            img_back.setVisibility(View.GONE);
            img_symbol.setVisibility(View.VISIBLE);
            img_color.setVisibility(View.GONE);
            img_pic.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);

        } else {
            img_ok.setVisibility(View.VISIBLE);
            img_back.setVisibility(View.VISIBLE);
            img_add.setVisibility(View.GONE);
            img_option.setVisibility(View.VISIBLE);
            img_symbol.setVisibility(View.VISIBLE);
            img_color.setVisibility(View.VISIBLE);
            img_pic.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);

        }
        setSupportActionBar(toolbar);
//        try {
//            getSupportActionBar().setHomeButtonEnabled(false);
//        } catch (NullPointerException ex) {
//
//        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        if (this instanceof MainActivity){
//        }

    }
    protected void setOkOnClick(View.OnClickListener listener) {
        img_ok.setOnClickListener(listener);
    }
    protected void setAddOnClick(View.OnClickListener listener) {
        img_add.setOnClickListener(listener);
    }
    protected void setBackOnClick(View.OnClickListener listener) {
        rlt_back.setOnClickListener(listener);
    }
    protected void setOptionOnClick(View.OnClickListener listener) {
        img_option.setOnClickListener(listener);
    }
    protected void setColorOnClick(View.OnClickListener listener) {
        img_color.setOnClickListener(listener);
    }

    protected void gotoMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        startActivity(intent);
        finish();
    }
    public abstract Toolbar getToolbar();
}
