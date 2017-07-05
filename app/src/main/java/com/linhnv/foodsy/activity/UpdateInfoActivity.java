package com.linhnv.foodsy.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.network.HttpHandler;
import com.linhnv.foodsy.model.SP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

public class UpdateInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = UpdateInfoActivity.class.getSimpleName();
    private String phoneNumber;
    private EditText editText_fullname, editText_email, editText_address, editText_phone;
    private RadioGroup radio_sex;
    private RadioButton radio_male, radio_female;
    private Button button_update_info;
    private ImageView image_avatar;
    private String token = "";
    private String url_update_info = "https://foodsyapp.herokuapp.com/api/user/update";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    //sp
    private SP sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        init();
        //Get Sp
        sp = new SP(this);
        phoneNumber =  sp.getPhoneNumber();
        if (phoneNumber.length() > 0){
            editText_phone.setText(phoneNumber);
        }
        button_update_info.setOnClickListener(this);
        token = getIntent().getExtras().getString("token");
        sp = new SP(this);
    }
    private void init(){
        editText_fullname = (EditText) findViewById(R.id.edit_text_fullname);
        editText_email = (EditText) findViewById(R.id.edit_text_email);
        editText_address = (EditText) findViewById(R.id.edit_text_address);
        editText_phone = (EditText) findViewById(R.id.edit_text_phone);
        radio_sex = (RadioGroup) findViewById(R.id.radio_sex);
        radio_male = (RadioButton) findViewById(R.id.radio_male);
        radio_female = (RadioButton) findViewById(R.id.radio_female);
        button_update_info = (Button) findViewById(R.id.button_update_info);
        image_avatar = (ImageView) findViewById(R.id.image_avatar);
        image_avatar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_update_info:
                int selectId = radio_sex.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectId);
                String fullname = editText_fullname.getText().toString().trim();
                String email = editText_email.getText().toString().trim();
                String address = editText_address.getText().toString().trim();
                String phone = editText_phone.getText().toString();
                if (fullname.length() == 0){
                    editText_fullname.setError(getString(R.string.error_message_fullname));
                    editText_fullname.requestFocus();
                }else if (selectId == -1){
                    Toasty.error(UpdateInfoActivity.this, getString(R.string.error_message_sex), Toast.LENGTH_SHORT).show();
                }else if (email.length() > 0){
                    if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
                        Toasty.error(UpdateInfoActivity.this, getString(R.string.error_message_email), Toast.LENGTH_SHORT).show();
                        editText_email.requestFocus();
                    }else{
                        String sex = String.valueOf(radioButton.getText());
                        if(sex.equalsIgnoreCase("Nam")){
                            sex = "m";
                        }else{
                            sex = "f";
                        }
                        new UpdateUserInfo().execute(token, fullname, email, address, phone, sex);
                    }
                }else{
                    String sex = String.valueOf(radioButton.getText());
                    if(sex.equalsIgnoreCase("Nam")){
                        sex = "m";
                    }else{
                        sex = "f";
                    }
                    new UpdateUserInfo().execute(token, fullname, email, address, phone, sex);
                }
                break;
            case R.id.image_avatar:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
        }
    }
    class UpdateUserInfo extends AsyncTask<String, Void, String>{
        HttpHandler httpHandler;
        String token, fullname, email, address, phone, sex;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Update infomation...");
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            fullname = params[1];
            email = params[2];
            address = params[3];
            phone = params[4];
            sex = params[5];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(url_update_info); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("display_name", fullname);
                postDataParams.put("email", email);
                postDataParams.put("address", address);
                postDataParams.put("phone_number", phone);
                postDataParams.put("gender", sex);
                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000 /* milliseconds */);
                conn.setConnectTimeout(20000 /* milliseconds */);
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
            super.onPostExecute(result);
            hideProgressDialog();
            try {
                JSONObject root = new JSONObject(result);
                int status = root.getInt("status");
                if (status == 200){
                    sp.setStateLogin(true);
                    startActivity(new Intent(UpdateInfoActivity.this, MenuActivity.class));
                    finish();
                    Toasty.success(UpdateInfoActivity.this, "Update info successful!", Toast.LENGTH_SHORT).show();
                }else{
                    Toasty.success(UpdateInfoActivity.this, "Update fail!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image_avatar.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle(R.string.dialog_title_update);
        builer.setMessage(R.string.dialog_message_update);
        builer.setPositiveButton(R.string.dialog_button_Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sp.setStateLogin(true);
                startActivity(new Intent(UpdateInfoActivity.this, MenuActivity.class));
                finish();
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
