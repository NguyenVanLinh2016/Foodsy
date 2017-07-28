package com.linhnv.foodsy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.linhnv.foodsy.R;

public class AddProduct extends AppCompatActivity {
    private int int_category, id_placeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        int_category = getIntent().getExtras().getInt("category");
        id_placeID = getIntent().getExtras().getInt("place");
        Log.e("all id", String.valueOf(int_category + id_placeID));
    }
    public void init(){

    }
}
