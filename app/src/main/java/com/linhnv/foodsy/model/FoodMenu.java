package com.linhnv.foodsy.model;

/**
 * Created by linhnv on 13/07/2017.
 */

public class FoodMenu {
    private int id;
    private String name;
    private String description;
    private String photo;
    private int price;
    private String type;
    private String status;
    private int category_id;

    public FoodMenu(){

    }

    public FoodMenu(int id, String name, String description, String photo, int price, String type, String status, int category_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.price = price;
        this.type = type;
        this.status = status;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
