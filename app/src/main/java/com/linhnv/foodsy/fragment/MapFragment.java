package com.linhnv.foodsy.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.linhnv.foodsy.R;
import com.linhnv.foodsy.activity.BaseActivity;
import com.linhnv.foodsy.activity.SignInActivity;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

/**
 * Created by linhnv on 21/05/2017.
 */

public class MapFragment extends BaseFragment {
    GoogleMap mGoogleMap;
    private MapView mMapView;
    private SP sp;
    private boolean flag = false;
    private static final String URL_PLACE_AROUND = "https://foodsyapp.herokuapp.com/api/place/around";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        sp = new SP(getActivity());
        final double latitude = sp.getLatitude();
        final double longitude = sp.getLongitude();
        mMapView = (MapView) view.findViewById(R.id.map_main);
        mMapView.onCreate(savedInstanceState);

        showProgressDialog("Loading...");

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                LatLng syndey = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(syndey).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Di An thon"));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(syndey).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        });
        sp.getToken();
        Log.d("TEST", sp.getToken());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    class LoadPlaceAround extends AsyncTask<String, Void, String>{
        HttpHandler httpHandler;
        String latitude, longitude;
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Authencation...");
        }
        @Override
        protected String doInBackground(String... params) {
            latitude = params[0];
            longitude = params[1];
            token = params[2];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(URL_PLACE_AROUND); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("latitude", latitude);
                postDataParams.put("longitude", longitude);
                postDataParams.put("token", token);
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
            Log.d("TEST", result);
//            if (result.equals("422")){
//                Toasty.error(SignInActivity.this, "Username or password is correct", Toast.LENGTH_SHORT).show();
//            }else{
//                try {
//                    JSONObject root = new JSONObject(result);
//                    int status = root.getInt("status");
//                    if (status == 200){
//                        JSONObject jsonObject = root.getJSONObject("data");
//                        token = jsonObject.getString("token");
//                        Log.d(TAG, token);
//                        //get user info
//                        new SignInActivity.GetUserInfo().execute(token);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
