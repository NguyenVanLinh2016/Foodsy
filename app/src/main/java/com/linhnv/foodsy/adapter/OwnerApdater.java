package com.linhnv.foodsy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.activity.AddplaceActivity;
import com.linhnv.foodsy.activity.EditplaceActivity;
import com.linhnv.foodsy.activity.PlaceDetailActivity;
import com.linhnv.foodsy.activity.SearchActivity;
import com.linhnv.foodsy.activity.ViewPlacesOwner;
import com.linhnv.foodsy.model.Places;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by linhnv on 21/05/2017.
 */

public class OwnerApdater extends RecyclerView.Adapter<OwnerApdater.PlaceViewHolder> {
    private List<Places> placeList;
    private Context context;
    private LayoutInflater layoutInflater;
    public OwnerApdater(Context context, List<Places> data) {
        this.context = context;
        this.placeList = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_place, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {

        Places place = placeList.get(position);
        holder.txtName_Res.setText(place.getDisplay_name());
        holder.txtName_Food.setText(place.getAddress());
        holder.txtAgo.setText("Sửa");
        holder.tv_id.setText(String.valueOf(place.getId()));
        Picasso.with(context)
                .load(place.getPhoto())
                .placeholder(R.drawable.bglogin5)
                .error(R.drawable.bglogin5)
                .into(holder.imgPlace);


    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }



    class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imgPlace;
        private TextView txtName_Res, txtName_Food, txtAgo,tv_id;
        int id;
        public PlaceViewHolder(View itemView) {
            super(itemView);
            imgPlace = (ImageView) itemView.findViewById(R.id.imgPlace);
            txtName_Res = (TextView) itemView.findViewById(R.id.txtName_Res);
            txtName_Food = (TextView) itemView.findViewById(R.id.txtName_Food);
            txtAgo = (TextView) itemView.findViewById(R.id.txtAgo);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            txtAgo.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txtAgo:
                    Intent intent = new Intent(context, EditplaceActivity.class);
                    Bundle b = new Bundle();
                    id = Integer.valueOf(tv_id.getText().toString());
                    b.putInt("id", id);
                    Log.e("id owner", String.valueOf(id));
                    intent.putExtras(b);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                    break;

            }
            id = Integer.valueOf(tv_id.getText().toString());
           Intent i =  new Intent(context, ViewPlacesOwner.class);
            Bundle c = new Bundle();
            c.putInt("id_owner", id);
            i.putExtras(c);
            context.startActivity(i);
        }
    }
}
