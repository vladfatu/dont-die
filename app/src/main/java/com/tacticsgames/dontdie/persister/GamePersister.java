package com.tacticsgames.dontdie.persister;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vladfatu on 11/03/2016.
 */
public class GamePersister {

    private static final String NAMESPACE = "dont-die";
    private SharedPreferences sharedPreferences;

    public GamePersister(Context context) {
        sharedPreferences = context.getSharedPreferences(NAMESPACE, Context.MODE_PRIVATE);
    }

    public void persistString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getPersistedString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void persistBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getPersistedBoolean(String key, boolean defaultValueToReturn) {
        return sharedPreferences.getBoolean(key, defaultValueToReturn);
    }

}
