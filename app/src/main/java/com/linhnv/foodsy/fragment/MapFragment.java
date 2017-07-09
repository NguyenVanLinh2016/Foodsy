package com.linhnv.foodsy.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.linhnv.foodsy.R;
import com.linhnv.foodsy.activity.BaseActivity;
import com.linhnv.foodsy.activity.SignInActivity;
import com.linhnv.foodsy.model.DirectionFinderListener;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.Route;
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

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by linhnv on 21/05/2017.
 */

public class MapFragment extends BaseFragment implements DirectionFinderListener, GoogleMap.OnInfoWindowClickListener {
    GoogleMap mGoogleMap;
    private MapView mMapView;
    private SP sp;
    private boolean flag = false;
    private static final String URL_PLACE_AROUND = "https://foodsyapp.herokuapp.com/api/place/around";
    double latitude, longitude;
    private List<Places> listPlace;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMakers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = new SP(getActivity());
        listPlace = new ArrayList<>();
        latitude = sp.getLatitude();
        longitude = sp.getLongitude();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        new LoadPlaceAround().execute(sp.getToken(), String.valueOf(latitude), String.valueOf(longitude));
        mMapView = (MapView) view.findViewById(R.id.map_main);
        mMapView.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                googleMap.addMarker(new MarkerOptions().position(syndey).icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocationgif)));

                Log.d("TEST", listPlace.size() +"--");
                for ( int j=0; j<listPlace.size(); j++ ){
                    Log.d("TEST", listPlace.get(j).getId() +"");
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(listPlace.get(j).getLatitude(), listPlace.get(j).getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title(listPlace.get(j).getDisplay_name()));
                    //marker.showInfoWindow();
                    //createMarker(mGoogleMap, listPlace.get(j).getLatitude(), listPlace.get(j).getLongitude(), listPlace.get(j).getDisplay_name());
                }
                CameraPosition cameraPosition = new CameraPosition.Builder().target(syndey).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        });
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

    @Override
    public void onDirectionFinderStart() {
        showProgressDialog("Loading...");
        if (originMarkers != null){
            for (Marker marker: originMarkers){
                marker.remove();
            }
        }

        if (destinationMakers != null){
            for (Marker marker: destinationMakers){
                marker.remove();
            }
        }
        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMakers = new ArrayList<>();
        for(Route route: routes){

        }
    }

    class LoadPlaceAround extends AsyncTask<String, Void, String>{
        HttpHandler httpHandler;
        String token, latitude, longitude;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Loading...");
        }
        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            latitude = params[1];
            longitude = params[2];
            httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeServiceCall(URL_PLACE_AROUND +"?token="+token+"&latitude="+latitude+"&longitude="+longitude);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("405")){
                Toasty.error(getActivity(), "Loading error", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }else{
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200){
                        JSONArray jsonArray = root.getJSONArray("data");
                        for ( int i = 0; i<jsonArray.length(); i++ ){
                            JSONObject data = jsonArray.getJSONObject(i);
                            int id = data.getInt("id");
                            String display_name = data.getString("display_name");
                            String description = data.getString("description");
                            String address = data.getString("address");
                            String phone_number = data.getString("phone_number");
                            String email = data.getString("email");
                            String photo = data.getString("photo");
                            String price_limit = data.getString("price_limit");
                            String time_open = data.getString("time_open");
                            String time_close = data.getString("time_close");
                            String wifi_password = data.getString("wifi_password");
                            Double latitude = data.getDouble("latitude");
                            Double longitude = data.getDouble("longitude");
                            String user_id = data.getString("user_id");

                            final Places place = new Places();
                            place.setId(id);
                            place.setDisplay_name(display_name);
                            place.setLatitude(latitude);
                            place.setLongitude(longitude);
                            listPlace.add(place);
                        }
                        //addListMaker();
                    }
                    hideProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideProgressDialog();
                }
            }
        }
    }
    private void addListMaker(){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                Marker marker = null;
                for ( int j=0; j<listPlace.size(); j++ ){
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(listPlace.get(j).getLatitude(), listPlace.get(j).getLongitude()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .title(listPlace.get(j).getDisplay_name()));
                    //marker.showInfoWindow();
                    //createMarker(mGoogleMap, listPlace.get(j).getLatitude(), listPlace.get(j).getLongitude(), listPlace.get(j).getDisplay_name());
                }
                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {

                    @Override
                    public boolean onMarkerClick(Marker arg0) {
                        if(arg0.getTitle().equals("MyHome")) // if marker source is clicked
                            Toast.makeText(getActivity(), arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
                        return true;
                    }

                });
                //mGoogleMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) getActivity());
                onInfoWindowClick(marker);
            }
        });
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getActivity(), "Info window clicked", Toast.LENGTH_SHORT).show();
    }
    //double latitude, double longitude, String title, String snippet, int iconResID
    protected Marker createMarker(GoogleMap googleMap, double latitude, double longitude, String title) {
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title(title));
        //marker.showInfoWindow();
        return marker;
    }
}
