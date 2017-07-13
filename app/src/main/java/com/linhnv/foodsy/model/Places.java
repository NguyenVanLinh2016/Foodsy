package com.linhnv.foodsy.model;

/**
 * Created by Huu on 06/07/2017.
 */

public class Places {
    private int id;
    private String display_name;
    private String description;
    private String address;
    private String city;
    private String phone_number;
    private String email;
    private String photo;
    private String price_limit;
    private String time_open;
    private String time_close;
    private String wifi_password;
    private Double latitude;
    private Double longitude;
    private String status;
    private int user_id;

    public Places() {
    }

    public Places(String display_name, String description, String address, String city) {
        this.display_name = display_name;
        this.description = description;
        this.address = address;
        this.city = city;
    }

    public Places(String address, String phone_number, String email, String price_limit, String time_open, String time_close, String wifi_password, String description){
        this.address = address;
        this.phone_number = phone_number;
        this.price_limit = price_limit;
        this.time_open = time_open;
        this.time_close = time_close;
        this.wifi_password = wifi_password;
        this.description = description;
    }

    public Places(int id, String display_name, String description, String address,
                  String city, String phone_number, String email, String photo, String price_limit, String time_open, String time_close,
                  String wifi_password, Double latitude, Double longitude, String status, int user_id) {
        this.id = id;
        this.display_name = display_name;
        this.description = description;
        this.address = address;
        this.city = city;
        this.phone_number = phone_number;
        this.email = email;
        this.photo = photo;
        this.price_limit = price_limit;
        this.time_open = time_open;
        this.time_close = time_close;
        this.wifi_password = wifi_password;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.user_id = user_id;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrice_limit() {
        return price_limit;
    }

    public void setPrice_limit(String price_limit) {
        this.price_limit = price_limit;
    }

    public String getTime_open() {
        return time_open;
    }

    public void setTime_open(String time_open) {
        this.time_open = time_open;
    }

    public String getTime_close() {
        return time_close;
    }

    public void setTime_close(String time_close) {
        this.time_close = time_close;
    }

    public String getWifi_password() {
        return wifi_password;
    }

    public void setWifi_password(String wifi_password) {
        this.wifi_password = wifi_password;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


}
