package net.johnpwood.android.standuptimer.test;

import net.johnpwood.android.standuptimer.Prefs;
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
        a.onResume();
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
    public void test_greater_than_20_meeting_participants_displays_error_dialog_if_unlimited_participants_not_allowed() {
        Prefs.setAllowUnlimitedParticipants(a, false);
        TextView t = (TextView) a.findViewById(R.id.num_participants);
        t.setText("21");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertTrue(a.showInvalidNumberOfParticipantsDialogCalled());
        assertFalse(a.startTimerCalled());
    }

    @MediumTest
    public void test_greater_than_20_meeting_participants_succeeds_if_unlimited_participants_is_allowed() {
        Prefs.setAllowUnlimitedParticipants(a, true);
        TextView t = (TextView) a.findViewById(R.id.num_participants);
        t.setText("21");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertFalse(a.showInvalidNumberOfParticipantsDialogCalled());
        assertTrue(a.startTimerCalled());
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
        Spinner s2 = (Spinner) a.findViewById(R.id.team_names);
        s2.setSelection(1);

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        a.loadState();
        assertEquals(13, a.getNumParticipants());
        assertEquals(2, a.getMeetingLengthPos());
        assertEquals(1, a.getTeamNamesPos());
    }

    @MediumTest
    public void test_about_box_displayed_successfully() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.about);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertTrue(a.displayAboutBoxCalled());
        assertFalse(a.displayHelpDialogCalled());
        assertFalse(a.displaySettingsCalled());
        assertFalse(isFinishCalled());
        EasyMock.verify(menuItem);
    }

    @MediumTest
    public void test_help_dialog_displayed_successfully() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.help);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertFalse(a.displayAboutBoxCalled());
        assertTrue(a.displayHelpDialogCalled());
        assertFalse(a.displaySettingsCalled());
        assertFalse(isFinishCalled());
        assertFalse(a.displayTeamConfigurationCalled());
        EasyMock.verify(menuItem);
    }

    @MediumTest
    public void test_settings_displayed_successfully() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.settings);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertFalse(a.displayAboutBoxCalled());
        assertFalse(a.displayHelpDialogCalled());
        assertTrue(a.displaySettingsCalled());
        assertFalse(isFinishCalled());
        assertFalse(a.displayTeamConfigurationCalled());
        EasyMock.verify(menuItem);
    }

    @MediumTest
    public void test_team_configuration_displayed_successfully() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.teams);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertFalse(a.displayAboutBoxCalled());
        assertFalse(a.displaySettingsCalled());
        assertFalse(isFinishCalled());
        assertTrue(a.displayTeamConfigurationCalled());
        EasyMock.verify(menuItem);
    }

    @MediumTest
    public void test_quit_menu_item_quits_the_application() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.quit);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertFalse(a.displayAboutBoxCalled());
        assertFalse(a.displayHelpDialogCalled());
        assertFalse(a.displaySettingsCalled());
        assertTrue(isFinishCalled());
        assertFalse(a.displayTeamConfigurationCalled());
        EasyMock.verify(menuItem);
    }
}
