package net.johnpwood.android.standuptimer.test;

import net.johnpwood.android.standuptimer.Prefs;
import net.johnpwood.android.standuptimer.R;
import net.johnpwood.android.standuptimer.mock.StandupTimerMock;
import android.content.Intent;
import android.graphics.Color;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

public class StandupTimerTest extends ActivityUnitTestCase<StandupTimerMock> {

    private static final int DEFAULT_NUM_PARTICIPANTS = 2;
    private static final int DEFAULT_MEETING_LENGTH = 60 * 5;
    private StandupTimerMock a = null;

    public StandupTimerTest() {
        super(StandupTimerMock.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtra("meetingLengthPos", 1);
        intent.putExtra("numParticipants", 5);
        a = startActivity(intent, null, null);
    }

    @MediumTest
    public void test_onResume_acquires_wake_lock_and_starts_timer() {
        assertFalse(a.isWakeLockAcquired());
        assertFalse(a.isTimerActive());
        a.onResume();

        assertTrue(a.isWakeLockAcquired());
        assertTrue(a.isTimerActive());
    }

    @MediumTest
    public void test_onPause_cancels_timer_and_releases_wake_lock() {
        a.onResume();
        assertTrue(a.isWakeLockAcquired());
        assertTrue(a.isTimerActive());
        a.onPause();

        assertFalse(a.isWakeLockAcquired());
        assertFalse(a.isTimerActive());
    }

    @MediumTest
    public void test_onPause_saves_state_if_not_finished() {
        a.setRemainingMeetingSeconds(123);
        a.setTotalParticipants(456);
        a.onPause();

        a.loadState();
        assertEquals(123, a.getRemainingMeetingSeconds());
        assertEquals(456, a.getTotalParticipants());
    }

    @MediumTest
    public void test_onPause_clears_state_if_finished() {
        a.setRemainingMeetingSeconds(123);
        a.setTotalParticipants(456);
        clickFinishedButton();
        a.onPause();

        a.loadState();
        assertEquals(DEFAULT_MEETING_LENGTH, a.getRemainingMeetingSeconds());
        assertEquals(DEFAULT_NUM_PARTICIPANTS, a.getTotalParticipants());
    }

    @MediumTest
    public void test_timer_is_stopped_when_back_button_is_pressed() {
        a.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        a.onPause();

        a.loadState();
        assertEquals(DEFAULT_MEETING_LENGTH, a.getRemainingMeetingSeconds());
        assertEquals(DEFAULT_NUM_PARTICIPANTS, a.getTotalParticipants());
        assertTrue(a.isFinished());
        assertTrue(isFinishCalled());
    }

    @MediumTest
    public void test_click_next_button_advances_to_next_participant_and_resets_individual_timer() {
        a.setRemainingIndividualSeconds(4);
        assertEquals(0, a.getCompletedParticipants());
        clickNextButton();

        assertEquals(a.getStartingIndividualSeconds(), a.getRemainingIndividualSeconds());
        assertEquals(1, a.getCompletedParticipants());
    }

    @MediumTest
    public void test_click_next_button_assigns_remaining_time_to_individual_when_remaining_meeting_time_less_than_starting_individual_time() {
        a.setStartingIndividualSeconds(60);
        a.setRemainingMeetingSeconds(4);
        clickNextButton();

        assertEquals(4, a.getRemainingIndividualSeconds());
    }

    @MediumTest
    public void test_individual_timer_is_disabled_when_there_are_no_more_participants() {
        a.setCompletedParticipants(4);
        clickNextButton();

        assertEquals(0, a.getRemainingIndividualSeconds());

        TextView participantNumber = (TextView) a.findViewById(R.id.participant_number);
        assertEquals(a.getString(R.string.individual_status_complete), participantNumber.getText());

        TextView individualTimeRemaining = (TextView) a.findViewById(R.id.individual_time_remaining);
        assertEquals("0:00", individualTimeRemaining.getText());
        assertEquals(Color.GRAY, individualTimeRemaining.getTextColors().getDefaultColor());

        Button nextButton = (Button) a.findViewById(R.id.next_button);
        assertFalse(nextButton.isClickable());
        assertEquals(Color.GRAY, nextButton.getTextColors().getDefaultColor());
    }

    @MediumTest
    public void test_meeting_is_not_finished_when_all_participants_are_done() {
        a.setCompletedParticipants(4);
        clickNextButton();
        assertFalse(a.isFinished());
    }

    @MediumTest
    public void test_click_finish_button_ends_the_timer_and_clears_state() {
        clickFinishedButton();

        a.loadState();
        assertEquals(DEFAULT_MEETING_LENGTH, a.getRemainingMeetingSeconds());
        assertEquals(DEFAULT_NUM_PARTICIPANTS, a.getTotalParticipants());
        assertTrue(a.isFinished());
        assertTrue(isFinishCalled());
    }

    @MediumTest
    public void test_timer_is_changed_to_yellow_and_warning_sound_is_played_when_individual_timer_reaches_warning_time() {
        Prefs.setPlaySounds(a, true);
        Prefs.setWarningTime(a, 15);
        a.setRemainingIndividualSeconds(16);
        a.updateTimerValues();
        a.updateDisplay();

        TextView individualTimeRemaining = (TextView) a.findViewById(R.id.individual_time_remaining);
        assertEquals(Color.YELLOW, individualTimeRemaining.getTextColors().getDefaultColor());
        assertTrue(a.wasWarningSoundPlayed());
    }

    @MediumTest
    public void test_timer_is_changed_to_red_and_finished_sound_is_played_when_individual_timer_runs_out() {
        Prefs.setPlaySounds(a, true);
        a.setRemainingIndividualSeconds(1);
        a.updateTimerValues();
        a.updateDisplay();

        TextView individualTimeRemaining = (TextView) a.findViewById(R.id.individual_time_remaining);
        assertEquals(Color.RED, individualTimeRemaining.getTextColors().getDefaultColor());
        assertTrue(a.wasFinishedSoundPlayed());
    }

    @MediumTest
    public void test_warning_sound_is_not_played_if_sounds_are_disabled() {
        Prefs.setWarningTime(a, 15);
        Prefs.setPlaySounds(a, false);
        a.setRemainingIndividualSeconds(16);
        a.updateTimerValues();

        assertFalse(a.wasWarningSoundPlayed());
    }

    @MediumTest
    public void test_finished_sound_is_not_played_if_sounds_are_disabled() {
        Prefs.setPlaySounds(a, false);
        a.setRemainingIndividualSeconds(1);
        a.updateTimerValues();

        assertFalse(a.wasFinishedSoundPlayed());
    }

    private void clickFinishedButton() {
        ((Button) a.findViewById(R.id.finished_button)).performClick();
    }

    private void clickNextButton() {
        ((Button) a.findViewById(R.id.next_button)).performClick();
    }
}
