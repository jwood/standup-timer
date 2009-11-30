package net.johnpwood.android.standuptimer.test;

import net.johnpwood.android.standuptimer.StandupTimer;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class StandupTimerTest extends ActivityUnitTestCase<StandupTimer> {

    private Intent intent;

    public StandupTimerTest() {
        super(StandupTimer.class);
    }

    @Override
    public void setUp() {
        intent = new Intent();
    }

    @MediumTest
    public void testBlah() {
        assertTrue(true);
    }
}
