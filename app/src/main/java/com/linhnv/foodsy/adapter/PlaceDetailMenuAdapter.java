package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.activity.PlaceDetailActivity;
import com.linhnv.foodsy.model.FoodMenu;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.model.Places;

import java.util.List;

/**
 * Created by linhnv on 11/06/2017.
 */

public class PlaceDetailMenuAdapter extends RecyclerView.Adapter<PlaceDetailMenuAdapter.PlaceDetailsMenuViewHolder>{
    private List<Places> mListPlaceFoodMenu;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public PlaceDetailMenuAdapter(PlaceDetailActivity context, List<Places> data){
        this.mContext = context;
        this.mListPlaceFoodMenu = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public PlaceDetailsMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.row_item_food_detail, parent, false);
        return new PlaceDetailsMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceDetailsMenuViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mListPlaceFoodMenu.size();
    }

    class PlaceDetailsMenuViewHolder extends RecyclerView.ViewHolder{

        TextView text_address, text_phone_detail, text_email_detail, text_price_detail, text_time_detail, text_des_detail;
        public PlaceDetailsMenuViewHolder(View itemView) {
            super(itemView);

            text_address = (TextView) itemView.findViewById(R.id.text_view_address_detail);
            text_phone_detail = (TextView) itemView.findViewById(R.id.text_view_phone_detail);
            text_email_detail = (TextView) itemView.findViewById(R.id.text_view_email_detail);
            text_price_detail = (TextView) itemView.findViewById(R.id.text_view_price_detail);
            text_email_detail = (TextView) itemView.findViewById(R.id.text_view_time_detail);
            text_time_detail = (TextView) itemView.findViewById(R.id.text_view_email_detail);
            text_des_detail = (TextView) itemView.findViewById(R.id.text_view_des_detail);
        }
    }
}
