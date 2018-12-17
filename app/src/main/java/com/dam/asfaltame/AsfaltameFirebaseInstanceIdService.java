package com.dam.asfaltame;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class AsfaltameFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onTokenRefresh(){
        String rt = FirebaseInstanceId.getInstance().getToken();
        guardarToken(rt);
    }


    private void guardarToken(String token) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("registration_id", token);
        editor.apply();
    }

    private String leerToken() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("registration_id", null);
    }
}
