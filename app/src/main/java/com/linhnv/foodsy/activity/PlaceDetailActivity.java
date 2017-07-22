package com.linhnv.foodsy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.adapter.ExpanlistFoodMenuAdapter;
import com.linhnv.foodsy.model.DirectionFinder;
import com.linhnv.foodsy.model.DirectionFinderListener;
import com.linhnv.foodsy.model.FoodMenu;
import com.linhnv.foodsy.adapter.PlaceDetailMenuAdapter;
import com.linhnv.foodsy.model.PlaceFoodReviews;
import com.linhnv.foodsy.adapter.PlaceFoodReviewsAdapter;
import com.linhnv.foodsy.model.Places;
import com.linhnv.foodsy.model.Route;
import com.linhnv.foodsy.model.SP;
import com.linhnv.foodsy.network.ApiURL;
import com.linhnv.foodsy.network.HttpHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import es.dmoral.toasty.Toasty;

public class PlaceDetailActivity extends BaseActivity implements DirectionFinderListener, View.OnClickListener{

    private static final String TAG = PlaceDetailActivity.class.getSimpleName();
    private TextView mTextView_foodMenu;
    private TextView mTextView_placeDetails;
    private TextView mTextView_placeReviews;
    private RecyclerView recycle_view_foodDetails;
    private RecyclerView recycle_view_foodReviews;
    private RelativeLayout scroll_view_menu;
    private ExpandableListView expandable_lv_foodMenu;
    private ExpanlistFoodMenuAdapter expandalelist_Adapter;
    private List<String> listDataHeader;
    private HashMap<String, List<FoodMenu>> listDataChild;
    private ImageView image_view_details;
    private Button button_ready_detail, button_rating;
    private RatingBar ratingBar_detail;
    //view
    private View view_foodMenu;
    private View view_details;
    private View view_placeReviews;
    //toolbar
    private TextView text_view_name_detail;
    private ImageView image_view_back_detail;
    private ImageView image_view_bookmark_detail;

    private List<PlaceFoodReviews> mPlaceFoodReviewsList;
    private List<Places> mPlaceDetailsList;
    private PlaceDetailMenuAdapter mPlaceFoodDetailAdapter;
    private PlaceFoodReviewsAdapter mPlaceFoodReviewsAdapter;

    private SP sp;
    double latitude;
    double longitude;
    boolean isOn = false;

    //test
    private HashMap<String, List<FoodMenu>> list_test =  new HashMap<>();
    //id places
    private int id;
    private List<Integer> listId;
    private List<Integer> listId_bookmark;
    private RelativeLayout layout_rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        listId = new ArrayList<>();
        mPlaceFoodReviewsList = new ArrayList<>();
        listId_bookmark = new ArrayList<>();
        sp = new SP(this);
        //get data from intent
        id = getIntent().getExtras().getInt("id");
        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");
        String display_name = getIntent().getExtras().getString("display_name");
        String url_image = getIntent().getExtras().getString("url_image");
        Log.d(TAG, url_image);
        if (url_image.length() != 0){
            Picasso.with(this)
                    .load(url_image)
                    .placeholder(R.drawable.bglogin5)
                    .error(R.drawable.bglogin5)
                    .into(image_view_details);
        }else{
            image_view_details.setImageResource(R.drawable.bglogin5);
        }


        text_view_name_detail.setText(display_name);
        //duration map
        String origin = String.valueOf(sp.getLatitude() +","+ sp.getLongitude());
        String destination = String.valueOf(latitude +","+ longitude);
        sendRequest(origin, destination);

        mTextView_foodMenu.setTextColor(Color.parseColor("#3cb963"));
        layout_rating.setVisibility(View.GONE);
        expandable_lv_foodMenu.setVisibility(View.VISIBLE);

        view_foodMenu.setVisibility(View.VISIBLE);
        new LoadPlaceDetail().execute(sp.getToken(), String.valueOf(id));
        listDataHeader = new ArrayList<String>();
        //set image bookmark
        String getData = sp.getBookmark();
        if (getData.length() > 0){
            //getData = "2,5,6,7,8,1
            String [] id_s = getData.split(",");
            for(int i=0; i< id_s.length; i++){
                listId.add(Integer.valueOf(id_s[i]));
            }
            for (int i = 0; i< listId.size(); i++){
                if (listId.get(i).toString().equalsIgnoreCase(String.valueOf(id))){
                    isOn = true;
                    image_view_bookmark_detail.setImageResource(R.drawable.bookmark1);
                }
            }
        }
    }

    private void init() {
        mTextView_foodMenu = (TextView) findViewById(R.id.text_view_foodMenu);
        mTextView_placeDetails = (TextView) findViewById(R.id.text_view_placeDetail);
        mTextView_placeReviews = (TextView) findViewById(R.id.text_view_placeReviews);
        //expandable list
        scroll_view_menu = (RelativeLayout) findViewById(R.id.scroll_view_menu);
        //scroll_view_detail = (ScrollView) findViewById(R.id.scroll_view_detail);
        expandable_lv_foodMenu = (ExpandableListView) findViewById(R.id.expandable_lv_foodMenu);
        //details

        //recycleView
        recycle_view_foodDetails = (RecyclerView) findViewById(R.id.recycle_view_foodDetails);
        recycle_view_foodReviews = (RecyclerView) findViewById(R.id.recycle_view_foodReviews);
        image_view_details = (ImageView) findViewById(R.id.image_view_details);
        button_ready_detail = (Button) findViewById(R.id.button_ready_detail);
        button_rating = (Button) findViewById(R.id.button_rating);
        text_view_name_detail = (TextView) findViewById(R.id.text_view_name_detail);
        image_view_back_detail = (ImageView) findViewById(R.id.image_view_back_detail);
        image_view_bookmark_detail = (ImageView) findViewById(R.id.image_view_bookmark_detail);
        //image_view_cart.setCount(1);

        view_foodMenu = findViewById(R.id.view_foodMenu);
        view_details = findViewById(R.id.view_placeDetail);
        view_placeReviews = findViewById(R.id.view_placeReviews);
        layout_rating = (RelativeLayout) findViewById(R.id.layout_rating);
        ratingBar_detail = (RatingBar) findViewById(R.id.ratingBar_detail);
        //setonClick
        mTextView_foodMenu.setOnClickListener(this);
        mTextView_placeDetails.setOnClickListener(this);
        mTextView_placeReviews.setOnClickListener(this);
        button_ready_detail.setOnClickListener(this);
        image_view_back_detail.setOnClickListener(this);

        //setonClick
        button_ready_detail.setOnClickListener(this);
        image_view_back_detail.setOnClickListener(this);
        image_view_bookmark_detail.setOnClickListener(this);
        button_rating.setOnClickListener(this);
    }

    private void setDefaultView() {
        mTextView_foodMenu.setTextColor(Color.BLACK);
        mTextView_placeDetails.setTextColor(Color.BLACK);
        mTextView_placeReviews.setTextColor(Color.BLACK);
        //view
        view_foodMenu.setVisibility(View.INVISIBLE);
        view_details.setVisibility(View.INVISIBLE);
        view_placeReviews.setVisibility(View.INVISIBLE);
        //expandable list
        scroll_view_menu.setVisibility(View.GONE);
        expandable_lv_foodMenu.setVisibility(View.GONE);
        //recycleView
        recycle_view_foodDetails.setVisibility(View.GONE);
        recycle_view_foodReviews.setVisibility(View.GONE);
        layout_rating.setVisibility(View.GONE);
    }
    private void foodMenu(){
        listDataChild = new HashMap<String, List<FoodMenu>>();
        for (int i=0; i< listDataHeader.size(); i++){
            listDataChild.put(listDataHeader.get(i), list_test.get(listDataHeader.get(i)));
        }
        expandalelist_Adapter = new ExpanlistFoodMenuAdapter(this, listDataHeader, listDataChild);
        expandable_lv_foodMenu.setTranscriptMode(ExpandableListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        expandable_lv_foodMenu.setStackFromBottom(true);
        // setting list adapter
        expandable_lv_foodMenu.setAdapter(expandalelist_Adapter);
        expandable_lv_foodMenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;
            }
        });
        expandable_lv_foodMenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(PlaceDetailActivity.this, ""+listDataHeader.get(groupPosition) +" : "+
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void foodDetails() {

        String address = getIntent().getExtras().getString("address");
        String phone = getIntent().getExtras().getString("phone");
        String email = getIntent().getExtras().getString("email");
        String price = getIntent().getExtras().getString("price");
        String time_open = getIntent().getExtras().getString("time_open");
        String time_close = getIntent().getExtras().getString("time_close");
        String wifi = getIntent().getExtras().getString("wifi");
        String description = getIntent().getExtras().getString("description");
        //detail
        mPlaceDetailsList = new ArrayList<>();
        mPlaceDetailsList.add(new Places(address, phone, email, price, time_open, time_close, wifi, description));
        mPlaceFoodDetailAdapter = new PlaceDetailMenuAdapter(this, mPlaceDetailsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle_view_foodDetails.setLayoutManager(linearLayoutManager);
        recycle_view_foodDetails.setAdapter(mPlaceFoodDetailAdapter);

    }

    private void foodReviews() {
        mPlaceFoodReviewsAdapter = new PlaceFoodReviewsAdapter(this, mPlaceFoodReviewsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle_view_foodReviews.setLayoutManager(linearLayoutManager);
        recycle_view_foodReviews.setAdapter(mPlaceFoodReviewsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ready_detail:
                Intent intent = new Intent(PlaceDetailActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                b.putDouble("latitude", latitude);
                b.putDouble("longitude", longitude);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.image_view_back_detail:
                finish();
                break;
            case R.id.text_view_foodMenu:
                setDefaultView();
                mTextView_foodMenu.setTextColor(Color.parseColor("#3cb963"));
                scroll_view_menu.setVisibility(View.VISIBLE);
                expandable_lv_foodMenu.setVisibility(View.VISIBLE);
                view_foodMenu.setVisibility(View.VISIBLE);
                foodMenu();
                break;
            case R.id.text_view_placeDetail:
                setDefaultView();
                mTextView_placeDetails.setTextColor(Color.parseColor("#3cb963"));
                recycle_view_foodDetails.setVisibility(View.VISIBLE);
                view_details.setVisibility(View.VISIBLE);
                foodDetails();
                break;
            case R.id.text_view_placeReviews:
                mPlaceFoodReviewsList.clear();
                setDefaultView();
                mTextView_placeReviews.setTextColor(Color.parseColor("#3cb963"));
                recycle_view_foodReviews.setVisibility(View.VISIBLE);
                layout_rating.setVisibility(View.VISIBLE);
                view_placeReviews.setVisibility(View.VISIBLE);
                String token = sp.getToken();
                new LoadComment().execute(String.valueOf(id), token);
                break;
            case R.id.image_view_bookmark_detail:
                listId_bookmark.clear();
                String bookmark = sp.getBookmark();
                String dataBookmark1 = "";
                String dataBookmark2 = "";

                if (isOn){
                    image_view_bookmark_detail.setImageResource(R.drawable.bookmark2);
                    isOn = false;
                    //check data
                    String [] id_b = sp.getBookmark().split(",");
                    for(int i=0; i<id_b.length; i++){
                        listId_bookmark.add(Integer.valueOf(id_b[i].toString()));
                    }
                    for (int i =0; i<listId_bookmark.size(); i++){
                        if (listId_bookmark.get(i).intValue() == id){
                            listId_bookmark.remove(i);
                        }
                    }
                    for (int i =0; i<listId_bookmark.size(); i++){
                        dataBookmark2 = dataBookmark2.concat(listId_bookmark.get(i).intValue() +",");
                    }
                    sp.setBookmark(dataBookmark2);
                    Log.d(TAG, "----"+ dataBookmark2);
                }else{
                    //set state
                    image_view_bookmark_detail.setImageResource(R.drawable.bookmark1);
                    isOn = true;
                    //set data
                    if (bookmark.length() == 0){
                        sp.setBookmark(id +",");
                    }else{
                        sp.setBookmark(bookmark.concat( id +","));
                        String [] id_b = sp.getBookmark().split(",");
                        for(int i=0; i<id_b.length; i++){
                            listId_bookmark.add(Integer.valueOf(id_b[i].toString()));
                        }
                        for (int i =0; i<listId_bookmark.size(); i++){
                            dataBookmark1 = dataBookmark1.concat(listId_bookmark.get(i).intValue() +",");
                        }
                        Log.d(TAG, dataBookmark1 +"----");
                        sp.setBookmark(dataBookmark1);
                    }
                }
                break;
            case R.id.button_rating:
                dialog_rating();
                break;
        }
    }

    private class LoadPlaceDetail extends AsyncTask<String, Void, String> {
        String token, id;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            id = params[1];
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(ApiURL.URL_PLACE_DETAIL +"?token="+token +"&id="+id);
            Log.e(TAG, "Response from url: " + jsonStr);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            if (result != null){
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200){
                        JSONArray data = root.getJSONArray("data");
                        for ( int i = 0; i < data.length(); i++ ) {
                            JSONObject menu = data.getJSONObject(i);
                            //data category_name: Gỏi & salad,Món ăn nhẹ,Sashimi...
                            String category_name = menu.getString("category_name");
                            listDataHeader.add(category_name);
                            JSONArray products = menu.getJSONArray("products");
                            List<FoodMenu> list_test2 = new ArrayList<>();
                            //get array in product
                            for ( int j = 0; j<products.length(); j++){
                                JSONObject item = products.getJSONObject(j);
                                int id = item.getInt("id");
                                String name = item.getString("name");
                                String description = item.getString("description");
                                String photo = item.getString("photo");
                                String url = ApiURL.URL_IMAGE_PRODUCT + "/"+ id + "/photo?token=" + token;
                                int price = item.getInt("price");
                                String type = item.getString("type");
                                String status_menu = item.getString("status");
                                int category_id = item.getInt("category_id");
                                FoodMenu foodmenu = new FoodMenu(
                                        id, name, description, url, price, type, status_menu, category_id
                                );
                                list_test2.add(foodmenu);
                            }
                            list_test.put(category_name, list_test2);
                        }
                        foodMenu();
                    }else if (status == 405){
                        Toasty.error(PlaceDetailActivity.this, "Load data error!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toasty.error(PlaceDetailActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Log.d(TAG, "Error");
            }
        }
    }
    //dialog rating
    private void dialog_rating(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlaceDetailActivity.this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_message_comment,null);

        builder.setView(view);
        builder.setCancelable(false);

        final EditText edit_text_message = (EditText) view.findViewById(R.id.edit_text_message);;
        builder.setPositiveButton(R.string.button_send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String token = sp.getToken();
                float rating = ratingBar_detail.getRating();
                String message = edit_text_message.getText().toString();
                if (rating == 0.0){
                    rating = 1;
                }
                if (message.length() < 6){
                    Toasty.error(PlaceDetailActivity.this, "Vui lòng nhập lớn hơn 6 ký tự!!", Toast.LENGTH_SHORT).show();
                    edit_text_message.requestFocus();
                }else{
                    new SendComment().execute(token, message, String.valueOf(rating), String.valueOf(id));
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create();
        builder.show();
    }
    //draw map
    private void sendRequest(String origin, String destination){
        try{
            new DirectionFinder((DirectionFinderListener) this, origin, destination).execute();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

    }
    @Override
    public void onDirectionFinderStart() {

    }


    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        for(Route route: routes){
            button_ready_detail.setText("Cách đây: "+ route.duration.text.split(" ")[0] +" phút");
        }
    }
    public class LoadComment extends AsyncTask<String, Void, String> {
        String id, token;
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
            String jsonStr = sh.makeServiceCall(ApiURL.URL_LOADCOMMENT +"/"+id +"/comments?token="+ token);
            Log.e(TAG+"----", "Response from url: " + jsonStr);
            return jsonStr;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            hideProgressDialog();
            Log.d(TAG, result);
            if (result != null){
                if (result.equals("404")){
                    Toasty.error(PlaceDetailActivity.this, "Chưa có đánh giá", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        JSONObject root = new JSONObject(result);
                        int status = root.getInt("status");
                        if (status == 200){
                            JSONArray jsonArray = root.getJSONArray("data");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject data = jsonArray.getJSONObject(i);
                                int id = data.getInt("id");
                                String message = data.getString("message");
                                String photo = data.getString("photo");
                                String rating = data.getString("rating");
                                int user_id = data.getInt("user_id");
                                String url = ApiURL.URL_IMAGE_USER + "/"+ user_id  + "?token=" + token;
                                int place_id = data.getInt("place_id");
                                String status_c = data.getString("status");
                                String created_at = data.getString("created_at");
                                String updated_at = data.getString("updated_at");
                                String display_name = data.getString("display_name");

                                PlaceFoodReviews placeFoodReviews = new PlaceFoodReviews(
                                        id,
                                        message,
                                        url,
                                        rating,
                                        user_id,
                                        place_id,
                                        status_c,
                                        created_at,
                                        updated_at,
                                        display_name
                                );
                                mPlaceFoodReviewsList.add(placeFoodReviews);
                            }
                            foodReviews();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                Log.d(TAG, "Error");
            }
        }
    }

    class SendComment extends AsyncTask<String, Void, String>{
        HttpHandler httpHandler;
        String token, message, rating, place_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }
        @Override
        protected String doInBackground(String... params) {
            token = params[0];
            message = params[1];
            rating = params[2];
            place_id = params[3];
            try {
                httpHandler = new HttpHandler();
                URL url = new URL(ApiURL.URL_COMMENT); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("token", token);
                postDataParams.put("message", message);
                postDataParams.put("rating", rating);
                postDataParams.put("place_id", place_id);
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
            if (result.equals("409")){
                Toasty.error(PlaceDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    JSONObject root = new JSONObject(result);
                    int status = root.getInt("status");
                    if (status == 200){
                        Toasty.success(PlaceDetailActivity.this, "Đánh giá thành công !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
