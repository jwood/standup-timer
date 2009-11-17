package net.johnpwood.android.standuptimer;

import android.app.Activity;
import android.os.Bundle;

public class Timer extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        int meetingLengthPos = getIntent().getIntExtra("meetingLengthPos", 0);
        int numParticipants = getIntent().getIntExtra("numParticipants", 0);

        Logger.d("Timer: meetingLengthPos = " + meetingLengthPos);
        Logger.d("Timer: numParticipants = " + numParticipants);

        MeetingLength meetingLength = MeetingLength.findByPosition(meetingLengthPos);
    }
}
