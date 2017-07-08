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
import com.linhnv.foodsy.model.SP;

/**
 * Created by linhnv on 21/05/2017.
 */

public class MapFragment extends BaseFragment {
    GoogleMap mGoogleMap;
    private MapView mMapView;
    private SP sp;

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
        hideProgressDialog();
    }
}
