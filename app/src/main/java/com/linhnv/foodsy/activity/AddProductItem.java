package com.linhnv.foodsy.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;

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

public class AddProductItem extends BaseActivity {
    private EditText ed_name, ed_decription, ed_price;
    private Button btn_register;
    private SP sp;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_item);
        init();
        sp = new SP(this);
        token = sp.getToken();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed_price.getText().toString().trim();
                String decliption = ed_decription.getText().toString().trim();
                String price = ed_price.getText().toString().trim();
               // new RegisterProduct().execute(token, name, decliption, price, String.valueOf(id_placeID), String.valueOf(int_category));
                finish();
            }
        });
    }

    public void init() {
        ed_name = (EditText) findViewById(R.id.ed_add_product_name);
        ed_decription = (EditText) findViewById(R.id.ed_add_product_decription);
        ed_price = (EditText) findViewById(R.id.ed_add_product_price);
        btn_register = (Button) findViewById(R.id.button_register_product);
    }

    //Category product
    public class RegisterProduct extends AsyncTask<String, Void, String> {
        HttpHandler httpHandler;
        String token, name, description, place_id, category_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            name = params[1];
            description = params[2];
            place_id = params[3];
            category_id = params[4];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(ApiURL.URL_ADD_PRODUCT); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("name", name);
                postDataParams.put("description", description);
                postDataParams.put("place_id", place_id);
                postDataParams.put("category_id", category_id);
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(httpHandler.getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String(String.valueOf(responseCode));
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressDialog();
            if (result.equals("200")) {
                Toasty.success(AddProductItem.this, "Thêm loại món ăn thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toasty.error(AddProductItem.this, "Lỗi đăng ký!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
