package com.linhnv.foodsy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgCloseSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        anhXa();
        imgCloseSignUp.setOnClickListener(this);
    }

    private void anhXa(){
        imgCloseSignUp = (ImageView) findViewById(R.id.imgCloseSignUp);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgCloseSignUp:
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
                break;
        }
    }
}
