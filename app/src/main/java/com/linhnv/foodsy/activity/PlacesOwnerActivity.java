package com.linhnv.foodsy.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.linhnv.foodsy.R;
import com.linhnv.foodsy.adapter.OwnerApdater;
import com.linhnv.foodsy.adapter.PlaceAdapter;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PlacesOwnerActivity extends BaseActivity {
    private static final String TAG = PlacesOwnerActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private OwnerApdater placeAdapter;
    private List<Places> placeList;
    SP sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_owner);
        init();
        placeList = new ArrayList<>();
        sp = new SP(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(PlacesOwnerActivity.this, AddplaceActivity.class);
                startActivity(i);
            }
        });

        String token = sp.getToken();
        Log.d(TAG, "tokenHome: "+ token);
        new GetPlaceOwner().execute(token);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlacesOwnerActivity.this, ViewPlacesOwner.class));
            }
        });
    }
    public void init(){
        recyclerView = (RecyclerView) findViewById(R.id.rcy_owner);
    }
    public class GetPlaceOwner extends AsyncTask<String, Void, String> {
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
            String jsonStr = sh.makeServiceCall(ApiURL.URL_PLACE_OWNER + "?token=" + token);
            Log.e(TAG, "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("result owner", result.toString());
            hideProgressDialog();
            if (result != null){
                if (result.equals("405")){
                    Toasty.error(PlacesOwnerActivity.this, "Loading error", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200){
                            JSONArray eat = root.getJSONArray("data");
                            for (int i = 0; i < eat.length(); i++) {
                                JSONObject b = eat.getJSONObject(i);
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
                                places.setDescription(description);
                                Log.d("img", url.toString());
                                // adding contact to contact list
                                placeList.add(places);
                                setAdapter();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Log.d(TAG, "Error");
            }
        }
    }
    private void setAdapter(){
        placeAdapter = new OwnerApdater(PlacesOwnerActivity.this, placeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlacesOwnerActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(placeAdapter);
    }

}
