package com.linhnv.foodsy.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.linhnv.foodsy.model.ImageUtils;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.RealPathUtil;
import com.linhnv.foodsy.model.User;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;
import com.linhnv.foodsy.model.SP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = UpdateInfoActivity.class.getSimpleName();
    private String phoneNumber;
    private EditText editText_fullname, editText_email, editText_address, editText_phone;
    private RadioGroup radio_sex;
    private RadioButton radio_male, radio_female;
    private Button button_update_info;
    private ImageView image_avatar;
    private String token = "";
    //sp
    private SP sp;
    //camera
    private final CharSequence[] items = {"Chụp ảnh", "Tải ảnh lên"};
    private static final int REQUEST_CAMERA = 1001;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 2;
    private static final int REQUEST_GALLERY = 1002;
    private boolean flag = false;
    private Uri selectedImageUri;
    private Bitmap mBitmap;
    private File actualImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        init();
        //Get Sp
        sp = new SP(this);
        phoneNumber = sp.getPhoneNumber();
        if (phoneNumber.length() > 0) {
            editText_phone.setText(phoneNumber);
        }
        button_update_info.setOnClickListener(this);
        token = getIntent().getExtras().getString("token");
        sp = new SP(this);
    }

    public String token() {
        sp = new SP(getApplicationContext());
        String token = sp.getToken();
        return token;
    }

    private void init() {
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
        switch (v.getId()) {
            case R.id.button_update_info:
                int selectId = radio_sex.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectId);
                String fullname = editText_fullname.getText().toString().trim();
                String email = editText_email.getText().toString().trim();
                String address = editText_address.getText().toString().trim();
                String phone = editText_phone.getText().toString();
                if (fullname.length() == 0) {
                    editText_fullname.setError(getString(R.string.error_message_fullname));
                    editText_fullname.requestFocus();
                } else if (selectId == -1) {
                    Toasty.error(UpdateInfoActivity.this, getString(R.string.error_message_sex), Toast.LENGTH_SHORT).show();
                } else if (email.length() > 0) {
                    if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
                        Toasty.error(UpdateInfoActivity.this, getString(R.string.error_message_email), Toast.LENGTH_SHORT).show();
                        editText_email.requestFocus();
                    } else {
                        String sex = String.valueOf(radioButton.getText());
                        if (sex.equalsIgnoreCase("Nam")) {
                            sex = "m";
                        } else {
                            sex = "f";
                        }
                        new UpdateUserInfo().execute(token, fullname, email, address, phone, sex);
                        try {
                            execMultipartPost();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    String sex = String.valueOf(radioButton.getText());
                    if (sex.equalsIgnoreCase("Nam")) {
                        sex = "m";
                    } else {
                        sex = "f";
                    }
                    new UpdateUserInfo().execute(token, fullname, email, address, phone, sex);
                    try {
                        execMultipartPost();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.image_avatar:
                openFileChooserDialog();
                break;
        }
    }

    class UpdateUserInfo extends AsyncTask<String, Void, String> {
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
                URL url = new URL(ApiURL.URL_UPDATE_INFO); // here is your URL path
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
                if (status == 200) {
                    sp.setStateLogin(true);
                    startActivity(new Intent(UpdateInfoActivity.this, MenuActivity.class));
                    finish();
                    Toasty.success(UpdateInfoActivity.this, "Update info successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.success(UpdateInfoActivity.this, "Update fail!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
    private void openFileChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn ảnh");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        initCameraPermission();
                        break;
                    case 1:
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        break;
                    default:
                }
            }
        });
        builder.show();
    }
    //take a picture
    @TargetApi(Build.VERSION_CODES.M)
    private void initCameraPermission() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Permission to use Camera", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        } else {
            initCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCameraIntent();
            } else {
                Toast.makeText(this, "Bạn không cho phép sử camera lúc này!!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initGalleryIntent();
            } else {
                Toast.makeText(this, "Bạn không cho phép sử ghi hình ảnh lúc này!!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void initCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getOutputMediafile(1);
        selectedImageUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }
    private File getOutputMediafile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name)
        );
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

    String realPath = "";

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                realPath = selectedImageUri.getPath();
            } else if (requestCode == REQUEST_GALLERY) {
                selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 11) {
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
                } else if (Build.VERSION.SDK_INT < 19) {
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
                } else {
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
                }

                Log.d(TAG, "real path: " + realPath);
            }
            mBitmap = ImageUtils.getScaledImage(selectedImageUri, this);
            setImageBitmap(mBitmap);
        }
    }
    private void setImageBitmap(Bitmap bm) {
        image_avatar.setImageBitmap(bm);
    }
    private void execMultipartPost() throws Exception {
        File file = new File(realPath);
        final String contentType = file.toURL().openConnection().getContentType();

        Log.d(TAG, "file: " + file.getPath());
        Log.d(TAG, "contentType: " + contentType);

        //composer
        new Compressor(this)
                .compressToFileAsFlowable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File files) {
                        actualImage = files;

                        RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), actualImage);

                        final String filename = "file_" + System.currentTimeMillis() / 1000L;

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("token", token)
                                .addFormDataPart("photo", filename + ".jpg", fileBody)
                                .build();

                        Request request = new Request.Builder()
                                .url(ApiURL.URL_UPDATE_INFO)
                                .post(requestBody)
                                .build();

                        OkHttpClient okHttpClient = new OkHttpClient();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, final IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "Success");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, final Response response) throws IOException {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Log.d("TEST", "Fail");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        Log.d("TEST", throwable.getMessage());
                    }
                });

    }
}
