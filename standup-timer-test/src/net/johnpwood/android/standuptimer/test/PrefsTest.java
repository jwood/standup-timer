package net.johnpwood.android.standuptimer.test;

import net.johnpwood.android.standuptimer.Prefs;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class PrefsTest extends ActivityUnitTestCase<Prefs> {

    private Prefs a = null;

    public PrefsTest() {
        super(Prefs.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        a = startActivity(intent, null, null);
    }

    @MediumTest
    public void test_can_get_and_set_the_play_sounds_setting() {
        Prefs.setPlaySounds(a, false);
        assertFalse(Prefs.playSounds(a));
        Prefs.setPlaySounds(a, true);
        assertTrue(Prefs.playSounds(a));
    }

    @MediumTest
    public void test_can_get_and_set_the_warning_time() {
        Prefs.setWarningTime(a, 22);
        assertEquals(22, Prefs.getWarningTime(a));
    }

    @MediumTest
    public void test_invalid_warning_times_are_reset_to_the_default() {
        PreferenceManager.getDefaultSharedPreferences(a).edit().putString("warning_time", "abcdefg").commit();
        assertEquals(15, Prefs.getWarningTime(a));
        assertEquals("15", PreferenceManager.getDefaultSharedPreferences(a).getString("warning_time", "1"));        
    }
}
