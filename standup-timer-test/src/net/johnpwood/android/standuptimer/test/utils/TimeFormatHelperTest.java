package net.johnpwood.android.standuptimer.test.utils;

import junit.framework.TestCase;
import net.johnpwood.android.standuptimer.utils.TimeFormatHelper;
import android.graphics.Color;

public class TimeFormatHelperTest extends TestCase {

    public void test_times_are_displayed_in_the_proper_format() {
        assertEquals("0:00", TimeFormatHelper.formatTime(0));
        assertEquals("0:30", TimeFormatHelper.formatTime(30));
        assertEquals("0:59", TimeFormatHelper.formatTime(59));
        assertEquals("1:00", TimeFormatHelper.formatTime(60));
        assertEquals("1:01", TimeFormatHelper.formatTime(61));
        assertEquals("1:59", TimeFormatHelper.formatTime(119));
        assertEquals("2:00", TimeFormatHelper.formatTime(120));
    }

    public void test_times_are_displayed_in_the_proper_color() {
        assertEquals(Color.GREEN, TimeFormatHelper.determineColor(60, 15));
        assertEquals(Color.YELLOW, TimeFormatHelper.determineColor(15, 15));
        assertEquals(Color.YELLOW, TimeFormatHelper.determineColor(7, 15));
        assertEquals(Color.RED, TimeFormatHelper.determineColor(0, 15));
    }
}
