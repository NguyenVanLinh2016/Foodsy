package com.linhnv.foodsy.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.DrawsLineItem;
import com.linhnv.foodsy.model.PLaceFoodMenu;
import com.linhnv.foodsy.adapter.PlaceFoodMenuAdapter;
import com.linhnv.foodsy.model.PlaceFoodReviews;
import com.linhnv.foodsy.adapter.PlaceFoodReviewsAdapter;
import com.linhnv.foodsy.network.HttpHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends BaseActivity {

    TextView mTextView_foodMenu;
    private TextView mTextView_placeDetails;
    private TextView mTextView_placeReviews;
    private RecyclerView recycle_view_foodMenu;
    private RecyclerView recycle_view_foodReviews;

    private ArrayList<PLaceFoodMenu> mPlaceFoodMenuList;
    private List<PlaceFoodReviews> mPlaceFoodReviewsList;

    private PlaceFoodMenuAdapter mPlaceFoodMenuAdapter;
    private PlaceFoodReviewsAdapter mPlaceFoodReviewsAdapter;

    private static String urlPlace = "https://foodsyapp.herokuapp.com/api/place";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        mTextView_foodMenu.setTextColor(Color.parseColor("#3cb963"));
        recycle_view_foodMenu.setVisibility(View.VISIBLE);
     //   foodMenu();

        mTextView_foodMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor();
                mTextView_foodMenu.setTextColor(Color.parseColor("#3cb963"));
                recycle_view_foodMenu.setVisibility(View.VISIBLE);
                recycle_view_foodReviews.setVisibility(View.GONE);
               // foodMenu();
            }
        });
        mTextView_placeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor();
                mTextView_placeDetails.setTextColor(Color.parseColor("#3cb963"));
                recycle_view_foodMenu.setVisibility(View.VISIBLE);
                recycle_view_foodReviews.setVisibility(View.GONE);
               // foodMenu();
            }
        });
        mTextView_placeReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor();
                mTextView_placeReviews.setTextColor(Color.parseColor("#3cb963"));
                recycle_view_foodMenu.setVisibility(View.GONE);
                recycle_view_foodReviews.setVisibility(View.VISIBLE);
                foodReviews();
            }
        });

    }

    private void init() {
        mTextView_foodMenu = (TextView) findViewById(R.id.text_view_foodMenu);
        mTextView_placeDetails = (TextView) findViewById(R.id.text_view_placeDetail);
        mTextView_placeReviews = (TextView) findViewById(R.id.text_view_placeReviews);
        recycle_view_foodMenu = (RecyclerView) findViewById(R.id.recycle_view_foodMenu);
        recycle_view_foodReviews = (RecyclerView) findViewById(R.id.recycle_view_foodReviews);
    }

    private void setTextColor() {
        mTextView_foodMenu.setTextColor(Color.BLACK);
        mTextView_placeDetails.setTextColor(Color.BLACK);
        mTextView_placeReviews.setTextColor(Color.BLACK);
    }

  /*  private void foodMenu() {
        mPlaceFoodMenuList = new ArrayList<>();
        mPlaceFoodMenuList.add(new PLaceFoodMenu("Cơm niêu Sing chỉ từ 59K", "Tận hưởng điều hòa mát rượi trốn nóng với Cơm niêu Sing chỉ từ 59K tại hệ thống nhà hàng Kombo", 59));
        mPlaceFoodMenuList.add(new PLaceFoodMenu("Cơm niêu Sing chỉ từ 59K", "Tận hưởng điều hòa mát rượi trốn nóng với Cơm niêu Sing chỉ từ 59K tại hệ thống nhà hàng Kombo", 59));
        mPlaceFoodMenuList.add(new PLaceFoodMenu("Cơm niêu Sing chỉ từ 59K", "Tận hưởng điều hòa mát rượi trốn nóng với Cơm niêu Sing chỉ từ 59K tại hệ thống nhà hàng Kombo", 59));
        mPlaceFoodMenuList.add(new PLaceFoodMenu("Cơm niêu Sing chỉ từ 59K", "Tận hưởng điều hòa mát rượi trốn nóng với Cơm niêu Sing chỉ từ 59K tại hệ thống nhà hàng Kombo", 59));
        mPlaceFoodMenuList.add(new PLaceFoodMenu("Cơm niêu Sing chỉ từ 59K", "Tận hưởng điều hòa mát rượi trốn nóng với Cơm niêu Sing chỉ từ 59K tại hệ thống nhà hàng Kombo", 59));

        mPlaceFoodMenuAdapter = new PlaceFoodMenuAdapter(this, mPlaceFoodMenuList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle_view_foodMenu.setLayoutManager(linearLayoutManager);

        recycle_view_foodMenu.addItemDecoration(new DrawsLineItem(this, LinearLayoutManager.VERTICAL));
        recycle_view_foodMenu.setAdapter(mPlaceFoodMenuAdapter);
    }
*/
    private void foodReviews() {
        mPlaceFoodReviewsList = new ArrayList<>();
        mPlaceFoodReviewsList.add(new PlaceFoodReviews("Phan Kim Lien", "July 20th, 2016", "Mình đi ăn cũng lâu lâu rồi nên ko nhớ tên món nữa. Nhưng ốc ở đây siêu tươi ngon, ốc dai, béo, chế biến sạch sẽ và rất thơm ngon"));
        mPlaceFoodReviewsList.add(new PlaceFoodReviews("Phan Kim Lien", "July 20th, 2016", "Mình đi ăn cũng lâu lâu rồi nên ko nhớ tên món nữa. Nhưng ốc ở đây siêu tươi ngon, ốc dai, béo, chế biến sạch sẽ và rất thơm ngon"));
        mPlaceFoodReviewsList.add(new PlaceFoodReviews("Phan Kim Lien", "July 20th, 2016", "Mình đi ăn cũng lâu lâu rồi nên ko nhớ tên món nữa. Nhưng ốc ở đây siêu tươi ngon, ốc dai, béo, chế biến sạch sẽ và rất thơm ngon"));
        mPlaceFoodReviewsList.add(new PlaceFoodReviews("Phan Kim Lien", "July 20th, 2016", "Mình đi ăn cũng lâu lâu rồi nên ko nhớ tên món nữa. Nhưng ốc ở đây siêu tươi ngon, ốc dai, béo, chế biến sạch sẽ và rất thơm ngon"));
        mPlaceFoodReviewsList.add(new PlaceFoodReviews("Phan Kim Lien", "July 20th, 2016", "Mình đi ăn cũng lâu lâu rồi nên ko nhớ tên món nữa. Nhưng ốc ở đây siêu tươi ngon, ốc dai, béo, chế biến sạch sẽ và rất thơm ngon"));

        mPlaceFoodReviewsAdapter = new PlaceFoodReviewsAdapter(this, mPlaceFoodReviewsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle_view_foodReviews.setLayoutManager(linearLayoutManager);
        recycle_view_foodReviews.setAdapter(mPlaceFoodReviewsAdapter);
    }

    private class getPlaceDetail extends AsyncTask<String, Void, String> {
        String name, price, description, token;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler httpHandler = new HttpHandler();
            String jsonStr = httpHandler.makeServiceCall(urlPlace);
            mPlaceFoodMenuList = new ArrayList<>();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray data = new JSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                     JSONObject c = data.getJSONObject(i);
                        PLaceFoodMenu pLaceFoodMenu = new PLaceFoodMenu();
                        pLaceFoodMenu.foodName = c.getString("display_name");
                        pLaceFoodMenu.price = Double.parseDouble(c.getString("price_limit"));
                        pLaceFoodMenu.foodDescription= c.getString("description");
                        mPlaceFoodMenuList.add(pLaceFoodMenu);
                    }

                } catch (Exception e) {
                }
            }
            return null;
        }
    }

}
