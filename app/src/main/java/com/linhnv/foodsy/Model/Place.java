package com.linhnv.foodsy.Model;

import android.widget.ImageView;

/**
 * Created by linhnv on 21/05/2017.
 */

public class Place {
    private int id;
    private String nameRestaurant;
    private String nameFood;
    private int ago;
    public void Place(){

    }

    public Place(int id, String nameRestaurant, String nameFood, int ago) {
        this.id = id;
        this.nameRestaurant = nameRestaurant;
        this.nameFood = nameFood;
        this.ago = ago;
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
    public String getNameFood(){
        return nameFood;
    }
    public void setNameFood(String nameFood){
        this.nameFood = nameFood;
    }
    public int getAgo(){
        return ago;
    }
    public void setAgo(int ago){
        this.ago = ago;
    }
}
