package com.linhnv.foodsy.network;

/**
 * Created by linhnv on 17/06/2017.
 */

public class ApiURL {
    //SigninActivity, SignupActivity
    public static final String URL_SIGNIN = "https://foodsyapp.herokuapp.com/api/auth/login";
    public static final String URL_SIGNIN_SOCIAL = "https://foodsyapp.herokuapp.com/api/auth/login/social";
    public static final String URL_GET_PROFILE = "https://foodsyapp.herokuapp.com/api/user/profile";
    public static final String URL_REGISTER = "https://foodsyapp.herokuapp.com/api/auth/register";
    //UpdateInfoActivity
    public static final String URL_UPDATE_INFO = "https://foodsyapp.herokuapp.com/api/user/update";
    //HomeFragment
    public static final String URL_IMAGE = "https://foodsyapp.herokuapp.com/api/place/photo";
    public static final String URL_PLACE_EAT = "https://foodsyapp.herokuapp.com/api/place/category/eat";
    public static final String URL_PLACE_DRINK = "https://foodsyapp.herokuapp.com/api/place/category/drink";
    public static final String URL_PLACE_ENTERTAIN = "https://foodsyapp.herokuapp.com/api/place/category/entertain";
    //BookmarkFragment
    public static final String URL_PLACE_BOOKMARK = "https://foodsyapp.herokuapp.com/api/place";
    public static final String URL_PLACE_IMAGE = "https://foodsyapp.herokuapp.com/api/place/photo";
    //NotificationFragment
    public static final String URL_NOTIFICATIONS = "https://foodsyapp.herokuapp.com/api/event/available";
    public static final String URL_LOADIMAGE = "https://foodsyapp.herokuapp.com/api/event/";
    //MapFragment
    public static final String URL_PLACE_AROUND = "https://foodsyapp.herokuapp.com/api/place/around";
}
