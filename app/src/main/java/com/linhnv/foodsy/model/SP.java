package com.linhnv.foodsy.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.linhnv.foodsy.R;

/**
 * Created by Huu on 28/06/2017.
 */


public class SP {
    Context _context;
    private static final String PREFS_FILE = "login";
    private static final int PREFS_MODE = Context.MODE_PRIVATE;
    private static final String KEY_USER = "user";
    private static final String PREFS_FILE_USER = "user_info";
    private static final String KEY_PHONE = "phonenumber";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longtitude";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_INFO= "info_user";

    public SP(Context context) {
        this._context = context;
    }

    public void setStateLogin(boolean value) {
        SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = prefs.edit();
        // write state login
        editor.putBoolean(KEY_USER, value);
        editor.commit();
    }

    public boolean getStateLogin() {
        SharedPreferences user = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return user.getBoolean(KEY_USER, false);
    }

    public void setPhoneNumber(String value) {
        SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = prefs.edit();
        // write a phone number
        editor.putString(KEY_PHONE, value);
        editor.commit();
    }

    public String getPhoneNumber() {
        SharedPreferences user = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return user.getString(KEY_PHONE, "");
    }

    public void setLocationUser(double latitude, double longitude) {
        SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(KEY_LATITUDE, Float.valueOf(String.valueOf(latitude)));
        editor.putFloat(KEY_LONGITUDE, Float.valueOf(String.valueOf(longitude)));
        editor.commit();
    }

    public Float getLatitude() {
        SharedPreferences user = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return user.getFloat(KEY_LATITUDE, 0);
    }

    public Float getLongitude() {
        SharedPreferences user = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return user.getFloat(KEY_LONGITUDE, 0);
    }

    public void setToken(String value) {
        SharedPreferences prefs = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        SharedPreferences.Editor editor = prefs.edit();
        // write state login
        editor.putString(KEY_TOKEN, value);
        editor.commit();
    }

    public String getToken() {
        SharedPreferences user = _context.getSharedPreferences(PREFS_FILE, PREFS_MODE);
        return user.getString(KEY_TOKEN, "");
    }
    public void setUser(String object){
               SharedPreferences mPreferences = _context.getSharedPreferences(PREFS_FILE_USER, PREFS_MODE);
               SharedPreferences.Editor preEditor = mPreferences.edit();
               Gson gson = new Gson();
               String json = gson.toJson(object);
               preEditor.putString(KEY_USER_INFO, json);
               preEditor.commit();
           }
    public String getUser(){
        SharedPreferences user = _context.getSharedPreferences(PREFS_FILE_USER, PREFS_MODE);
        return user.getString(KEY_USER_INFO, "");
    }
}