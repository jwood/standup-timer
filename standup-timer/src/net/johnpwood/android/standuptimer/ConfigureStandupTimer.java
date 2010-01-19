package net.johnpwood.android.standuptimer;

import net.johnpwood.android.standuptimer.model.Team;
import net.johnpwood.android.standuptimer.utils.Logger;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfigureStandupTimer extends Activity implements OnClickListener {
    private static final String MEETING_LENGTH_POS = "meetingLengthPos";
    private static final String NUMBER_OF_PARTICIPANTS = "numberOfParticipants";
    private static final String TEAM_NAMES_POS = "teamNamesPos";

    private int meetingLengthPos = 0;
    private int numParticipants = 0;
    private int teamNamesPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeGUIElements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about:
            Logger.d("Displaying the about box");
            displayAboutBox();
            return true;
        case R.id.help:
            Logger.d("Displaying the help dialog");
            displayHelpDialog();
            return true;
        case R.id.settings:
            Logger.d("Displaying the settings");
            displaySettings();
            return true;
        case R.id.teams:
            Logger.d("Displaying the team configuration");
            displayTeamConfiguration();
            return true;
        case R.id.quit:
            Logger.d("Quitting");
            finish();
            return true;
        default:
            Logger.e("Unknown menu item selected");
            return false;
        }
    }

    protected void displaySettings() {
        startActivity(new Intent(this, Prefs.class));
    }

    protected void displayAboutBox() {
        startActivity(new Intent(this, About.class));
    }

    protected void displayHelpDialog() {
        startActivity(new Intent(this, Help.class));
    }

    protected void displayTeamConfiguration() {
        startActivity(new Intent(this, TeamList.class));
    }

    public void onClick(View v) {
        Intent i = new Intent(this, StandupTimer.class);

        Spinner meetingLengthSpinner = (Spinner) findViewById(R.id.meeting_length);
        meetingLengthPos = meetingLengthSpinner.getSelectedItemPosition();
        i.putExtra("meetingLengthPos", meetingLengthPos);

        TextView t = (TextView) findViewById(R.id.num_participants);
        numParticipants = Integer.parseInt(t.getText().toString());
        i.putExtra("numParticipants", numParticipants);

        Spinner teamNameSpinner = (Spinner) findViewById(R.id.team_names);
        teamNamesPos = teamNameSpinner.getSelectedItemPosition();
        i.putExtra("teamName", (String) teamNameSpinner.getSelectedItem());

        if (numParticipants > 1 && numParticipants <= 20) {
            saveState();
            startTimer(i);
        } else {
            showInvalidNumberOfParticipantsDialog();
        }
    }

    protected void showInvalidNumberOfParticipantsDialog() {
        showDialog(0);
    }

    protected void startTimer(Intent i) {
        startActivity(i);
    }

    private void initializeGUIElements() {
        loadState();
        initializeNumberOfParticipants();
        initializeMeetingLengthSpinner();
        initializeTeamNamesSpinner();
        initializeStartButton();
    }

    private void initializeNumberOfParticipants() {
        TextView t = (TextView) findViewById(R.id.num_participants);
        t.setText(Integer.toString(numParticipants));
    }

    private void initializeMeetingLengthSpinner() {
        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.meeting_lengths,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setSelection(meetingLengthPos);
    }

    private void initializeTeamNamesSpinner() {
        Spinner s = (Spinner) findViewById(R.id.team_names);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Team.findAllTeamNames(this));
        adapter.add("");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setSelection(teamNamesPos);
    }

    private void initializeStartButton() {
        View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }

    private void saveState() {
        Logger.i("Saving state.  mettingLengthPos = " + meetingLengthPos +
                ", numParticipants = " + numParticipants +
                ", teamNamePos = " + teamNamesPos);
        SharedPreferences.Editor preferences = getPreferences(MODE_PRIVATE).edit();
        preferences.putInt(MEETING_LENGTH_POS, meetingLengthPos);
        preferences.putInt(NUMBER_OF_PARTICIPANTS, numParticipants);
        preferences.putInt(TEAM_NAMES_POS, teamNamesPos);
        preferences.commit();
    }

    protected void loadState() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        meetingLengthPos = preferences.getInt(MEETING_LENGTH_POS, 0);
        numParticipants = preferences.getInt(NUMBER_OF_PARTICIPANTS, 2);
        teamNamesPos = preferences.getInt(TEAM_NAMES_POS, 0);
        Logger.i("Retrieved state.  mettingLengthPos = " + meetingLengthPos +
                ", numParticipants = " + numParticipants +
                ", teamNamePos = " + teamNamesPos);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.valid_num_participants_warning)
            .setCancelable(true)
            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dismissDialog(0);
                }
            });
        return builder.create();
    }

    protected int getMeetingLengthPos() {
        return meetingLengthPos;
    }

    protected int getNumParticipants() {
        return numParticipants;
    }

    protected int getTeamNamesPos() {
        return teamNamesPos;
    }
}
