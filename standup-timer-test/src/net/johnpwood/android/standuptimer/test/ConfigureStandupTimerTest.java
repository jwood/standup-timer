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

        Prefs.setAllowUnlimitedParticipants(a, false);
        Prefs.setAllowVariableMeetingLength(a, false);
        a.onResume();
    }

    @MediumTest
    public void test_less_than_1_meeting_participants_displays_error_dialog() {
        TextView t = (TextView) a.findViewById(R.id.num_participants);
        t.setText("0");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertTrue(a.showInvalidNumberOfParticipantsDialogCalled());
        assertFalse(a.startTimerCalled());
    }

    @MediumTest
    public void test_greater_than_20_meeting_participants_displays_error_dialog_if_unlimited_participants_not_allowed() {
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
        Spinner s = a.getMeetingLengthSpinner();
        s.setSelection(2);
        Spinner s2 = (Spinner) a.findViewById(R.id.team_names);
        s2.setSelection(0);

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        a.loadState();
        assertEquals(13, a.getNumParticipants());
        assertEquals(15, a.getMeetingLength());
        assertEquals(0, a.getTeamNamesPos());
    }

    @MediumTest
    public void test_specifying_an_invalid_number_of_participants_shouldnt_crash_the_app() {
        Prefs.setAllowUnlimitedParticipants(a, true);
        TextView t = (TextView) a.findViewById(R.id.num_participants);
        t.setText("2198723498239487239487234987");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertFalse(a.showInvalidNumberOfParticipantsDialogCalled());
        assertTrue(a.startTimerCalled());
    }

    @MediumTest
    public void test_starting_a_meeting_with_a_non_traditional_meeting_length() {
        Prefs.setAllowVariableMeetingLength(a, true);
        a.onResume();

        TextView numParticipants = (TextView) a.findViewById(R.id.num_participants);
        numParticipants.setText("11");
        TextView meetingLength = a.getMeetingLengthEditText();
        meetingLength.setText("13");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertFalse(a.showInvalidNumberOfParticipantsDialogCalled());
        assertTrue(a.startTimerCalled());
        assertEquals(13, a.getIntent().getIntExtra("meetingLength", 0));
    }

    @MediumTest
    public void test_specifying_an_invalid_meeting_length_shouldnt_crash_the_app() {
        Prefs.setAllowVariableMeetingLength(a, true);
        a.onResume();

        TextView numParticipants = (TextView) a.findViewById(R.id.num_participants);
        numParticipants.setText("11");
        TextView meetingLength = a.getMeetingLengthEditText();
        meetingLength.setText("2198723498239487239487234987");

        Button b = (Button) a.findViewById(R.id.start_button);
        b.performClick();

        assertFalse(a.showInvalidNumberOfParticipantsDialogCalled());
        assertTrue(a.startTimerCalled());
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
}
