package com.linhnv.foodsy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Place;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout frame_eat, frame_drink, frame_entertaiment, frame_bookmark;
    private EditText edit_text_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
    }

    public void init() {
        frame_eat = (FrameLayout) findViewById(R.id.frame_eat);
        frame_drink = (FrameLayout) findViewById(R.id.frame_drink);
        frame_entertaiment = (FrameLayout) findViewById(R.id.frame_entertaiment);
        frame_bookmark = (FrameLayout) findViewById(R.id.frame_bookmark);
        edit_text_search = (EditText) findViewById(R.id.edit_text_search);
        frame_eat.setOnClickListener(this);
        frame_drink.setOnClickListener(this);
        frame_entertaiment.setOnClickListener(this);
        frame_bookmark.setOnClickListener(this);
        edit_text_search.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle(R.string.dialog_title_end_app);
        builer.setMessage(R.string.dialog_message_end_app);
        builer.setPositiveButton(R.string.dialog_button_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });
        builer.setNegativeButton(R.string.dialog_button_Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builer.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.frame_eat:
                bundle.putInt("eat", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.frame_entertaiment:
                bundle.putInt("eat", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.frame_drink:
                bundle.putInt("eat", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.frame_bookmark:
                bundle.putInt("eat", 3);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.edit_text_search:
                bundle.putInt("search", 1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }
}
