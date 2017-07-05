package com.linhnv.foodsy.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by linhnv on 10/06/2017.
 */

public class DrawsLineItem extends RecyclerView.ItemDecoration {
    private static final int []ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDrawale;
    private int mOrientation;
    public  DrawsLineItem(Context context, int orentation){
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDrawale = a.getDrawable(0);
        a.recycle();
        setOrientaion(orentation);
    }
    public void setOrientaion(int orientaion){
        if(orientaion != HORIZONTAL_LIST && orientaion != VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientaion;
    }
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state){
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawale.getIntrinsicHeight();
            mDrawale.setBounds(left, top, right, bottom);
            mDrawale.draw(c);
        }
    }
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDrawale.getIntrinsicHeight();
            mDrawale.setBounds(left, top, right, bottom);
            mDrawale.draw(c);
        }
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDrawale.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDrawale.getIntrinsicWidth(), 0);
        }
    }
}
