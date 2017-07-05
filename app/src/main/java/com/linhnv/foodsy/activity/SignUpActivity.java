package com.linhnv.foodsy.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.SP;
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
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener{

    private ImageView imgCloseSignUp;
    private Button btnSignUp, btnLoginPhone_signup;
    private EditText edtUsername, edtPassword, edtConfirmPass;
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private int status = 0;
    AccessToken accessToken = AccountKit.getCurrentAccessToken();
    public static int APP_REQUEST_CODE = 99;
    private String url_register = "https://foodsyapp.herokuapp.com/api/auth/register";
    private String url_signin_social = "https://foodsyapp.herokuapp.com/api/auth/login/social";
    private String url_get_profile = "https://foodsyapp.herokuapp.com/api/user/profile";
    private String phoneNumberString = "";
    //facebook
    private LoginButton button_login_fb_signup;
    public CallbackManager callbackManager;
    //sp
    private SP sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        imgCloseSignUp.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnLoginPhone_signup.setOnClickListener(this);
        button_login_fb_signup.setOnClickListener(this);
        sp = new SP(this);
    }

    private void init(){
        imgCloseSignUp = (ImageView) findViewById(R.id.imgCloseSignUp);
        btnSignUp = (Button) findViewById(R.id.btnSignUp_signup);
        edtUsername = (EditText) findViewById(R.id.edtUsername_signup);
        edtPassword = (EditText) findViewById(R.id.edtPass_signup);
        edtConfirmPass = (EditText) findViewById(R.id.edtConfirmPass_signup);
        btnLoginPhone_signup = (Button) findViewById(R.id.btnLoginPhone_signup);
        button_login_fb_signup = (LoginButton) findViewById(R.id.button_login_fb_signup);
        //login facebook
        button_login_fb_signup.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        button_login_fb_signup.setCompoundDrawablePadding(0);
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
            case R.id.btnLoginPhone_signup:
                phoneLogin();
                break;
            case R.id.button_login_fb_signup:
                callbackManager = CallbackManager.Factory.create();
                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {

                            }

                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                            }
                        });
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
                //button_login_facebook.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday"));

                button_login_fb_signup.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest mGraphRequest = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject me, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            //Toast.makeText(SignInActivity.this, "Login facebook error 12121", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String username = me.optString("email");
                                            new SignInSocial().execute(username);
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        mGraphRequest.setParameters(parameters);
                        mGraphRequest.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toasty.error(SignUpActivity.this, "Login on Cancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toasty.error(SignUpActivity.this, "Login on Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    public class RegisterUser extends AsyncTask<String, Void, String> {
        HttpHandler httpHandler;
        String username, password;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }
        @Override
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
            if (result.equals("422")){
                Toasty.error(SignUpActivity.this, "Username is exist!", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 201){
                        Toasty.success(SignUpActivity.this, "Register successful!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                        finish();
                    }else{
                        Toasty.error(SignUpActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class SignInSocial extends AsyncTask<String, Void, String>{
        HttpHandler httpHandler;
        String username;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Authencation...");
        }
        @Override
        protected String doInBackground(String... params) {
            username = params[0];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(url_signin_social); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
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
            try {
                JSONObject root = new JSONObject(result);
                JSONObject data = root.getJSONObject("data");
                String token = data.getString("token");
                new GetUserInfo().execute(token);
                //if login facebook successful, call method logout because save state in app
                LoginManager.getInstance().logOut();
            } catch (JSONException e) {
                hideProgressDialog();
                e.printStackTrace();
            }
        }
    }
    public void phoneLogin() {
        final Intent intent = new Intent(SignUpActivity.this, AccountKitActivity.class);
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
        //account kit
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
                            // Get phone number
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            phoneNumberString = phoneNumber.toString();
                            String phone = "0"+ phoneNumberString.substring(3, phoneNumberString.length());
                            //save phone in sp
                            sp.setPhoneNumber(phone);
                            //call login get token and then check user
                            new SignInSocial().execute(phone);
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
            Toasty.success(SignUpActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        }else{
            //facebook
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    public class GetUserInfo extends AsyncTask<String, String, String>{
        HttpHandler httpHandler;
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog("Athencation...");
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
            try {
                JSONObject root = new JSONObject(result);
                int status = root.getInt("status");
                if (status == 200){
                    JSONObject jsonObject = root.getJSONObject("data");
                    String gender = jsonObject.getString("gender");
                    if (gender.equalsIgnoreCase("n")){
                        Intent intent = new Intent(SignUpActivity.this, UpdateInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("token", token);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }else{
                        startActivity(new Intent(SignUpActivity.this, MenuActivity.class));
                        finish();
                    }
                }else{
                    Toasty.success(SignUpActivity.this, "Error!!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }
}
