package net.johnpwood.android.standuptimer;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConfigureStandupTimer extends Activity implements OnClickListener {
    private static final String MEETING_LENGTH_POS = "meetingLengthPos";
    private static final String NUMBER_OF_PARTICIPANTS = "numberOfParticipants";

    private int meetingLengthPos = 0;
    private int numParticipants = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeGUIElements();
    }

    public void onClick(View v) {
        Intent i = new Intent(this, StandupTimer.class);

        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        meetingLengthPos = s.getSelectedItemPosition();
        i.putExtra("meetingLengthPos", meetingLengthPos);

        Object o = findViewById(R.id.num_participants);
        Class<? extends Object> c = o.getClass();
        try {
            Method m = c.getMethod("getCurrent");
            numParticipants = (Integer) m.invoke(o, new Object[]{});
        } catch (Exception e) {
            Logger.e("Failed to get the number of partipipants: " + e.getMessage());
            Logger.e("Quitting");
            throw new RuntimeException(e);
        }
        i.putExtra("numParticipants", numParticipants);

        saveState();
        startActivity(i);
    }

    private void initializeGUIElements() {
        loadState();
        initializeNumberOfParticipantsPicker();
        initializeMeetingLengthSpinner();
        initializeStartButton();
    }

    private void initializeStartButton() {
        View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }

    private void initializeMeetingLengthSpinner() {
        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.meeting_lengths,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setSelection(meetingLengthPos);
    }

    private void initializeNumberOfParticipantsPicker() {
        Object o = findViewById(R.id.num_participants);
        Class<? extends Object> c = o.getClass();
        try {
            Method m = c.getMethod("setRange", int.class, int.class);
            m.invoke(o, 2, 20);

            m = c.getMethod("setCurrent", int.class);
            m.invoke(o, numParticipants);
        } catch (Exception e) {
            Logger.e("Failed to set the range of the participants number picker: " + e.getMessage());
            Logger.e("Quitting");
            throw new RuntimeException(e);
        }
    }

    private void saveState() {
        Logger.i("Saving state.  mettingLengthPos = " + meetingLengthPos + ", numParticipants = " + numParticipants);
        SharedPreferences.Editor preferences = getPreferences(MODE_PRIVATE).edit();
        preferences.putInt(MEETING_LENGTH_POS, meetingLengthPos);
        preferences.putInt(NUMBER_OF_PARTICIPANTS, numParticipants);
        preferences.commit();
    }

    private void loadState() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        meetingLengthPos = preferences.getInt(MEETING_LENGTH_POS, 0);
        numParticipants = preferences.getInt(NUMBER_OF_PARTICIPANTS, 2);
        Logger.i("Retrieved state.  mettingLengthPos = " + meetingLengthPos + ", numParticipants = " + numParticipants);
    }
}
