package net.johnpwood.android.standuptimer;

import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StandupTimer extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initializeGUIElements();
    }

    private void initializeGUIElements() {
        initializeNumberOfParticipantsPicker();
        initializeMeetingLengthSpinner();
    }

    private void initializeMeetingLengthSpinner() {
        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.meeting_lengths, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    private void initializeNumberOfParticipantsPicker() {
        Object o = findViewById(R.id.num_participants);
        Class c = o.getClass();
        try {
            Method m = c.getMethod("setRange", int.class, int.class);
            m.invoke(o, 0, 15);
        } catch (Exception e) {
            Log.e("Failed to set the range of the participants number picker: ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
