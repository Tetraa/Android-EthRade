package com.example.aureliengiudici.ethrade.Configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aureliengiudici on 22/02/2018.
 */

public class ConfigurationManager {
    private static final String TAG = "ConfigurationManager";
    private static final String PREF_KEY = "com.example.aureliengiudici.ethrade.PREFERENCE_FILE_KEY";
    private final Context context;

    public ConfigurationManager(Context context){
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getSharedPreferencesEditor() {
        return this.getSharedPreferences().edit();
    }


    public void putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putString(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putLong(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.putFloat(key, value);
        editor.commit();
    }

    public String getString(String key) {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getString(key, null);
    }

    public boolean getBoolean(String key) {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getBoolean(key, false);
    }

    public int getInt(String key) {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getInt(key, -1);
    }

    public long getLong(String key) {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getLong(key, -1);
    }

    public float getFloat(String key) {
        SharedPreferences prefs = getSharedPreferences();
        return prefs.getFloat(key, -1);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor();
        editor.remove(key);
        editor.commit();
    }


    private JSONObject formatToJSON(String data){
        try{
            return new JSONObject(data);
        } catch (JSONException e){
            Log.d(TAG, e.toString());
            return null;
        }
    }
}
