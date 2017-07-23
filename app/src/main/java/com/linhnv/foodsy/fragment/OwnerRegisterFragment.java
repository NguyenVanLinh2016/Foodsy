package com.linhnv.foodsy.fragment;


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
import com.linhnv.foodsy.adapter.OwerRegisterAdapter;
import com.linhnv.foodsy.adapter.PlaceAdapter;
import com.linhnv.foodsy.model.OwnerRegister;
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

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerRegisterFragment extends BaseFragment {

    private RecyclerView recycle_view_owner;
    private SP sp;
    private List<OwnerRegister> ownerRegisterList;
    private OwerRegisterAdapter ownerAdapter;
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
        return view;
    }
    private void init(){

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
}
