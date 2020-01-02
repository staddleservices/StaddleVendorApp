package staddlevendor.com.staddlevendor.sheardPref;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String PREFS_NAME = "MyPrefs";

    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static String loadPreferences(Context context, String key) {
        SharedPreferences settings;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, "");
    }
}
