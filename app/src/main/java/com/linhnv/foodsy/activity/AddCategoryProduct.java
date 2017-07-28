package com.linhnv.foodsy.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.adapter.CategoryAdapter;
import com.linhnv.foodsy.adapter.PlaceAdapter;
import com.linhnv.foodsy.fragment.HomeFragment;
import com.linhnv.foodsy.model.Category;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;

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

public class AddCategoryProduct extends BaseActivity {
    private static final String TAG = AddCategoryProduct.class.getSimpleName();
    private EditText ed_decription, ed_name;
    private String token;
    private SP sp;
    FloatingActionButton fab_category;
    RecyclerView rcy_categoryproduct;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private int int_category, id_placeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_product);
        init();
        categoryList = new ArrayList<>();
        sp = new SP(this);
        token = sp.getToken();
        int_category = getIntent().getExtras().getInt("id_category");
        fab_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductCategory();
            }
        });
        rcy_categoryproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddCategoryProduct.this, AddProduct.class);
                Bundle b = new Bundle();
                b.putInt("category", int_category);
                i.putExtras(b);
                startActivity(i);
            }
        });

        new ViewProductOwner().execute(token);
    }

    public void init() {
        fab_category = (FloatingActionButton) findViewById(R.id.fab_category);
        rcy_categoryproduct = (RecyclerView) findViewById(R.id.rcy_categoryproduct);
    }

    public void addProductCategory() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_dialog, null);
        builder.setView(view);
        ed_name = (EditText) view.findViewById(R.id.ed_add_name_category);
        ed_decription = (EditText) view.findViewById(R.id.ed_add_decription_category);
        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = ed_name.getText().toString().trim();
                String decription = ed_decription.getText().toString().trim();
                new RegisterCategory().execute(token, name, decription, String.valueOf(int_category));
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }

    //Category product
    public class RegisterCategory extends AsyncTask<String, Void, String> {
        HttpHandler httpHandler;
        String token, name, description, place_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            name = params[1];
            description = params[2];
            place_id = params[3];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(ApiURL.URL_PLACE_CATEGORY_PRODUCT); // here is your URL path
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("name", name);
                postDataParams.put("description", description);
                postDataParams.put("place_id", place_id);
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
            if (result.equals("200")) {
                Toasty.success(AddCategoryProduct.this, "Thêm loại món ăn thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toasty.error(AddCategoryProduct.this, "Lỗi đăng ký!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class ViewProductOwner extends AsyncTask<String, Void, String> {
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
            String jsonStr = sh.makeServiceCall("https://foodsyapp.herokuapp.com/api/place/" + int_category + "/productcategory?token=" + token);
            Log.e(TAG, "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null) {
                if (result.equals("405")) {
                    Toasty.error(AddCategoryProduct.this, "Loading error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200) {
                            JSONArray eat = root.getJSONArray("data");
                            for (int i = 0; i < eat.length(); i++) {
                                JSONObject c = eat.getJSONObject(i);
                                String id = c.getString("id");
                                String name = c.getString("name");
                                String description = c.getString("description");

                                // tmp hash map for single contact
                                Category category = new Category();
                                // adding each child node to HashMap key => value
                                category.setId(Integer.parseInt(id));
                                category.setName(name);
                                category.setDescription(description);
                                // adding contact to contact list
                                categoryList.add(category);
                                setAdapter();
                            }
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

    private void setAdapter() {
        categoryAdapter = new CategoryAdapter(this, categoryList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcy_categoryproduct.setLayoutManager(linearLayoutManager);
        rcy_categoryproduct.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
    }
}
