package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Places;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by linhnv on 21/05/2017.
 */

public class PlaceAdapterShimmer extends RecyclerView.Adapter<PlaceAdapterShimmer.ShimmerViewHolder>{

    private List<Places> placeList;
    private Context context;
    private LayoutInflater layoutInflater;
    public PlaceAdapterShimmer(Context context, List<Places> data){
        this.context = context;
        this.placeList = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ShimmerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_place, parent, false);
        return new ShimmerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShimmerViewHolder holder, int position) {

        Places place = placeList.get(position);
        holder.txtName_Res.setText(place.getDisplay_name());
        holder.txtName_Food.setText(place.getAddress());
        holder.txtAgo.setText("Cách đây " + place.getCity()+ " phút");
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

    class ShimmerViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPlace;
        private TextView txtName_Res, txtName_Food, txtAgo;

        public ShimmerViewHolder(View itemView) {
            super(itemView);
            imgPlace = (ImageView) itemView.findViewById(R.id.imgPlace);
            txtName_Res = (TextView) itemView.findViewById(R.id.txtName_Res);
            txtName_Food = (TextView) itemView.findViewById(R.id.txtName_Food);
            txtAgo = (TextView) itemView.findViewById(R.id.txtAgo);

        }
    }
}
