package com.linhnv.foodsy.model;

/**
 * Created by linhnv on 22/07/2017.
 */

public class OwnerRegister {
    private int id_user;
    private String display_name_user;
    private int id_place;
    private String display_name_place;
    private String address;
    private String phone_number;
    private String photo;

    public OwnerRegister(){}

    public OwnerRegister(int id_user, String display_name_user, int id_place, String display_name_place, String address, String phone_number, String photo) {
        this.id_user = id_user;
        this.display_name_user = display_name_user;
        this.id_place = id_place;
        this.display_name_place = display_name_place;
        this.address = address;
        this.phone_number = phone_number;
        this.photo = photo;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getDisplay_name_user() {
        return display_name_user;
    }

    public void setDisplay_name_user(String display_name_user) {
        this.display_name_user = display_name_user;
    }

    public int getId_place() {
        return id_place;
    }

    public void setId_place(int id_place) {
        this.id_place = id_place;
    }

    public String getDisplay_name_place() {
        return display_name_place;
    }

    public void setDisplay_name_place(String display_name_place) {
        this.display_name_place = display_name_place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
