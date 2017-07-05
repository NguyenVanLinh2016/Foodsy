package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.PlaceFoodReviews;

import java.util.List;

/**
 * Created by linhnv on 11/06/2017.
 */

public class PlaceFoodReviewsAdapter extends RecyclerView.Adapter<PlaceFoodReviewsAdapter.PlaceFoodReviewsViewHolder> {
    private Context mContext;
    private List<PlaceFoodReviews> mListPlaceReviews;
    private LayoutInflater mLayoutInflater;

    public PlaceFoodReviewsAdapter(Context context, List<PlaceFoodReviews> data){
        this.mContext = context;
        this.mListPlaceReviews = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PlaceFoodReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.row_item_food_reviews, parent, false);
        return new PlaceFoodReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceFoodReviewsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mListPlaceReviews.size();
    }

    class PlaceFoodReviewsViewHolder extends RecyclerView.ViewHolder{

        private TextView txtDisplayName;

        public PlaceFoodReviewsViewHolder(View itemView) {
            super(itemView);

            txtDisplayName = (TextView) itemView.findViewById(R.id.text_view_displayName_reviews);
        }
    }
}
