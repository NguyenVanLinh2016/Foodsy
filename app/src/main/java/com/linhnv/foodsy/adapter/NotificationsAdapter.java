package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Notifications;
import com.linhnv.foodsy.model.Places;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by linhnv on 16/06/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {
    private List<Notifications> notiList;
    private Context context;
    private LayoutInflater layoutInflater;
    public NotificationsAdapter(Context context, List<Notifications> data){
        this.context = context;
        this.notiList = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_item_notifications, parent, false);
        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {
        Notifications notifications = notiList.get(position);
        holder.text_nameResNoti.setText(notifications.getPlace_name());
        holder.text_ago_noti.setText(notifications.getTitle());
        String datetime = notifications.getUpdated_at();
        //2017-07-09 03:57:21
        String day = (String) datetime.substring(8,10);
        String month = (String) datetime.substring(5,7);
        String year = (String) datetime.substring(0,4);
        String timer = (String) datetime.substring(11, 16);
        holder.text_ago_noti.setText(timer +"  "+ day +"-"+ month);
        holder.text_Des.setText(notifications.getTitle() +"\n"+ notifications.getSale());
        Picasso.with(context).load(notifications.getPhoto()).into(holder.image_noti);
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }

    class NotificationsViewHolder extends RecyclerView.ViewHolder{
        private ImageView image_noti;
        private TextView text_nameResNoti, text_Des, text_ago_noti;

        public NotificationsViewHolder(View itemView) {
            super(itemView);
            image_noti = (ImageView) itemView.findViewById(R.id.image_noti);

            text_nameResNoti = (TextView) itemView.findViewById(R.id.text_view_nameResNoti);
            text_Des = (TextView) itemView.findViewById(R.id.text_view_food_decription);
            text_ago_noti = (TextView) itemView.findViewById(R.id.text_view_ago_noti);
        }
    }
}
