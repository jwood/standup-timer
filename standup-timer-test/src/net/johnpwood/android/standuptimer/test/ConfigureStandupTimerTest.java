package net.johnpwood.android.standuptimer.test;

import net.johnpwood.android.standuptimer.R;
import net.johnpwood.android.standuptimer.mock.ConfigureStandupTimerMock;

import org.easymock.EasyMock;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfigureStandupTimerTest extends ActivityUnitTestCase<ConfigureStandupTimerMock> {
    private ConfigureStandupTimerMock a = null;

    public ConfigureStandupTimerTest() {
        super(ConfigureStandupTimerMock.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        a = startActivity(intent, null, null);
    }

    @MediumTest
    public void test_less_than_2_meeting_participants_displays_error_dialog() {
        TextView t = (TextView) a.findViewById(R.id.num_participants);
        t.setText("1");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertTrue(a.showInvalidNumberOfParticipantsDialogCalled());
        assertFalse(a.startTimerCalled());
    }

    @MediumTest
    public void test_greater_than_20_meeting_participants_displays_error_dialog() {
        TextView t = (TextView) a.findViewById(R.id.num_participants);
        t.setText("21");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertTrue(a.showInvalidNumberOfParticipantsDialogCalled());
        assertFalse(a.startTimerCalled());
    }

    @MediumTest
    public void test_valud_number_of_meeting_participants_starts_the_timer() {
        TextView t = (TextView) a.findViewById(R.id.num_participants);
        t.setText("11");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertFalse(a.showInvalidNumberOfParticipantsDialogCalled());
        assertTrue(a.startTimerCalled());
    }

    @MediumTest
    public void test_state_is_saved_when_timer_is_started() {
        TextView t = (TextView) a.findViewById(R.id.num_participants);
        t.setText("13");
        Spinner s = (Spinner) a.findViewById(R.id.meeting_length);
        s.setSelection(2);

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        a.loadState();
        assertEquals(13, a.getNumParticipants());
        assertEquals(2, a.getMeetingLengthPos());
    }

    @MediumTest
    public void test_about_box_displayed_successfully() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.about);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertTrue(a.displayAboutBoxCalled());
        assertFalse(a.displaySettingsCalled());
        assertFalse(isFinishCalled());
        EasyMock.verify(menuItem);
    }

    @MediumTest
    public void test_settings_displayed_successfully() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.settings);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertFalse(a.displayAboutBoxCalled());
        assertTrue(a.displaySettingsCalled());
        assertFalse(isFinishCalled());
        EasyMock.verify(menuItem);
    }

    @MediumTest
    public void test_quit_menu_item_quits_the_application() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.quit);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertFalse(a.displayAboutBoxCalled());
        assertFalse(a.displaySettingsCalled());
        assertTrue(isFinishCalled());
        EasyMock.verify(menuItem);
    }
}
