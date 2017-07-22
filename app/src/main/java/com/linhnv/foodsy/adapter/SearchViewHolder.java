package com.linhnv.foodsy.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;

/**
 * Created by Huu on 19/07/2017.
 */

public class SearchViewHolder extends RecyclerView.ViewHolder {
    public ImageView mPhoto;
    public TextView mNamePlace, mAddress, mPriceLimit, mAgo, mRanks;
    public SearchViewHolder(View itemView) {
        super(itemView);

        mPhoto = (ImageView) itemView.findViewById(R.id.image_view_photo_search);
        mNamePlace = (TextView) itemView.findViewById(R.id.text_view_namePlace_search);
        mAddress = (TextView) itemView.findViewById(R.id.text_view_address_search);
        mPriceLimit = (TextView) itemView.findViewById(R.id.text_view_pricelimit_search);
        mAgo = (TextView) itemView.findViewById(R.id.text_view_ago_search);
        mRanks = (TextView) itemView.findViewById(R.id.text_view_ranks_search);
    }


}
