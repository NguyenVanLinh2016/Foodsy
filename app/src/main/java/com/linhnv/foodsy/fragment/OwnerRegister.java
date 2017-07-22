package com.linhnv.foodsy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linhnv.foodsy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnerRegister extends Fragment {


    public OwnerRegister() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_owner_register, container, false);
    }

}
