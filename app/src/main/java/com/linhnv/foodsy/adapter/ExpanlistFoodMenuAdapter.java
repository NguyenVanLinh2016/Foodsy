package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.FoodMenu;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by linhnv on 13/07/2017.
 */

public class ExpanlistFoodMenuAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<FoodMenu>> listDataChild;
    public ExpanlistFoodMenuAdapter(Context context, List<String> listDataHeader, HashMap<String, List<FoodMenu>> listDataChild){
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        TextView text_listHeader = (TextView) convertView.findViewById(R.id.text_view_listHeader);
        text_listHeader.setTypeface(null, Typeface.BOLD);
        text_listHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FoodMenu foodMenu = (FoodMenu) getChild(groupPosition, childPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_food_menu, null);
        }
        TextView text_name_food = (TextView) convertView.findViewById(R.id.text_view_nameFood);
        TextView text_description = (TextView) convertView.findViewById(R.id.text_view_content_name);
        TextView text_foodPrice = (TextView) convertView.findViewById(R.id.text_view_Price);
        ImageView image_food = (ImageView) convertView.findViewById(R.id.image_food_detail);
        text_name_food.setText(foodMenu.getName());
        text_description.setText(foodMenu.getDescription());
        text_foodPrice.setText(String.valueOf(foodMenu.getPrice()));
        Picasso.with(context)
                .load(foodMenu.getPhoto())
                .placeholder(R.drawable.bglogin5)
                .error(R.drawable.bglogin5)
                .into(image_food);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
