package com.linhnv.foodsy.Model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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

            txtDueDate = (TextView) itemView.findViewById(R.id.text_view_dueDate_noti);
            txtNameRes = (TextView) itemView.findViewById(R.id.text_view_nameRes_noti);
            txtContent = (TextView) itemView.findViewById(R.id.text_view_content_noti);
        }
    }
}
