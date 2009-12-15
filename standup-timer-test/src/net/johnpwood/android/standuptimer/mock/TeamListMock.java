package net.johnpwood.android.standuptimer.mock;

import net.johnpwood.android.standuptimer.TeamList;
import net.johnpwood.android.standuptimer.dao.TeamDAO;
import android.app.Dialog;
import android.content.DialogInterface;
import android.test.RenamingDelegatingContext;
import android.view.View;

public class TeamListMock extends TeamList {
    private boolean displayAddTeamDialogCalled = false;
    private TeamDAO dao;

    @Override
    protected void displayAddTeamDialog() {
        displayAddTeamDialogCalled = true;
    }

    public boolean displayAddTeamDialogCalled() {
        return displayAddTeamDialogCalled;
    }

    @Override
    public void updateTeamList() {
        super.updateTeamList();
    }

    @Override
    public Dialog onCreateDialog(int id) {
        return super.onCreateDialog(0);
    }

    @Override
    public View getTextEntryView() {
        return super.getTextEntryView();
    }

    @Override
    public DialogInterface.OnClickListener addTeamButtonListener() {
        return super.addTeamButtonListener();
    }

    @Override
    public TeamDAO createTeamDAO() {
        if (dao == null) {
            dao = new TeamDAO(new RenamingDelegatingContext(this, "test_"));
        }
        return dao;
    }    
}
