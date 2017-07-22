package com.linhnv.foodsy.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.adapter.NotificationsAdapter;
import com.linhnv.foodsy.model.DrawsLineItem;
import com.linhnv.foodsy.model.Notifications;
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

/**
 * Created by linhnv on 21/05/2017.
 */

public class NotificationFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<Notifications> list;
    private NotificationsAdapter adapter;
    private SP sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_notification);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        sp = new SP(getActivity());
        list = new ArrayList<>();
        new LoadNotifications().execute(sp.getToken());
    }

    class LoadNotifications extends AsyncTask<String, Void, String> {
        HttpHandler httpHandler;
        String token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeServiceCall(ApiURL.URL_NOTIFICATIONS + "?token=" + token);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result.equals("405")) {
                Toasty.error(getActivity(), "Loading error", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200) {
                        JSONArray jsonArray = root.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            int id = data.getInt("id");
                            String title = data.getString("title");
                            String content = data.getString("content");
                            String photo = data.getString("photo");
                            String url = ApiURL.URL_LOADIMAGE + id + "/photo" + "?token=" + token;
                            String sale = data.getString("sale");
                            String time_start = data.getString("time_start");
                            String time_end = data.getString("time_end");
                            String status_place = data.getString("status");
                            String place_id = data.getString("place_id");
                            String created_at = data.getString("created_at");
                            String updated_at = data.getString("updated_at");
                            String place_name = data.getString("place_name");
                            final Notifications notifications = new Notifications();
                            notifications.setId(id);
                            notifications.setTitle(title);
                            notifications.setContent(content);
                            notifications.setPhoto(url);
                            notifications.setSale(sale);
                            notifications.setTime_start(time_start);
                            notifications.setTime_end(time_end);
                            notifications.setStatus(status_place);
                            notifications.setPlace_id(place_id);
                            notifications.setCreated_at(created_at);
                            notifications.setUpdated_at(updated_at);
                            notifications.setPlace_name(place_name);
                            list.add(notifications);
                            setAdapter();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setAdapter() {
        adapter = new NotificationsAdapter(getActivity(), list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DrawsLineItem(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
