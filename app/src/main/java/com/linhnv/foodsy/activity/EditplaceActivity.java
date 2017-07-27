package com.linhnv.foodsy.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Category;
import com.linhnv.foodsy.model.ImageUtils;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.RealPathUtil;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;
import com.squareup.picasso.Picasso;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import static com.linhnv.foodsy.activity.UpdateInfoActivity.EMAIL_ADDRESS_PATTERN;

public class EditplaceActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = EditplaceActivity.class.getSimpleName();
    private SP sp;
    private EditText ed_display_name, ed_description, ed_address, ed_phone_number, ed_email, ed_price_limit, ed_time_open, ed_time_close, ed_wifi_password;
    private String latitude;
    private String longitude;
    private int user_id;
    private Spinner sp_category;
    private Button btn_registerplaces, btn_clear;
    private int category_id;
    List<Category> categoryList;
    ImageView img_location, image_add_places;

    //camera
    private final CharSequence[] items = {"Chụp ảnh", "Tải ảnh lên"};
    private static final int REQUEST_CAMERA = 1005;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 2;
    private static final int REQUEST_GALLERY = 1006;
    private Uri selectedImageUri;
    private Bitmap mBitmap;
    private File actualImage;
    String realPath = "";
    String id_places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplace);
        int id_get = getIntent().getExtras().getInt("id");
        id_places = String.valueOf(id_get);
        init();
        categoryList = new ArrayList<>();
        sp = new SP(this);
        String token = sp.getToken();
        new RegisterAddActivity().execute(token);
        btn_registerplaces.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        img_location.setOnClickListener(this);
        image_add_places.setOnClickListener(this);
        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_id = position + 1;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        new GetPlaces().execute(token);
        //location();


    }


    public class RegisterAddActivity extends AsyncTask<String, Void, String> {
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(ApiURL.url_category + "?token=" + token);
            Log.e("Response", "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            Log.e("TEST", result.toString());
            if (result != null) {
                if (result.equals("405")) {
                    Toasty.error(EditplaceActivity.this, "Loading error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200) {
                            JSONArray eat = root.getJSONArray("data");
                            for (int i = 0; i < eat.length(); i++) {
                                JSONObject c = eat.getJSONObject(i);
                                int id = c.getInt("id");
                                String name = c.getString("name");
                                String description = c.getString("description");

                                Category category = new Category();
                                category.setId(id);
                                category.setName(name);
                                category.setDescription(description);
                                categoryList.add(category);
                            }
                            setSpinner();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.d("error", "Error");
            }
        }
    }

    private void setSpinner() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            list.add(categoryList.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);
        sp_category.setAdapter(adapter);
    }

    private void init() {
        ed_display_name = (EditText) findViewById(R.id.edit_text_nameproduct);
        ed_description = (EditText) findViewById(R.id.edit_text_descriptionproduct);
        ed_address = (EditText) findViewById(R.id.edit_text_address);
        ed_phone_number = (EditText) findViewById(R.id.edit_text_phone);
        ed_email = (EditText) findViewById(R.id.edit_text_email);
        ed_price_limit = (EditText) findViewById(R.id.edit_text_price_limit);
        ed_time_open = (EditText) findViewById(R.id.edit_text_time_open);
        ed_time_close = (EditText) findViewById(R.id.edit_text_time_close);
        ed_wifi_password = (EditText) findViewById(R.id.edit_text_wifi_restaurants);
        sp_category = (Spinner) findViewById(R.id.sp_category2);
        btn_registerplaces = (Button) findViewById(R.id.btn_registerplaces2);
        btn_clear = (Button) findViewById(R.id.btn_clear2);
        img_location = (ImageView) findViewById(R.id.img_location);
        image_add_places = (ImageView) findViewById(R.id.image_add_places);

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
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Log.d("Permission", "Permission to use Camera");
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        } else {
            initCameraIntent();
        }
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
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_PLACES" +
                    "" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }

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
        image_add_places.setImageBitmap(bm);
    }

    private void execMultipartPost() throws Exception {
        File file = new File(realPath);
        final String contentType = file.toURL().openConnection().getContentType();
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
                                .addFormDataPart("token", sp.getToken())
                                .addFormDataPart("id", id_places)
                                .addFormDataPart("photo", filename + ".jpg", fileBody)
                                .build();

                        Request request = new Request.Builder()
                                .url(ApiURL.URL_PLACES_UPDATE)
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_add_places:
                openFileChooserDialog();
                break;
            case R.id.btn_registerplaces2:
                String token = sp.getToken();
                String display_name_add = ed_display_name.getText().toString().trim();
                String description_add = ed_description.getText().toString().trim();
                String address_add = ed_address.getText().toString().trim();
                String phone_number_add = ed_phone_number.getText().toString().trim();
                String email_add = ed_email.getText().toString().trim();
                String price_limit_add = ed_price_limit.getText().toString().trim();
                String time_open_add = ed_time_open.getText().toString().trim();
                String time_close_add = ed_time_close.getText().toString().trim();
                String wifi_password_add = ed_wifi_password.getText().toString().trim();
                if (display_name_add.length() == 0) {
                    ed_display_name.setError(getString(R.string.error_message_name));
                    ed_display_name.requestFocus();
                } else if (description_add.length() == 0) {
                    ed_description.setError(getString(R.string.error_message_description));
                    ed_description.requestFocus();
                } else if (address_add.length() == 0) {
                    ed_address.setError(getString(R.string.error_message_description));
                    ed_address.requestFocus();
                } else if (phone_number_add.length() == 0) {
                    ed_phone_number.setError(getString(R.string.error_message_description));
                    ed_phone_number.requestFocus();
                } else if (!EMAIL_ADDRESS_PATTERN.matcher(email_add).matches()) {
                    Toasty.error(EditplaceActivity.this, getString(R.string.error_message_email), Toast.LENGTH_SHORT).show();
                    ed_email.requestFocus();
                } else {

                    new EditPlaces().execute(token, display_name_add, description_add, address_add,
                            phone_number_add, email_add, price_limit_add, time_open_add, time_close_add,
                            wifi_password_add, latitude, longitude, String.valueOf(category_id), id_places);

                }
                break;
            case R.id.img_location:
                ed_address.setEnabled(true);
                break;
        }
    }

    public class EditPlaces extends AsyncTask<String, Void, String> {
        HttpHandler httpHandler;
        String token, display_name, description, address, phone_number, email, price_limit, time_open, time_close, wifi_password, latitude, longitude, category_id, id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            display_name = params[1];
            description = params[2];
            address = params[3];
            phone_number = params[4];
            email = params[5];
            price_limit = params[6];
            time_open = params[7];
            time_close = params[8];
            wifi_password = params[9];
            latitude = params[10];
            longitude = params[11];
            category_id = params[12];
            id = params[13];

            try {
                httpHandler = new HttpHandler();
                URL url = new URL(ApiURL.URL_PLACES_UPDATE); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("display_name", display_name);
                postDataParams.put("description", description);
                postDataParams.put("address", address);
                postDataParams.put("phone_number", phone_number);
                postDataParams.put("email", email);
                postDataParams.put("price_limit", price_limit);
                postDataParams.put("time_open", time_open);
                postDataParams.put("time_close", time_close);
                postDataParams.put("wifi_password", wifi_password);
                postDataParams.put("latitude", latitude);
                postDataParams.put("longitude", longitude);
                postDataParams.put("category_id", category_id);
                postDataParams.put("id", id);

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
                    return new String(String.valueOf(responseCode));
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
                Log.d("result status", result);
                if (status == 200) {
                    JSONObject b = root.getJSONObject("data");
                    String id = b.getString("id");
                    id_places = id;
                    Log.e("id photo", id_places);
                    try {
                        execMultipartPost();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toasty.success(EditplaceActivity.this, "Sửa nhà hàng thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditplaceActivity.this, PlacesOwnerActivity.class));
                    finish();
                } else {
                    Toasty.error(EditplaceActivity.this, "Lỗi đăng ký!!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class GetPlaces extends AsyncTask<String, Void, String> {
        String token;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(ApiURL.URL_PLACE_GET + id_places + "?token=" + token);
            Log.e(TAG, "Response from url edit: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null) {
                if (result.equals("405")) {
                    Toasty.error(EditplaceActivity.this, "Loading error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200) {
                            JSONObject b = root.getJSONObject("data");
                            String display_name = b.getString("display_name");
                            String description = b.getString("description");
                            String address = b.getString("address");
                            String city = b.getString("city");
                            String phone_number = b.getString("phone_number");
                            String email = b.getString("email");
                            Double latitude = b.getDouble("latitude");
                            Double longitude = b.getDouble("longitude");
                            int id = b.getInt("id");
                            String url = ApiURL.URL_IMAGE + "?token=" + token + "&id=" + id;
                            String price_limit = b.getString("price_limit");
                            String time_open = b.getString("time_open");
                            String time_close = b.getString("time_close");
                            String wifi_password = b.getString("wifi_password");
                            // tmp hash map for single contact
                            Places places = new Places();
                            // adding each child node to HashMap key => value

                            places.setDescription(description);
                            Log.d("img", url.toString());
                            // adding contact to contact list

                            places.setId(id);
                            places.setDisplay_name(display_name);
                            places.setAddress(address);
                            places.setPhone_number(phone_number);
                            places.setEmail(email);
                            places.setLatitude(latitude);
                            places.setLongitude(longitude);
                            places.setPrice_limit(price_limit);
                            places.setTime_open(time_open);
                            places.setTime_close(time_close);
                            places.setWifi_password(wifi_password);
                            places.setPhoto(url);

                            ed_display_name.setText(display_name);
                            ed_description.setText(description);
                            ed_address.setText(address);
                            ed_phone_number.setText(phone_number);
                            ed_email.setText(email);
                            ed_price_limit.setText(price_limit);
                            ed_time_open.setText(time_close);
                            ed_time_close.setText(time_close);
                            ed_wifi_password.setText(wifi_password);
                            Picasso.with(EditplaceActivity.this)
                                    .load(places.getPhoto())
                                    .placeholder(R.drawable.bglogin5)
                                    .error(R.drawable.bglogin5)
                                    .into(image_add_places);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.d(TAG, "Error");
            }
        }
    }
}
