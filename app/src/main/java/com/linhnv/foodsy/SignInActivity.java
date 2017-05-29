package com.linhnv.foodsy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgCloseSignIn;
    private TextView txtForgetPass;
    private Button btnSignInPhone;
    AccessToken accessToken = AccountKit.getCurrentAccessToken();
    public static int APP_REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        anhXa();
        txtForgetPass.setOnClickListener(this);
        imgCloseSignIn.setOnClickListener(this);
        btnSignInPhone.setOnClickListener(this);
    }
    public void phoneLogin() {
        final Intent intent = new Intent(SignInActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        configurationBuilder.setDefaultCountryCode("VN"); //set ma vung viet nam cho dien so dien thoai
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    private void anhXa(){
        txtForgetPass = (TextView) findViewById(R.id.txtForgetPass);
        imgCloseSignIn = (ImageView) findViewById(R.id.imgCloseSignIn);
        btnSignInPhone = (Button) findViewById(R.id.btnLoginPhone_signin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

        }
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
                    toastMessage = "Success: " +loginResult.getAccessToken().getAccountId();
                    getAccount();
                }else{
                    toastMessage = String.format("Success:%s...", loginResult.getAuthorizationCode().substring(0,10));
                }
                // Success! Start your next activity...
                //startActivity(new Intent(MainActivity.this, ControllerActivity.class));
                //finish();
            }
            // Surface the result to your user in an appropriate way.
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        }
    }
    private void getAccount(){
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();
                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = phoneNumber.toString();
                Toast.makeText(SignInActivity.this, ""+ accountKitId, Toast.LENGTH_SHORT).show();
                Log.d("MainAcitivity", phoneNumberString);
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.d("MainAcitivity", "Error");
            }
        });
    }
}
