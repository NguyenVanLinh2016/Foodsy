package com.linhnv.foodsy.model;

/**
 * Created by linhnv on 16/06/2017.
 */

public class Notifications {
    private int id;
    private String title;
    private String content;
    private String photo;
    private String sale;
    private String time_start;
    private String time_end;
    private String status;
    private String place_id;
    private String created_at;
    private String updated_at;
    private String place_name;

    public Notifications(){

    }

    public Notifications(int id, String title, String content, String photo, String sale, String time_start, String time_end, String status, String place_id, String created_at, String updated_at, String place_name) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.photo = photo;
        this.sale = sale;
        this.time_start = time_start;
        this.time_end = time_end;
        this.status = status;
        this.place_id = place_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.place_name = place_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
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

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }
}
