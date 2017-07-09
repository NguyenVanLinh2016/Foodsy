package com.linhnv.foodsy.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.linhnv.foodsy.activity.HomeActivity;
import com.linhnv.foodsy.activity.MenuActivity;
import com.linhnv.foodsy.activity.SignInActivity;
import com.linhnv.foodsy.activity.UpdateInfoActivity;
import com.linhnv.foodsy.adapter.PlaceAdapterShimmer;
import com.linhnv.foodsy.model.ClickListener;
import com.linhnv.foodsy.model.Notifications;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.adapter.PlaceAdapter;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.RecyclerTouchListenerHome;
import com.linhnv.foodsy.activity.PlaceDetailActivity;
import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.SP;
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

/**
 * Created by linhnv on 21/05/2017.
 */

public class HomeFragment extends BaseFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private List<Places> placeList;
    private PlaceAdapterShimmer placeAdapterShimmer;
    private SP sp;
    private String url_places = null;
    private String url_img = "https://foodsyapp.herokuapp.com/api/place/photo";
    private String url_place_eat = "https://foodsyapp.herokuapp.com/api/place/category/eat";
    private String url_place_drink = "https://foodsyapp.herokuapp.com/api/place/category/drink";
    private String url_place_entertain = "https://foodsyapp.herokuapp.com/api/place/category/entertain";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListenerHome(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Places places = placeList.get(position);
                Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
                Bundle b = new Bundle();
                b.putInt("id", places.getId());
                b.putDouble("latitude", places.getLatitude());
                b.putDouble("longitude", places.getLongitude());
                b.putString("display_name", places.getDisplay_name());
                b.putString("url_image", places.getPhoto());
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
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
            String jsonStr = sh.makeServiceCall(url_places + "?latitude=" + latitude + "&longitude="
                    + longitude + "&token=" + token);
            Log.e(TAG, "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null){
                if (result.equals("405")){
                    Toasty.error(getActivity(), "Loading error", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200){
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
                                Double latitude = b.getDouble("latitude");
                                Double longitude = b.getDouble("longitude");
                                int id = b.getInt("id");
                                String url = url_img + "?token=" + token + "&id=" + id;
                                // tmp hash map for single contact
                                Places places = new Places();
                                // adding each child node to HashMap key => value
                                places.setId(id);
                                places.setDisplay_name(display_name);
                                places.setAddress(address);
                                places.setLatitude(latitude);
                                places.setLongitude(longitude);
                                places.setCity(String.valueOf(sochan));
                                places.setPhoto(url);
                                Log.d(TAG, places.toString());
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
        placeAdapter = new PlaceAdapter(getActivity(), placeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(placeAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        placeList = new ArrayList<>();
        sp = new SP(getContext());
        Log.d("User info", sp.getUser());
        Intent i = getActivity().getIntent();
        Bundle b = i.getExtras();

        int url_eat = b.getInt("eat");
        if (url_eat == 0) {
            url_places = url_place_eat;
        } else if (url_eat == 1) {
            url_places = url_place_entertain;
        } else if (url_eat == 2) {
            url_places = url_place_drink;
        }
        String token = sp.getToken();
        Log.d(TAG, "tokenHome: "+ token);
        new GetEatInfo().execute(String.valueOf(sp.getLatitude()), String.valueOf(sp.getLongitude()), token);
    }
}
