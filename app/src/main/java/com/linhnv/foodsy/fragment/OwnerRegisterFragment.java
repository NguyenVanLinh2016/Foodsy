package com.linhnv.foodsy.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.activity.PlaceDetailActivity;
import com.linhnv.foodsy.activity.SignInActivity;
import com.linhnv.foodsy.adapter.OwerRegisterAdapter;
import com.linhnv.foodsy.adapter.PlaceAdapter;
import com.linhnv.foodsy.model.ClickListener;
import com.linhnv.foodsy.model.OwnerRegister;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.RecyclerTouchListenerHome;
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

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerRegisterFragment extends BaseFragment {

    private RecyclerView recycle_view_owner;
    private SP sp;
    private List<OwnerRegister> ownerRegisterList;
    private OwerRegisterAdapter ownerAdapter;
    private int pos;
    public OwnerRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_owner_register, container, false);
        recycle_view_owner = (RecyclerView) view.findViewById(R.id.recycle_view_owner);
        sp = new SP(getActivity());
        ownerRegisterList = new ArrayList<>();
        new GetOwnerPending().execute();
        recycle_view_owner.addOnItemTouchListener(new RecyclerTouchListenerHome(getContext(), recycle_view_owner, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                OwnerRegister ownerRegister = ownerRegisterList.get(position);
                pos = position;
                new AcceptionUser().execute(sp.getToken(), String.valueOf(ownerRegister.getId_user()), String.valueOf(1));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }
    private class GetOwnerPending extends AsyncTask<String, Void, String> {
        HttpHandler sh;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(ApiURL.URL_GET_OWNER + "?token=" + sp.getToken());
            Log.e(TAG, "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result.equals("405")){
                Toasty.error(getActivity(), "Loading error", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200){
                        JSONArray owner = root.getJSONArray("data");
                        for (int i = 0; i < owner.length(); i++) {
                            JSONObject b = owner.getJSONObject(i);
                            int id_user = b.getInt("id");
                            String display_name_user = b.getString("display_name");
                            //get place
                            JSONObject place = b.getJSONObject("place");
                            int id_place = place.getInt("id");
                            String display_name_place = place.getString("display_name");
                            String address = place.getString("address");
                            String phone_number = place.getString("phone_number");
                            String url = ApiURL.URL_IMAGE + "?token=" + sp.getToken() + "&id=" + id_place;
                            Log.e("TEST", url);
                            OwnerRegister ownerRegister = new OwnerRegister(
                                    id_user,
                                    display_name_user,
                                    id_place,
                                    display_name_place,
                                    address,
                                    phone_number,
                                    url
                            );
                            ownerRegisterList.add(ownerRegister);
                        }
                        setAdapter();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }  Log.d(TAG, "Error");
        }
    }
    private void setAdapter(){
        ownerAdapter = new OwerRegisterAdapter(getActivity(), ownerRegisterList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle_view_owner.setLayoutManager(linearLayoutManager);
        recycle_view_owner.setAdapter(ownerAdapter);
    }
    private class AcceptionUser extends AsyncTask<String, Void, String>{
        HttpHandler httpHandler;
        String token, user_id, acception;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Acception for owner...");
        }
        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            user_id = params[1];
            acception = params[2];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(ApiURL.URL_ACCEPTION_OWNER); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("user_id", user_id);
                postDataParams.put("acception", acception);
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
            Log.d("Owner", result);
            if (result.equals("405")){
                Toasty.error(getActivity(), "Acception user fail", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200){
                        ownerRegisterList.remove(pos);
                        ownerAdapter.notifyDataSetChanged();
                        Toasty.success(getActivity(), "Acception user successfull", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
