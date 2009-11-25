package net.johnpwood.android.standuptimer;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {
    private static final String SOUNDS = "sounds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    public static boolean playSounds(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SOUNDS, true);
    }
}
