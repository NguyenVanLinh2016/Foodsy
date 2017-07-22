package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Places;

import java.util.ArrayList;

/**
 * Created by linhnv on 10/06/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements Filterable {

    Context c;
    ArrayList<Places> places, filterList;
    CustomFilter filter;

    public SearchAdapter(Context context, ArrayList<Places> places) {
        this.c = context;
        this.places = places;
        this.filterList = places;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_search, null);
        SearchViewHolder searchViewHolder = new SearchViewHolder(v);
        return searchViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.mNamePlace.setText(places.get(position).getDisplay_name());
        holder.mAddress.setText(places.get(position).getAddress());
        holder.mPriceLimit.setText("Mức giá: " + places.get(position).getPrice_limit());
        holder.mAgo.setText(places.get(position).getMinutes() + " phút");
//        holder.mRanks.setText(place.getPrice_limit());
    }


    @Override
    public int getItemCount() {
        return places.size();
    }


    /*class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView mPhoto;
        private TextView mNamePlace, mAddress, mPriceLimit, mAgo, mRanks;
        public SearchViewHolder(View itemView) {
            super(itemView);

            mPhoto = (ImageView) itemView.findViewById(R.id.image_view_photo_search);
            mNamePlace = (TextView) itemView.findViewById(R.id.text_view_namePlace_search);
            mAddress = (TextView) itemView.findViewById(R.id.text_view_address_search);
            mPriceLimit = (TextView) itemView.findViewById(R.id.text_view_pricelimit_search);
            mAgo = (TextView) itemView.findViewById(R.id.text_view_ago_search);
            mRanks = (TextView) itemView.findViewById(R.id.text_view_ranks_search);
        }
    }*/
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }
}
