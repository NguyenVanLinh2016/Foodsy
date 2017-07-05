package com.linhnv.foodsy.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linhnv on 17/06/2017.
 */

public class MyFunctions {

    //url register
    String url_register = "foodsy-api.herokuapp.com/api/auth/register";

    public JSONObject user_register(String username, String password){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
