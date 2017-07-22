package com.linhnv.foodsy.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.fragment.BaseFragment;
import com.linhnv.foodsy.model.Category;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.HttpHandler;
import com.thomashaertel.widget.MultiSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

public class RegisterOwnerActivity extends BaseActivity {
    private String url_register = "https://foodsyapp.herokuapp.com/api/user/owner/register";
    private String url_category = "https://foodsyapp.herokuapp.com/api/category/place";
    private SP sp;
    private Button btn_register;
    private MultiSpinner multiSpiner;
    List<Category> categoryList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);
        init();
        sp = new SP(this);
        final String token = sp.getToken();
        categoryList = new ArrayList<>();
        new RegisterOwner().execute(token);
        for (int i = 0; i < categoryList.size(); i++) {
            Log.e("adapter", categoryList.get(i).getName());
        }
//        adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item);
////        for ()
//        adapter.add("Thi?t k? Website");
//        adapter.add("CNTT - ?ng d?ng ph?n m?m");
//        adapter.add("L?p trÏnh di d?ng");
//        adapter.add("Thi?t k? d? h?a");
//        adapter.add("Hu?ng d?n du l?ch");
//        adapter.add("Qu?n tr? l? h‡nh");
//        adapter.add("Qu?n tr? nh‡ h‡ng");
//        adapter.add("Qu?n tr? kh·ch s?n");
//        adapter.add("K? to·n doanh nghi?p");
//        adapter.add("QTDN - Marketing & Sales");
//        adapter.add("TM–T - Digital & Online Marketing");
//        adapter.add("QHCC - PR & T? Ch?c S? Ki?n");
//        Log.e("adapter", adapter.toString());
//        multiSpiner.setAdapter(adapter, false, onSelectedListener);
//        // set initial selection
//        boolean[] selectedItems = new boolean[adapter.getCount()];
//        selectedItems[0] = true; //chonj item dau tien
//        multiSpiner.setSelected(selectedItems);

    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    builder.append(adapter.getItem(i)).append(",");
                }
            }
        }
    };

    private void init() {
        multiSpiner = (MultiSpinner) findViewById(R.id.sp_category);
    }

    public class RegisterOwner extends AsyncTask<String, Void, String> {
        String latitude, longitude, token;

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
            String jsonStr = sh.makeServiceCall(url_category + "?token=" + token);
            Log.e("Response", "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            Log.e("data return", result.toString());
            if (result != null) {
                if (result.equals("405")) {
                    Toasty.error(RegisterOwnerActivity.this, "Loading error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200) {
                            JSONArray eat = root.getJSONArray("data");
                            for (int i = 0; i < eat.length(); i++) {
                                JSONObject c = eat.getJSONObject(i);
                                String name = c.getString("name");
                                String description = c.getString("description");

                                Category category = new Category();
                                category.setName(name);
                                category.setDescription(description);
                                // adding contact to contact list
                                //arrayList.add(category);
                                categoryList.add(category);
                                hideProgressDialog();
                            }
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
}
