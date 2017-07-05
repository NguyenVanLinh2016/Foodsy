package com.linhnv.foodsy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.linhnv.foodsy.adapter.BookmarkAdapter;
import com.linhnv.foodsy.model.ClickListener;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.model.RecyclerTouchListenerHome;
import com.linhnv.foodsy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhnv on 21/05/2017.
 */

public class BookmarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookmarkAdapter bookmarkAdapter;
    private List<Place> placeList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_bookmark);
        placeList = new ArrayList<>();
        placeList.add(new Place(1, "The Coffee House", "Di An, Binh Duong", "Coffee capuchino", "Cafe/Dessert", "7", 3));
        placeList.add(new Place(2, "New Days", "Quan 3, Ho Chi Minh", "Matcha Trà Xanh", "Cafe/Dessert", "10", 4));
        placeList.add(new Place(3, "Phở 24h ", "Quan 5, Ho Chi Minh", "Phở đặt biệt", "Quan an", "9", 3));
        placeList.add(new Place(4, "Golden Vin Coffee", "Quan 1, Ho Chi Minh", "Trà sữa Cool", "Cafe/Dessert", "20", 5));
        placeList.add(new Place(5, "Cơm tấm 68", "Quan 4, Ho Chi Minh", "Cơm tấm", "Nha hang", "6", 5));
        placeList.add(new Place(6, "Cơm tấm 69", "Quan 4, Ho Chi Minh", "Cơm tấm", "Nha hang", "12", 4));

        bookmarkAdapter = new BookmarkAdapter(getContext(), placeList);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        recyclerView.setAdapter(bookmarkAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListenerHome(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //startActivity(new Intent(getActivity(), PlaceDetailActivity.class));
                Toast.makeText(getActivity(), "Here", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }
}
