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
import com.linhnv.foodsy.model.PLaceFoodMenu;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by linhnv on 11/06/2017.
 */

public class PlaceFoodMenuAdapter extends RecyclerView.Adapter<PlaceFoodMenuAdapter.PlaceFoodMenuViewHolder>{
    private List<PLaceFoodMenu> mListPlaceFoodMenu;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public PlaceFoodMenuAdapter(PlaceDetailActivity context, List<PLaceFoodMenu> data){
        this.mContext = context;
        this.mListPlaceFoodMenu = data;
        this.mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public PlaceFoodMenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.row_item_food_menu, parent, false);

        return new PlaceFoodMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceFoodMenuViewHolder holder, int position) {
        PLaceFoodMenu pLaceFoodMenu = mListPlaceFoodMenu.get(position);
        holder.txtFoodName.setText(pLaceFoodMenu.getFoodName());
        holder.txtFoodDescription.setText(pLaceFoodMenu.getFoodDescription());
        holder.txtFoodPrice.setText(pLaceFoodMenu.getPrice() +" k");
    }

    @Override
    public int getItemCount() {
        return mListPlaceFoodMenu.size();
    }

    class PlaceFoodMenuViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgFoodPhoto;
        private TextView txtFoodName, txtFoodDescription, txtFoodPrice;
        public PlaceFoodMenuViewHolder(View itemView) {
            super(itemView);

            txtFoodName = (TextView) itemView.findViewById(R.id.text_view_nameFood);
            txtFoodDescription = (TextView) itemView.findViewById(R.id.text_view_content_name);
            txtFoodPrice = (TextView) itemView.findViewById(R.id.text_view_Price);
        }
    }
}
