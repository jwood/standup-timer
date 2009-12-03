package net.johnpwood.android.standuptimer.mock;

import android.content.SharedPreferences;
import net.johnpwood.android.standuptimer.StandupTimer;

public class StandupTimerMock extends StandupTimer {
    private boolean warningSoundPlayed = false;
    private boolean finishedSoundPlayed = false;

    @Override
    public int getRemainingIndividualSeconds() {
        return super.getRemainingIndividualSeconds();
    }

    public void setRemainingIndividualSeconds(int value) {
        setPreference(REMAINING_INDIVIDUAL_SECONDS, value);
    }

    @Override
    public int getRemainingMeetingSeconds() {
        return super.getRemainingMeetingSeconds();
    }

    public void setRemainingMeetingSeconds(int value) {
        setPreference(REMAINING_MEETING_SECONDS, value);
    }

    @Override
    public int getStartingIndividualSeconds() {
        return super.getStartingIndividualSeconds();
    }

    public void setStartingIndividualSeconds(int value) {
        setPreference(STARTING_INDIVIDUAL_SECONDS, value);
    }

    @Override
    public int getCompletedParticipants() {
        return super.getCompletedParticipants();
    }

    public void setCompletedParticipants(int value) {
        setPreference(COMPLETED_PARTICIPANTS, value);
    }

    @Override
    public int getTotalParticipants() {
        return super.getTotalParticipants();
    }

    public void setTotalParticipants(int value) {
        setPreference(TOTAL_PARTICIPANTS, value);
    }

    @Override
    public int getWarningTime() {
        return super.getWarningTime();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished();
    }

    @Override
    public void playWarningSound() {
        warningSoundPlayed = true;
    }

    public boolean wasWarningSoundPlayed() {
        return warningSoundPlayed;
    }

    @Override
    public void playFinishedSound() {
        finishedSoundPlayed = true;
    }

    public boolean wasFinishedSoundPlayed() {
        return finishedSoundPlayed;
    }

    @Override
    public void updateTimerValues() {
        super.updateTimerValues();
    }

    @Override
    public void updateDisplay() {
        super.updateDisplay();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean isTimerActive() {
        if (super.getTimer() != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isWakeLockAcquired() {
        if (super.getWakeLock() != null && super.getWakeLock().isHeld()) {
            return true;
        } else {
            return false;
        }
    }

    public void loadState() {
        super.loadState(0, 2);
    }

    private void setPreference(String name, int value) {
        SharedPreferences.Editor preferences = getPreferences(MODE_PRIVATE).edit();
        preferences.putInt(name, value).commit();
        loadState();
    }
}
