package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.OwnerRegister;
import com.linhnv.foodsy.model.Places;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by linhnv on 22/07/2017.
 */

public class OwerRegisterAdapter extends RecyclerView.Adapter<OwerRegisterAdapter.OwerRegisterViewHolder> {

    private List<OwnerRegister> ownerList;
    private Context context;
    private LayoutInflater layoutInflater;
    public OwerRegisterAdapter(Context context, List<OwnerRegister> data){
        this.context = context;
        this.ownerList = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public OwerRegisterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_owner_register, parent, false);
        return new OwerRegisterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OwerRegisterViewHolder holder, int position) {
        OwnerRegister ownerRegister = ownerList.get(position);
        holder.txtName_Res.setText(ownerRegister.getDisplay_name_place());
        holder.txtName_Food.setText(ownerRegister.getAddress() +"\n"+ ownerRegister.getDisplay_name_user());
        Picasso.with(context)
                .load(ownerRegister.getPhoto())
                .placeholder(R.drawable.bglogin5)
                .error(R.drawable.bglogin5)
                .into(holder.imgPlaceOwner);
    }

    @Override
    public int getItemCount() {
        return ownerList.size();
    }


    class OwerRegisterViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgPlaceOwner;
        private TextView txtName_Res, txtName_Food;

        public OwerRegisterViewHolder(View itemView) {
            super(itemView);
            imgPlaceOwner = (ImageView) itemView.findViewById(R.id.imgPlaceOwner);
            txtName_Res = (TextView) itemView.findViewById(R.id.txtName_Res);
            txtName_Food = (TextView) itemView.findViewById(R.id.txtName_Food);
        }
    }
}
