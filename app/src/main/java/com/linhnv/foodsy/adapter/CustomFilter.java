package com.linhnv.foodsy.adapter;

import android.widget.Filter;

import com.linhnv.foodsy.adapter.SearchAdapter;
import com.linhnv.foodsy.model.Places;

import java.util.ArrayList;

/**
 * Created by linhnv on 22/07/2017.
 */

public class CustomFilter extends Filter {
    SearchAdapter searchAdapter;
    ArrayList<Places> places;

    public CustomFilter(ArrayList<Places> places, SearchAdapter searchAdapter) {
        this.searchAdapter = searchAdapter;
        this.places = places;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Places> filteredPlayers=new ArrayList<>();
            for (int i=0;i<places.size();i++)
            {
                //CHECK
                if(places.get(i).getDisplay_name().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(places.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=places.size();
            results.values=places;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        searchAdapter.places = (ArrayList<Places>) results.values;
        //REFRESH
        searchAdapter.notifyDataSetChanged();
    }
}
