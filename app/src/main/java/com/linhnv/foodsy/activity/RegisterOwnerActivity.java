package com.linhnv.foodsy.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.fragment.BaseFragment;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONException;
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

public class RegisterOwnerActivity extends BaseActivity {
    private String url_register = "https://foodsyapp.herokuapp.com/api/user/owner/register";
    private SP sp;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);
        init();
        sp = new SP(this);
        final String token = sp.getToken();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterOwner().execute(token);
            }
        });
    }

    private void init(){
        btn_register = (Button) findViewById(R.id.btn_registerowner);
    }
    public class RegisterOwner extends AsyncTask<String, Void, String> {
        HttpHandler httpHandler;
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(url_register); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
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
            Log.d("trang thai", result);
            if (Integer.valueOf(result) == 200){
                Toast.makeText(RegisterOwnerActivity.this, "Đăng ký thành công, đang chờ xét duyệt...", Toast.LENGTH_SHORT).show();
                btn_register.setEnabled(false);
                btn_register.setText("Đang chờ xét duyệt...");
            }

        }
    }
}
