package com.linhnv.foodsy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linhnv.foodsy.R;
import com.linhnv.foodsy.model.Place;
import com.linhnv.foodsy.model.Places;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by linhnv on 14/06/2017.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {

    private List<Places> bookmarkList;
    private Context context;
    private LayoutInflater layoutInflater;
    public BookmarkAdapter(Context context, List<Places> data){
        this.context = context;
        this.bookmarkList = data;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_bookmark, parent, false);
        return new BookmarkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, int position) {
        Places places = bookmarkList.get(position);
        holder.txtName_bookmark.setText(places.getDisplay_name());
        Picasso.with(context)
                .load(places.getPhoto())
                .placeholder(R.drawable.bglogin5)
                .error(R.drawable.bglogin5)
                .into(holder.image_bookmark);
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    class BookmarkViewHolder extends RecyclerView.ViewHolder{
        private ImageView image_bookmark;
        private TextView txtName_bookmark;
        public BookmarkViewHolder(View itemView) {
            super(itemView);
            image_bookmark = (ImageView) itemView.findViewById(R.id.image_view_bookmark);
            txtName_bookmark = (TextView) itemView.findViewById(R.id.text_view_namePlace_bookmark);
        }
    }
}
