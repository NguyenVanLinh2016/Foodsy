package com.linhnv.foodsy.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.linhnv.foodsy.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;


public class SignInActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgCloseSignIn;
    private TextView txtForgetPass;
    private EditText edtUsernname, edtPassword;
    private Button btnSignin, btnSignInPhone;
    AccessToken accessToken = AccountKit.getCurrentAccessToken();
    public static int APP_REQUEST_CODE = 99;
    private static final String TAG = SignInActivity.class.getSimpleName();
    private int status = 0;
    private String url_signin = "https://foodsy-api.herokuapp.com/api/auth/login";
    private String url_signin_social = "https://foodsy-api.herokuapp.com/api/auth/login/social";
    private String url_get_profile = "https://foodsy-api.herokuapp.com/api/user/profile";
    private String phoneNumberString = "";
    private String token = "";
    //facebook
    private Button button_login_facebook;
    public CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        init();
        txtForgetPass.setOnClickListener(this);
        imgCloseSignIn.setOnClickListener(this);
        btnSignInPhone.setOnClickListener(this);
        btnSignin.setOnClickListener(this);
        button_login_facebook.setOnClickListener(this);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.packagename",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void init(){
        edtUsernname = (EditText) findViewById(R.id.edit_text_username_signin);
        edtPassword = (EditText) findViewById(R.id.edit_text_pass_signin);
        btnSignin = (Button) findViewById(R.id.button_user_signin);
        txtForgetPass = (TextView) findViewById(R.id.txtForgetPass);
        imgCloseSignIn = (ImageView) findViewById(R.id.imgCloseSignIn);
        btnSignInPhone = (Button) findViewById(R.id.btnLoginPhone_signin);
        button_login_facebook = (LoginButton) findViewById(R.id.button_login_facebook);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_user_signin:
                String username = edtUsernname.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                if (username.length() < 6){
                    edtUsernname.setError(getString(R.string.error_message_username));
                    edtUsernname.requestFocus();
                }else if(password.length() < 6){
                    edtPassword.setError(getString(R.string.error_message_password));
                    edtPassword.requestFocus();
                }else{
                    new SignIn().execute(username, password);
                }
                break;
            case R.id.txtForgetPass:
                startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
                finish();
                break;
            case R.id.imgCloseSignIn:
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.btnLoginPhone_signin:
                phoneLogin();
                break;
            case R.id.button_login_facebook:
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(SignInActivity.this, "Success: "+ loginResult, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignInActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(SignInActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                //turn off setting button default facebook
                button_login_facebook.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                break;

        }
    }
    class SignIn extends AsyncTask<String, Void, String>{
        HttpHandler httpHandler;
        String username, password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Authencation...");
        }
        @Override
        protected String doInBackground(String... params) {
            username = params[0];
            password = params[1];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(url_signin); // here is your URL path

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
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null){
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200){
                        JSONObject jsonObject = root.getJSONObject("data");
                        token = jsonObject.getString("token");
                        Log.d(TAG, token);
                        //get user info
                        new GetUserInfo().execute(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toasty.success(SignInActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    class SignInSocial extends AsyncTask<String, String, Integer>{
        String username;
        OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Authencation...");
        }

        @Override
        protected Integer doInBackground(String... params) {
            username = params[0];
            //synchronous
            FormBody.Builder formBuilder = new FormBody.Builder();
            formBuilder.add("username", username);
            RequestBody formBody = formBuilder.build();
            final Request request = new Request.Builder().url(url_signin_social).post(formBody).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.code();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer status) {
            super.onPostExecute(status);
            hideProgressDialog();
            if (status == 200){
                Toasty.success(SignInActivity.this, "Signin successful!!", Toast.LENGTH_SHORT).show();
                //get token

            }else{
                Toasty.error(SignInActivity.this, "Signin fail!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void phoneLogin() {
        final Intent intent = new Intent(SignInActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        configurationBuilder.setDefaultCountryCode("VN"); //set ma vung viet nam cho dien so dien thoai
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE){// confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage = "";
            if(loginResult.getError() != null){
                toastMessage = loginResult.getError().getErrorType().getMessage();
            }else if(loginResult.wasCancelled()){
                toastMessage = "Login Cancelled";
            }else{
                if(loginResult.getAccessToken() != null){
                    //toastMessage = "Success: " +loginResult.getAccessToken().getAccountId();
                    Log.i(TAG, toastMessage);
                }else{
                    //toastMessage = String.format("Success:%s...", loginResult.getAuthorizationCode().substring(0,10));
                    Log.i(TAG, toastMessage);
                }
                // Success! Start your next activity...
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        // Get Account Kit ID
                        String accountKitId = account.getId();
                        Log.e("Account Kit Id", accountKitId);
                        if(account.getPhoneNumber()!=null) {
                            Log.e("CountryCode", "" + account.getPhoneNumber().getCountryCode());
                            Log.e("PhoneNumber", "" + account.getPhoneNumber().getPhoneNumber());
                            //call login get token and then check user


                            // Get phone number
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            phoneNumberString = phoneNumber.toString();
                            sendPhoneNumber(phoneNumberString);
                            Log.e("NumberString", phoneNumberString);
                        }

                        if(account.getEmail()!=null)
                            Log.e("Email",account.getEmail());
                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        // Handle Error
                        Log.e(TAG,error.toString());
                    }
                });
                toastMessage = "Login successful!!";
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        }
    }

    private void sendPhoneNumber(String phone){
        Intent intent = new Intent(SignInActivity.this, UpdateInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumberString", phone);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public class GetUserInfo extends AsyncTask<String, String, String>{
        HttpHandler httpHandler;
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Athencation...");
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeServiceCall(url_get_profile +"?token="+token);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null){
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200){
                        JSONObject jsonObject = root.getJSONObject("data");
                        String gender = jsonObject.getString("gender");
                        if (gender.equalsIgnoreCase("n")){
                            Intent intent = new Intent(SignInActivity.this, UpdateInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("token", token);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                    }else{
                        Toasty.success(SignInActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toasty.success(SignInActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
