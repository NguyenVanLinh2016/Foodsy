package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Category;
import com.linhnv.foodsy.model.Places;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by linhnv on 21/05/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.PlaceViewHolder>{

    private List<Category> categoryList;
    private Context context;
    private LayoutInflater layoutInflater;
    public CategoryAdapter(Context context, List<Category> data){
        this.context = context;
        this.categoryList = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_category, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.txtCategory.setText(category.getName());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder{
        private TextView txtCategory;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            txtCategory = (TextView) itemView.findViewById(R.id.tv_category_product);
        }
    }
}
