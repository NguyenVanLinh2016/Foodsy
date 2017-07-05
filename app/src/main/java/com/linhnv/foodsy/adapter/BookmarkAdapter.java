package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Place;

import java.util.List;

/**
 * Created by linhnv on 14/06/2017.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    private List<Place> placeList;
    private Context context;
    private LayoutInflater layoutInflater;
    public BookmarkAdapter(Context context, List<Place> data){
        this.context = context;
        this.placeList = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_bookmark, parent, false);
        return new BookmarkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.txtName_bookmark.setText(place.getNameRestaurant());
        holder.btnAgo_bookmark.setText("Cách đây " +place.getAgo()+ " P");
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    class BookmarkViewHolder extends RecyclerView.ViewHolder{
        //private ImageView imgPlace;
        private TextView txtName_bookmark;
        private Button btnAgo_bookmark;
        public BookmarkViewHolder(View itemView) {
            super(itemView);

            txtName_bookmark = (TextView) itemView.findViewById(R.id.text_view_namePlace_bookmark);
            btnAgo_bookmark = (Button) itemView.findViewById(R.id.button_ago_boormark);
        }
    }
}
