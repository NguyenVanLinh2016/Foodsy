package com.linhnv.foodsy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.SP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private SP sp;
    private Button btnSignUp, btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check user, if already update info -> go to MenuActivity
        sp = new SP(this);
        if (sp.getStateLogin()) {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
            finish();
        }

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
                finish();
                break;
            case R.id.btnSignIn:
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
                break;
        }
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
}
