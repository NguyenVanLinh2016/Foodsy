package com.linhnv.foodsy.model;

/**
 * Created by linhnv on 11/06/2017.
 */

public class PlaceFoodReviews {
    private String displayName;
    private String dateCreate;
    private String comment;

    public PlaceFoodReviews(){

    }
    public PlaceFoodReviews(String displayName, String dateCreate, String comment) {
        this.displayName = displayName;
        this.dateCreate = dateCreate;
        this.comment = comment;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
