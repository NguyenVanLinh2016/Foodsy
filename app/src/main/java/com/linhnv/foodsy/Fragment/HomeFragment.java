package com.linhnv.foodsy.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linhnv.foodsy.Model.Place;
import com.linhnv.foodsy.Model.PlaceAdapter;
import com.linhnv.foodsy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhnv on 21/05/2017.
 */

public class HomeFragment extends Fragment{

    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView1);
        placeList = new ArrayList<>();
        placeList.add(new Place(1, "The Coffee House", "Coffee capuchino", 20));
        placeList.add(new Place(2, "New Days", "Matcha Trà Xanh", 20));
        placeList.add(new Place(1, "Phở 24h ", "Phở đặt biệt", 30));
        placeList.add(new Place(1, "Golden Vin Coffee", "Trà sữa Cool", 20));
        placeList.add(new Place(1, "Cơm tấm 68", "Cơm tấm", 30));

        placeAdapter = new PlaceAdapter(getContext(), placeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(placeAdapter);
        return view;
    }
}
