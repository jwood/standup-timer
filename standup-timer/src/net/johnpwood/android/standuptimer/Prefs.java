package net.johnpwood.android.standuptimer;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {
    private static final String SOUNDS = "sounds";
    private static boolean SOUNDS_DEFAULT = true;
    private static final String WARNING_TIME = "warning_time";
    private static final String WARNING_TIME_DEFAULT = "15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    public static boolean playSounds(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SOUNDS, SOUNDS_DEFAULT);
    }

    public static int getWarningTime(Context context) {
        String value = PreferenceManager.getDefaultSharedPreferences(context).getString(WARNING_TIME, WARNING_TIME_DEFAULT);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(WARNING_TIME, WARNING_TIME_DEFAULT).commit();
            return Integer.parseInt(WARNING_TIME_DEFAULT);
        }
    }
}
