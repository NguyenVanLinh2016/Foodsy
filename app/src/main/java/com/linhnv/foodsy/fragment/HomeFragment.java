package com.linhnv.foodsy.fragment;

import android.app.ProgressDialog;
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

import com.linhnv.foodsy.activity.HomeActivity;
import com.linhnv.foodsy.activity.MenuActivity;
import com.linhnv.foodsy.activity.UpdateInfoActivity;
import com.linhnv.foodsy.model.ClickListener;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhnv on 21/05/2017.
 */

public class HomeFragment extends Fragment {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private List<Places> placeList;
    private SP sp;
    private String url_places = null;
    private String url_place_eat = "https://foodsyapp.herokuapp.com/api/place/eat";
    private String url_place_drink = "https://foodsyapp.herokuapp.com/api/place/drink";
    private String url_place_entertain = "https://foodsyapp.herokuapp.com/api/place/entertain";
    ArrayList<Places> eatList;

    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        placeList = new ArrayList<>();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListenerHome(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity(new Intent(getActivity(), PlaceDetailActivity.class));
                //Toast.makeText(getActivity(), "Here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        sp = new SP(getContext());

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
        Toast.makeText(getActivity(), url_places, Toast.LENGTH_LONG).show();
        new GetEatInfo().execute();
        return view;
    }

    public String token() {
        sp = new SP(getContext());
        String token = sp.getToken();
        return token;
    }

    public class GetEatInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url_places + "?token=" + token());
            Log.e(TAG, "Response from url: " + jsonStr);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray eat = jsonObj.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < eat.length(); i++) {
                        JSONObject c = eat.getJSONObject(i);

                        String display_name = c.getString("display_name");
                        String description = c.getString("description");
                        String address = c.getString("address");
                        String city = c.getString("city");

                        Log.d(TAG, display_name + description + address + city);
                        // tmp hash map for single contact
                        Places places = new Places();

                        // adding each child node to HashMap key => value
                        places.setDisplay_name(display_name);
                        places.setDescription(description);
                        places.setAddress(address);
                        places.setCity(city);

                        Log.d(TAG, places.toString());
                        // adding contact to contact list
                        placeList.add(places);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            placeAdapter = new PlaceAdapter(getContext(), placeList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(placeAdapter);

        }


    }

}
