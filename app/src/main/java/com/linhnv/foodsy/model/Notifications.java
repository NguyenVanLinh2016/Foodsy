package com.linhnv.foodsy.model;

/**
 * Created by linhnv on 16/06/2017.
 */

public class Notifications {
    private String mDueDate;
    private String mNameRes;
    private String mContent;

    public Notifications(){

    }
    public Notifications(String mDueDate, String mNameRes, String mContent) {
        this.mDueDate = mDueDate;
        this.mNameRes = mNameRes;
        this.mContent = mContent;
    }

    public String getmDueDate() {
        return mDueDate;
    }

    public void setmDueDate(String mDueDate) {
        this.mDueDate = mDueDate;
    }

    public String getmNameRes() {
        return mNameRes;
    }

    public void setmNameRes(String mNameRes) {
        this.mNameRes = mNameRes;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
}
