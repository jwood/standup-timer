package net.johnpwood.android.standuptimer.mock;

import net.johnpwood.android.standuptimer.ConfigureStandupTimer;
import android.content.Intent;

public class ConfigureStandupTimerMock extends ConfigureStandupTimer {
    private boolean showInvalidNumberOfParticipantsDialogCalled = false;
    private boolean startTimerCalled = false;
    private boolean displaySettingsCalled = false;
    private boolean displayAboutBoxCalled = false;

    @Override
    protected void showInvalidNumberOfParticipantsDialog() {
        showInvalidNumberOfParticipantsDialogCalled = true;
    }

    public boolean showInvalidNumberOfParticipantsDialogCalled() {
        return showInvalidNumberOfParticipantsDialogCalled;
    }

    @Override
    protected void startTimer(Intent i) {
        startTimerCalled = true;
    }

    public boolean startTimerCalled() {
        return startTimerCalled;
    }

    @Override
    protected void displaySettings() {
        displaySettingsCalled = true;
    }

    public boolean displaySettingsCalled() {
        return displaySettingsCalled;
    }

    @Override
    protected void displayAboutBox() {
        displayAboutBoxCalled = true;
    }

    public boolean displayAboutBoxCalled() {
        return displayAboutBoxCalled;
    }

    @Override
    public void loadState() {
        super.loadState();
    }

    @Override
    public int getMeetingLengthPos() {
        return super.getMeetingLengthPos();
    }

    @Override
    public int getNumParticipants() {
        return super.getNumParticipants();
    }
}
