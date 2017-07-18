package com.linhnv.foodsy.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linhnv.foodsy.activity.PlaceDetailActivity;
import com.linhnv.foodsy.adapter.BookmarkAdapter;
import com.linhnv.foodsy.adapter.PlaceAdapter;
import com.linhnv.foodsy.model.ClickListener;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.RecyclerTouchListenerHome;
import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by linhnv on 21/05/2017.
 */

public class BookmarkFragment extends BaseFragment {
    private static final String TAG = BookmarkFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private BookmarkAdapter bookmarkAdapter;
    private List<Places> placeList;
    private SP sp;
    private List<Integer> listId;
    private String url_place_bookmark = "https://foodsyapp.herokuapp.com/api/place";
    private String url_img = "https://foodsyapp.herokuapp.com/api/place/photo";
    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_bookmark);
        sp = new SP(getActivity());
        placeList = new ArrayList<>();
        //loadData();
        //init();
        Log.d(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();

        loadData();
        init();
        setAdapter();
        placeList.clear();
        //bookmarkAdapter.notifyDataSetChanged();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    private void init(){
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

    private void loadData(){
        listId = new ArrayList<>();
        token = sp.getToken();
        String getData = sp.getBookmark();
        if (getData.length() > 0){
            String [] id = getData.split(",");
            for(int i=0; i<id.length; i++){
                listId.add(Integer.valueOf(id[i].toString()));
            }
            //Toast.makeText(getActivity(), "SIZE: "+ listId.size(), Toast.LENGTH_SHORT).show();
            for (int i=0; i < listId.size(); i++){
                new GetPlaceBookmark().execute(listId.get(i).toString(), token);
            }
        }
    }

    public class GetPlaceBookmark extends AsyncTask<String, Void, String> {
        String token;
        String id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            id = params[0];
            token = params[1];
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url_place_bookmark +"/"+id + "?token="+ token);
            Log.e(TAG+"----", "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null){
                //placeList.clear();
                if (result.equals("405")){
                    Toasty.error(getActivity(), "Loading error", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200){
                            JSONObject data = root.getJSONObject("data");
                            int id = data.getInt("id");
                            String display_name = data.getString("display_name");
                            String description = data.getString("description");
                            String address = data.getString("address");
                            String city = data.getString("city");
                            String phone_number = data.getString("phone_number");
                            String email = data.getString("email");
                            String photo = data.getString("photo");
                            String url = url_img + "?token=" + token + "&id=" + id;
                            String price_limit = data.getString("price_limit");
                            String time_open = data.getString("time_open");
                            String time_close = data.getString("time_close");
                            String wifi_password = data.getString("wifi_password");
                            Double latitude = data.getDouble("latitude");
                            Double longitude = data.getDouble("longitude");
                            String status_p = data.getString("status");
                            int user_id = data.getInt("user_id");
                            Places place = new Places(
                                    id,
                                    display_name,
                                    description,
                                    address,
                                    city,
                                    phone_number,
                                    email,
                                    url,
                                    price_limit,
                                    time_open,
                                    time_close,
                                    wifi_password,
                                    latitude,
                                    longitude,
                                    status_p,
                                    user_id
                            );
                            placeList.add(place);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bookmarkAdapter.notifyDataSetChanged();
                }
            }else{
                Log.d(TAG, "Error");
            }
        }
    }
    private void setAdapter(){
        bookmarkAdapter = new BookmarkAdapter(getActivity(), placeList);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        recyclerView.setAdapter(bookmarkAdapter);
    }
}
