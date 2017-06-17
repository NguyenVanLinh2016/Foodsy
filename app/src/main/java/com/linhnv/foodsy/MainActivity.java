package com.linhnv.foodsy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp, btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //test
        //startActivity(new Intent(MainActivity.this, HomeActivity.class));

        anhXa();
        btnSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }
    public void anhXa(){
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));

                break;
            case R.id.btnSignIn:
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                break;
        }
    }
}
