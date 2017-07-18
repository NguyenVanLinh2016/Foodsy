package com.linhnv.foodsy.model;

/**
 * Created by linhnv on 11/06/2017.
 */

public class PlaceFoodReviews {
    private int id;
    private String message;
    private String photo;
    private String rating;
    private int user_id;
    private int place_id;
    private String status;
    private String created_at;
    private String updated_at;

    public PlaceFoodReviews(){

    }
    public PlaceFoodReviews(int id, String message, String photo, String rating, int user_id, int place_id, String status, String created_at, String updated_at) {
        this.id = id;
        this.message = message;
        this.photo = photo;
        this.rating = rating;
        this.user_id = user_id;
        this.place_id = place_id;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPlace_id() {
        return place_id;
    }

    public void setPlace_id(int place_id) {
        this.place_id = place_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
