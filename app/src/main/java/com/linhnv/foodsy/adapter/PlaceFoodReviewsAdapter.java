package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
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
        PlaceFoodReviews placeFoodReviews = mListPlaceReviews.get(position);
        holder.text_view_message.setText(placeFoodReviews.getMessage());
        holder.ratingBar_item.setRating(Float.valueOf(placeFoodReviews.getRating()));
    }

    @Override
    public int getItemCount() {
        return mListPlaceReviews.size();
    }

    class PlaceFoodReviewsViewHolder extends RecyclerView.ViewHolder{

        private TextView text_view_message;
        private RatingBar ratingBar_item;

        public PlaceFoodReviewsViewHolder(View itemView) {
            super(itemView);
            text_view_message = (TextView) itemView.findViewById(R.id.text_view_message);
            ratingBar_item = (RatingBar) itemView.findViewById(R.id.ratingBar_item);
        }
    }
}
