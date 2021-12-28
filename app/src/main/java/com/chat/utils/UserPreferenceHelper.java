package com.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.chat.main.model.user.UserResponse;
import com.chat.network.DevartlinkAPI;
import com.google.gson.Gson;

public class UserPreferenceHelper {
    private static SharedPreferences sharedPreferences = null;

    //here you can find shared preference operations like get saved data for user


    private UserPreferenceHelper() {

    }

    public static SharedPreferences getSharedPreferenceInstance(Context context) {
        if (sharedPreferences != null) return sharedPreferences;
        return sharedPreferences = context.getSharedPreferences("savedData",
                Context.MODE_PRIVATE);
    }

    public static void saveUserProfile(UserResponse user) {
        SharedPreferences.Editor prefsEditor = getSharedPreferenceInstance(DevartlinkAPI
                .getInstance().getApplicationContext()).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("userDetails", json);
        prefsEditor.apply();
    }

    public static UserResponse getUser() {
        Gson gson = new Gson();
        String json = getSharedPreferenceInstance(DevartlinkAPI.getInstance().getApplicationContext())
                .getString("userDetails", "");
        if (json.equals("")) return new UserResponse();
        return gson.fromJson(json, UserResponse.class);
    }

}
