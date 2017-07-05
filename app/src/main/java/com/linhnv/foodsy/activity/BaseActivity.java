package com.linhnv.foodsy.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false); //can cancel when press back
            mProgressDialog.setMessage("Loading");
        }
        mProgressDialog.show();
    }
    public void showProgressDialog(String msg){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false); //can cancel when press back
            mProgressDialog.setMessage(msg);
        }
        mProgressDialog.show();
    }
    public void hideProgressDialog(){
        if (mProgressDialog != null || mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
