package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.PlaceFoodReviews;
import com.squareup.picasso.Picasso;

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
        holder.text_view_displayName_reviews.setText(placeFoodReviews.getDisplay_name());
        holder.text_view_message.setText(placeFoodReviews.getMessage());
        holder.ratingBar_item.setRating(Float.valueOf(placeFoodReviews.getRating()));
        String datetime = placeFoodReviews.getUpdated_at();
        //2017-07-09 03:57:21
        String day = (String) datetime.substring(8,10);
        String month = (String) datetime.substring(5,7);
        String year = (String) datetime.substring(0,4);
        String timer = (String) datetime.substring(11, 16);
        holder.text_view_date_comment.setText(day +"-"+ month +"-"+ year);
        Picasso.with(mContext)
                .load(placeFoodReviews.getPhoto())
                .placeholder(R.drawable.bglogin5)
                .error(R.drawable.bglogin5)
                .into(holder.image_view_avatar_reviews);
    }

    @Override
    public int getItemCount() {
        return mListPlaceReviews.size();
    }

    class PlaceFoodReviewsViewHolder extends RecyclerView.ViewHolder{

        private TextView text_view_message, text_view_displayName_reviews, text_view_date_comment;
        private RatingBar ratingBar_item;
        private ImageView image_view_avatar_reviews;

        public PlaceFoodReviewsViewHolder(View itemView) {
            super(itemView);
            text_view_displayName_reviews = (TextView) itemView.findViewById(R.id.text_view_displayName_reviews);
            text_view_date_comment = (TextView) itemView.findViewById(R.id.text_view_date_comment);
            text_view_message = (TextView) itemView.findViewById(R.id.text_view_message);
            ratingBar_item = (RatingBar) itemView.findViewById(R.id.ratingBar_item);
            image_view_avatar_reviews = (ImageView) itemView.findViewById(R.id.image_view_avatar_reviews);
        }
    }
}
