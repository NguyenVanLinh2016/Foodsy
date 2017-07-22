package com.linhnv.foodsy.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.ClickListener;
import com.linhnv.foodsy.model.DrawsLineItem;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.adapter.SearchAdapter;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.RecyclerTouchListenerHome;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SearchActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private SearchAdapter mSearchAdapter;
    private ArrayList<Places> mPlaceList;
    private ImageView mBackSearch;
    private SearchView ed_search;
    private SP sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        sp = new SP(this);
        mPlaceList = new ArrayList<>();

        String token = sp.getToken();
        Log.d(TAG, "tokenHome: " + token);
        new GetEatInfo().execute(String.valueOf(sp.getLatitude()), String.valueOf(sp.getLongitude()), token);
        ed_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mSearchAdapter.getFilter().filter(query);
                return false;
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListenerHome(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Places places = mPlaceList.get(position);
                Intent intent = new Intent(SearchActivity.this, PlaceDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt("id", places.getId());
                b.putDouble("latitude", places.getLatitude());
                b.putDouble("longitude", places.getLongitude());
                b.putString("display_name", places.getDisplay_name());
                b.putString("url_image", places.getPhoto());
                b.putString("address", places.getAddress());
                b.putString("phone", places.getPhone_number());
                b.putString("email", places.getEmail());
                b.putString("price", places.getPrice_limit());
                b.putString("time_open", places.getTime_open());
                b.putString("time_close", places.getTime_close());
                b.putString("wifi", places.getWifi_password());
                b.putString("description", places.getDescription());
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    private void init() {
        mBackSearch = (ImageView) findViewById(R.id.image_view_back_search);
        ed_search = (SearchView) findViewById(R.id.edit_search_item);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);
        mBackSearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view_back_search:
                finish();
                break;
        }
    }

    public class GetEatInfo extends AsyncTask<String, Void, String> {
        String latitude, longitude, token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            latitude = params[0];
            longitude = params[1];
            token = params[2];
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(ApiURL.URL_PLACE + "?latitude=" + latitude + "&longitude="
                    + longitude + "&token=" + token);
            Log.e("Response from url: ", jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null) {
                if (result.equals("405")) {
                    Toasty.error(SearchActivity.this, "Loading error", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200) {
                            JSONArray eat = root.getJSONArray("data");
                            for (int i = 0; i < eat.length(); i++) {
                                JSONObject c = eat.getJSONObject(i);
                                String place = c.getString("place");
                                String minutes = c.getString("minutes");
                                int sochan = Math.round(Float.parseFloat(minutes));
                                // Get Json Place
                                JSONObject b = new JSONObject(place);
                                String display_name = b.getString("display_name");
                                String description = b.getString("description");
                                String address = b.getString("address");
                                String city = b.getString("city");
                                String phone_number = b.getString("phone_number");
                                String email = b.getString("email");
                                Double latitude = b.getDouble("latitude");
                                Double longitude = b.getDouble("longitude");
                                int id = b.getInt("id");
                                String url = ApiURL.URL_IMAGE_PLACE + "?token=" + token + "&id=" + id;
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
                                places.setMinutes(String.valueOf(sochan));
                                places.setPhoto(url);
                                places.setDescription(description);
                                Log.d("img", url.toString());
                                // adding contact to contact list
                                mPlaceList.add(places);
                                setAdapter();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.d("Error", "Error");
            }
        }
    }

    private void setAdapter() {
        mSearchAdapter = new SearchAdapter(this, mPlaceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DrawsLineItem(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mSearchAdapter);
    }
}
