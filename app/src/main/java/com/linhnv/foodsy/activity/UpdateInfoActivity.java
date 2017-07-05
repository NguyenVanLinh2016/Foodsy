package com.linhnv.foodsy.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.linhnv.foodsy.R;
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
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateInfoActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = UpdateInfoActivity.class.getSimpleName();
    private String phoneNumber;
    private EditText editText_fullname, editText_email, editText_address, editText_phone;
    private RadioGroup radio_sex;
    private RadioButton radio_male, radio_female;
    private Button button_update_info;
    private ImageView image_avatar;
    private String token = "";
    private String url_update_info = "https://foodsy-api.herokuapp.com/api/user/update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        init();
        //phoneNumber = getIntent().getExtras().getString("phoneNumberString");
        button_update_info.setOnClickListener(this);
        token = getIntent().getExtras().getString("token");

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
                String phone = "0988038478";
                String sex = String.valueOf(radioButton.getText());
                if (fullname.length() == 0){
                    editText_fullname.setError(getString(R.string.error_message_fullname));
                }else if (!radio_male.isChecked() || radio_female.isChecked()){
                    Toasty.error(UpdateInfoActivity.this, getString(R.string.error_message_sex), Toast.LENGTH_SHORT).show();
                }else{
                    new UpdateUserInfo().execute(token, fullname, email, address, sex);
                }
                break;
        }
    }

    public void showPopupMenu(View v){
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_takepicture, popup.getMenu());
        popup.show();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.choose_image:
                Toast.makeText(this, "here", Toast.LENGTH_SHORT).show();
                chooseImage();
                return true;
            case R.id.take_picture:

                return true;
            default:
                return false;
        }
    }
    public void chooseImage() {
        Log.d(TAG, "Choose image");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 69);
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
            sex = params[4];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(url_update_info); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("display_name", fullname);
                postDataParams.put("email", email);
                postDataParams.put("address", address);
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
            Log.d(TAG, result);
        }
    }
    public void test(){
        final OkHttpClient client = new OkHttpClient();


    }
}
