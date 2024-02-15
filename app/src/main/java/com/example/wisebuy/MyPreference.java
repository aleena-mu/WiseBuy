package com.example.wisebuy;

import android.content.Context;
import android.content.SharedPreferences;



public class MyPreference {
    private final SharedPreferences preferences;

    public MyPreference(Context context) {
        String USER_PREFERENCE = "User";
        preferences = context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
    }

   public void saveUserDetailsToSharedPreferences(String username, String userId) {

        if (preferences.getString("username", null) == null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", username);
            editor.putString("userId", userId);
            editor.apply();
        }
    }

   public void deleteUserFromSharedPreferences() {
       SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
    public String getUsername() {
        return preferences.getString("username", null);
    }

    public String getUserId() {
        return preferences.getString("userId", null);

    }
}
