package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.model.Places;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by linhnv on 21/05/2017.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>{

    private List<Places> placeList;
    private Context context;
    private LayoutInflater layoutInflater;
    public PlaceAdapter(Context context, List<Places> data){
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
        holder.txtAgo.setText("Cách đây " + place.getCity()+ " phút");
        Picasso.with(context).load(place.getPhoto()).into(holder.imgPlace);

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPlace;
        private TextView txtName_Res, txtName_Food, txtAgo;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            imgPlace = (ImageView) itemView.findViewById(R.id.imgPlace);
            txtName_Res = (TextView) itemView.findViewById(R.id.txtName_Res);
            txtName_Food = (TextView) itemView.findViewById(R.id.txtName_Food);
            txtAgo = (TextView) itemView.findViewById(R.id.txtAgo);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int item = placeList.get()
//                }
//            });
        }
    }
}
