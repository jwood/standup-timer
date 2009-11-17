package net.johnpwood.android.standuptimer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

public class Timer extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        Spinner s = (Spinner) findViewById(R.id.meeting_length);
        Log.i("BLAH", getIntent().getStringExtra("meetingLength"));
        Log.i("BLAH", "" + getIntent().getIntExtra("numParticipants", 0));
    }
}
