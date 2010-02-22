package net.johnpwood.android.standuptimer.mock;

import android.content.SharedPreferences;
import net.johnpwood.android.standuptimer.StandupTimer;
import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.Team;

public class StandupTimerMock extends StandupTimer {
    private boolean warningSoundPlayed = false;
    private boolean finishedSoundPlayed = false;
    private boolean persistMeetingCalled = false;

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
    public Team getTeam() {
        return super.getTeam();
    }

    @Override
    public void setTeam(Team team) {
        super.setTeam(team);
    }

    @Override
    public long getMeetingStartTime() {
        return super.getMeetingStartTime();
    }

    public void setMeetingStartTime(long value) {
        setPreference(MEETING_START_TIME, value);
    }

    @Override
    public long getIndividualStatusStartTime() {
        return super.getIndividualStatusStartTime();
    }

    @Override
    public long getIndividualStatusEndTime() {
        return super.getIndividualStatusEndTime();
    }

    public void setIndividualStatusEndTime(long value) {
        setPreference(INDIVIDUAL_STATUS_END_TIME, value);
    }

    @Override
    public int getQuickestStatus() {
        return super.getQuickestStatus();
    }

    public void setQuickestStatus(int value) {
        setPreference(QUICKEST_STATUS, value);
    }

    @Override
    public int getLongestStatus() {
        return super.getLongestStatus();
    }

    public void setLongestStatus(int value) {
        setPreference(LONGEST_STATUS, value);
    }

    @Override
    public int getCurrentIndividualStatusSeconds() {
        return super.getCurrentIndividualStatusSeconds();
    }

    public void setCurrentIndividualStatusSeconds(int value) {
        setPreference(CURRENT_INDIVIDUAL_STATUS_SECONDS, value);
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
    public void persistMeeting(Meeting meeting) {
        persistMeetingCalled = true;
    }

    public boolean wasPersistMeetingCalled() {
        return persistMeetingCalled;
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

    @Override
    public void disableIndividualTimer() {
        super.disableIndividualTimer();
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
        super.loadState(5, 2);
    }

    public void clearState() {
        getPreferences(MODE_PRIVATE).edit().clear().commit();
    }

    private void setPreference(String name, int value) {
        SharedPreferences.Editor preferences = getPreferences(MODE_PRIVATE).edit();
        preferences.putInt(name, value).commit();
        loadState();
    }

    private void setPreference(String name, long value) {
        SharedPreferences.Editor preferences = getPreferences(MODE_PRIVATE).edit();
        preferences.putLong(name, value).commit();
        loadState();
    }
}
