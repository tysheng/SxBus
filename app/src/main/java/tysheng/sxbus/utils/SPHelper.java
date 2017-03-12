package tysheng.sxbus.utils;

import android.content.Context;
import android.content.SharedPreferences;

import tysheng.sxbus.App;


/**
 * Created by shengtianyang on 16/2/22.
 */
public class SPHelper {
    private static final String SP_NAME = "SP_NAME";
    private static SharedPreferences mSharedPreferences;

    private SPHelper() {
    }

    public static void put(String key, String value) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String get(String key, String defaultValue) {
        return getInstance().getString(key, defaultValue);
    }

    public static void put(String key, boolean value) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean get(String key, boolean defaultValue) {
        return getInstance().getBoolean(key, defaultValue);
    }

    public static void put(String key, int i) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putInt(key, i);
        editor.apply();
    }

    private static SharedPreferences getInstance() {
        if (mSharedPreferences == null) {
            mSharedPreferences = App.get().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public static int get(String key, int s) {
        return getInstance().getInt(key, s);
    }

    public static void clear() {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.clear().apply();
    }
}
