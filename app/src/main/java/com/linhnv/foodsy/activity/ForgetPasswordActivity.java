package com.linhnv.foodsy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.linhnv.foodsy.R;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgCloseForget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        anhXa();
        imgCloseForget.setOnClickListener(this);
    }

    private void anhXa(){
        imgCloseForget = (ImageView) findViewById(R.id.imgCloseForget);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgCloseForget:
                startActivity(new Intent(ForgetPasswordActivity.this, SignInActivity.class));
                finish();
                break;
        }
    }
}
