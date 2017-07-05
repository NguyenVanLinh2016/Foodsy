package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Place;

import java.util.List;

/**
 * Created by linhnv on 10/06/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<Place> mPlaceList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public SearchAdapter(Context context, List<Place> data){
        this.mContext = context;
        this.mPlaceList = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.row_item_search, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Place place = mPlaceList.get(position);
        holder.mNamePlace.setText(place.getNameRestaurant());
        holder.mAddress.setText(place.getAddress());
        holder.mCategory.setText(place.getCategory());
        holder.mAgo.setText(place.getAgo() + " km");
        holder.mRanks.setText(place.getRank() +"");
    }

    @Override
    public int getItemCount() {
        return mPlaceList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView mPhoto;
        private TextView mNamePlace, mAddress, mCategory, mAgo, mRanks;
        public SearchViewHolder(View itemView) {
            super(itemView);

            mPhoto = (ImageView) itemView.findViewById(R.id.image_view_photo_search);
            mNamePlace = (TextView) itemView.findViewById(R.id.text_view_namePlace_search);
            mAddress = (TextView) itemView.findViewById(R.id.text_view_address_search);
            mCategory = (TextView) itemView.findViewById(R.id.text_view_caterogy_search);
            mAgo = (TextView) itemView.findViewById(R.id.text_view_ago_search);
            mRanks = (TextView) itemView.findViewById(R.id.text_view_ranks_search);
        }
    }
}
