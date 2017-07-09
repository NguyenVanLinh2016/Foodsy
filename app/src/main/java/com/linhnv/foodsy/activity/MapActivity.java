package com.linhnv.foodsy.activity;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.DirectionFinder;
import com.linhnv.foodsy.model.DirectionFinderListener;
import com.linhnv.foodsy.model.Route;
import com.linhnv.foodsy.model.SP;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {
    private GoogleMap mMap;
    private SP sp;
    private Double latitude;
    private Double longitude;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMakers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        sp = new SP(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");
        //duration map
        String origin = String.valueOf(sp.getLatitude() +","+ sp.getLongitude());
        String destination = String.valueOf(latitude +","+ longitude);
        sendRequest(origin, destination);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng syndey = new LatLng(sp.getLatitude(), sp.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(syndey).icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocationgif)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(syndey, 18));
    }
    private void sendRequest(String origin, String destination){
        try{
            new DirectionFinder((DirectionFinderListener) this, origin, destination).execute();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

    }
    @Override
    public void onDirectionFinderStart() {
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMakers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);
            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
