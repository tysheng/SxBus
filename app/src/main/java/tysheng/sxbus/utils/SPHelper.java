package tysheng.sxbus.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by shengtianyang on 16/2/22.
 */
public class SPHelper {
    private final String SP_NAME = "SP_NAME";
    private SharedPreferences mSharedPreferences;

    public SPHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void put(String key, int i) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, i);
        editor.apply();
    }

    public int get(String key, int s) {
        return mSharedPreferences.getInt(key, s);
    }

    public void put(String key, boolean i) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, i);
        editor.apply();
    }

    public boolean get(String key, boolean s) {
        return mSharedPreferences.getBoolean(key, s);
    }

    public void clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear().apply();
    }
}
