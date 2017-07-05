package com.linhnv.foodsy.model;

/**
 * Created by linhnv on 11/06/2017.
 */

public class PLaceFoodMenu {

    public String foodName;
    public String foodDescription;
    public double price;
    public PLaceFoodMenu(){

    }
    public PLaceFoodMenu(String foodName, String foodDescription, double price){
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
