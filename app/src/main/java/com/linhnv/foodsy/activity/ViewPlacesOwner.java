package com.linhnv.foodsy.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

public class ViewPlacesOwner extends BaseActivity {
    private SP sp;
    private ImageView imgImageView;
    private int id_get;
    private String token;
    private CardView cv_addproduct, cv_addevent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_places_owner);
        init();
        sp = new SP(this);
        token = sp.getToken();
        id_get = getIntent().getExtras().getInt("id_owner");
        Toast.makeText(this, String.valueOf(id_get), Toast.LENGTH_SHORT).show();
        Picasso.with(getApplicationContext())
                .load(ApiURL.URL_IMAGE_PLACE + "?id=" + id_get + "&token=" + token)
                .placeholder(R.drawable.bglogin4)
                .error(R.drawable.bglogin4)
                .into(imgImageView);

        cv_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewPlacesOwner.this, AddCategoryProduct.class);
                Bundle b = new Bundle();
                b.putInt("id_category", id_get);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    public void init() {
        imgImageView = (ImageView) findViewById(R.id.img_owner);
        cv_addevent = (CardView) findViewById(R.id.cv_addevent);
        cv_addproduct = (CardView) findViewById(R.id.cv_addproduct);
    }
}
