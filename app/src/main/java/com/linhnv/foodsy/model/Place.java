package com.linhnv.foodsy.model;

/**
 * Created by linhnv on 21/05/2017.
 */

public class Place {
    private int id;
    private String nameRestaurant;
    private String address;
    private String nameFood;
    private String category;
    private String ago;
    private int rank;
    public void Place(){

    }

    public Place(int id, String nameRestaurant, String address, String nameFood, String category, String ago, int rank) {
        this.id = id;
        this.nameRestaurant = nameRestaurant;
        this.address = address;
        this.nameFood = nameFood;
        this.category = category;
        this.ago = ago;
        this.rank = rank;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNameRestaurant() {
        return nameRestaurant;
    }

    public void setNameRestaurant(String nameRestaurant) {
        this.nameRestaurant = nameRestaurant;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getNameFood(){
        return nameFood;
    }
    public void setNameFood(String nameFood){
        this.nameFood = nameFood;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public String getAgo(){
        return ago;
    }
    public void setAgo(String ago){
        this.ago = ago;
    }
    public int getRank(){
        return rank;
    }
    public void setRank(int rank){
        this.rank = rank;
    }
}
