package com.linhnv.foodsy.model;

/**
 * Created by Huu on 08/07/2017.
 */

public class User {
    private int id;
    private String username;
    private String display_name;
    private String email;
    private String phone_number;
    private String address;
    private String photo;
    private String gender;
    private String role;
    private String status;

    public User() {
    }

    public User(int id, String username, String display_name, String email,
                String phone_number, String address, String photo, String gender, String role, String status) {
        this.id = id;
        this.username = username;
        this.display_name = display_name;
        this.email = email;
        this.phone_number = phone_number;
        this.address = address;
        this.photo = photo;
        this.gender = gender;
        this.role = role;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
