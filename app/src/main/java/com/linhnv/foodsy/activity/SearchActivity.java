package com.linhnv.foodsy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.DrawsLineItem;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.adapter.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SearchAdapter mSearchAdapter;
    private List<Place> mPlaceList;
    private ImageView mBackSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search);
        mPlaceList = new ArrayList<>();
        mPlaceList.add(new Place(1, "The Coffee House", "Di An, Binh Duong", "Coffee capuchino", "Cafe/Dessert", "10.5", 3));
        mPlaceList.add(new Place(2, "New Days", "Quan 3, Ho Chi Minh", "Matcha Trà Xanh", "Cafe/Dessert", "6.5", 4));
        mPlaceList.add(new Place(3, "Phở 24h ", "Quan 5, Ho Chi Minh", "Phở đặt biệt", "Quan an", "2.5", 3));
        mPlaceList.add(new Place(4, "Golden Vin Coffee", "Quan 1, Ho Chi Minh", "Trà sữa Cool", "Cafe/Dessert", "3.7", 5));
        mPlaceList.add(new Place(5, "Cơm tấm 68", "Quan 4, Ho Chi Minh", "Cơm tấm", "Nha hang", "8.0", 5));

        mSearchAdapter = new SearchAdapter(this, mPlaceList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DrawsLineItem(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mSearchAdapter);

        init();

    }

    private void init(){
        mBackSearch = (ImageView) findViewById(R.id.image_view_back_search);
        mBackSearch.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_view_back_search:
                finish();
                break;
        }
    }
}
