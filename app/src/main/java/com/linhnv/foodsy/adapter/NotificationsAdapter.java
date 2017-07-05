package com.linhnv.foodsy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linhnv.foodsy.R;

/**
 * Created by linhnv on 16/06/2017.
 */

public class NotificationsAdapter {


    class NotificationsViewHolder extends RecyclerView.ViewHolder{

        private TextView txtDueDate, txtNameRes, txtContent;

        public NotificationsViewHolder(View itemView) {
            super(itemView);

            txtDueDate = (TextView) itemView.findViewById(R.id.text_view_Price);
            txtNameRes = (TextView) itemView.findViewById(R.id.text_view_nameFood);
          //  txtContent = (TextView) itemView.findViewById(R.id.text_view_content_noti);
        }
    }
}
