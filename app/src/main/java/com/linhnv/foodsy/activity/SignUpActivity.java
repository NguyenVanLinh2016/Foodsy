package com.linhnv.foodsy.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener{

    private ImageView imgCloseSignUp;
    private Button btnSignUp;
    private EditText edtUsername, edtPassword, edtConfirmPass;
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private int status = 0;
    private String url_register = "https://foodsy-api.herokuapp.com/api/auth/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        imgCloseSignUp.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    private void init(){
        imgCloseSignUp = (ImageView) findViewById(R.id.imgCloseSignUp);
        btnSignUp = (Button) findViewById(R.id.btnSignUp_signup);
        edtUsername = (EditText) findViewById(R.id.edtUsername_signup);
        edtPassword = (EditText) findViewById(R.id.edtPass_signup);
        edtConfirmPass = (EditText) findViewById(R.id.edtConfirmPass_signup);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgCloseSignUp:
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.btnSignUp_signup:
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirmPass = edtConfirmPass.getText().toString().trim();
                if (username.length() < 6){
                    edtUsername.setError(getString(R.string.error_message_username));
                    edtUsername.requestFocus();
                }else if(password.length() < 6){
                    edtPassword.requestFocus();
                    edtPassword.setError(getString(R.string.error_message_password));
                }else if(confirmPass.length() == 0){
                    edtConfirmPass.requestFocus();
                    edtConfirmPass.setError(getString(R.string.error_message_confirmPass));
                }else if(!password.equalsIgnoreCase(confirmPass)){
                    edtConfirmPass.setError(getString(R.string.error_message_confirm));
                }else{
                    new RegisterUser().execute(username, password);
                }
                break;
        }
    }
    private void register(String username, String password){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", username);
        formBuilder.add("password", password);
        RequestBody formBody = formBuilder.build();
        final Request request = new Request.Builder().url(url_register).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Register fail");
                    throw new IOException("Unexpected code " + response);
                } else {
                    status = 200;

                }
            }
        });
    }

    public class RegisterUser extends AsyncTask<String, Void, String> {
        HttpHandler httpHandler;
        String username, password;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        protected String doInBackground(String... params) {
            username = params[0];
            password = params[1];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(url_register); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
                postDataParams.put("password", password);
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

                if (responseCode == HttpsURLConnection.HTTP_OK) {

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
                    return new String("false");
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressDialog();
            Log.d(TAG, result);
            if (result != null){
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200){
                        Toasty.success(SignUpActivity.this, "Register successful!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        finish();
                    }else{
                        Toasty.success(SignUpActivity.this, "Username is existed!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toasty.success(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
