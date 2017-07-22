package com.linhnv.foodsy.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.adapter.PlaceAdapter;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerRegister extends BaseFragment {
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private List<Places> placeList;
    private String url_register_admin = "https://foodsyapp.herokuapp.com/api/user/admin/owner";

    public OwnerRegister() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owner_register, container, false);
    }

    public class GetOwerRegister extends AsyncTask<String, Void, String> {
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[1];
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url_register_admin + "?token=" + token);
            Log.e("Response", "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null) {
                if (result.equals("403")) {
                    Toasty.error(getActivity(), "You dont have this permission!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200) {
                            JSONArray eat = root.getJSONArray("data");
                            for (int i = 0; i < eat.length(); i++) {
                                JSONObject c = eat.getJSONObject(i);
                                String username = c.getString("username");
                                String display_name = c.getString("display_name");
                                String email = c.getString("email");
                                String phone_number = c.getString("phone_number");
                                String address = c.getString("address");
                                String gender = c.getString("gender");
                                String role = c.getString("role");

                                /*placeList.add(places);
                                setAdapter();*/
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
               // Log.d(TAG, "Error");
            }
        }
    }

}
