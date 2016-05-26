package com.example.user01.planit;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Angel on 5/26/2016.
 */
public class LoginLogoutHelpers {



     public static void saveUser(User user, Activity activity){
        //Creating a shared preference
        SharedPreferences mPrefs = activity.getBaseContext().getSharedPreferences("UserLoginData",0);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("currUser", json);
        prefsEditor.putBoolean("loggedIn", true);
        prefsEditor.commit();
    }

    public static User retrieveUser(Activity activity){
        SharedPreferences mPrefs = activity.getBaseContext().getSharedPreferences("UserLoginData",0);
        Gson gson = new Gson();
        String json = mPrefs.getString("currUser", "");
        return gson.fromJson(json, User.class);
    }

    public static boolean retrieveLoggedIn(Activity activity){
        SharedPreferences mPrefs = activity.getBaseContext().getSharedPreferences("UserLoginData",0);
        return mPrefs.getBoolean("loggedIn", false);
    }


    public static void logUserOut(Activity activity){
        SharedPreferences mPrefs = activity.getBaseContext().getSharedPreferences("UserLoginData",0);
        mPrefs.edit().clear().commit();
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putBoolean("loggedIn", false);
        prefsEditor.commit();
    }


}
