package net.johnpwood.android.standuptimer;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// TODO: Play sounds when time is individual time is running out, and when time runs out.
// TODO: Go back to home screen when Finish button is clicked
// TODO: Clean up the views

public class StandupTimer extends Activity implements OnClickListener {
    private int remainingIndividualSeconds = 0;
    private int remainingMeetingSeconds = 0;
    private int startingIndividualSeconds = 0;
    private int completedParticipants = 0;
    private int totalParticipants = 0;

    private Handler updateDisplayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateDisplay();
        }
    };

    private Handler disableIndividualTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            disableIndividualTimer();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        setupButtonListeners();
        initializeTimerValues();
        updateDisplay();
        startTimer();
    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.next_button:
            processNextButtonClick();
            break;

        case R.id.finished_button:
            processFinishedButtonClick();
            break;
        }
    }

    private void setupButtonListeners() {
        View nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        View finishedButton = findViewById(R.id.finished_button);
        finishedButton.setOnClickListener(this);
    }

    private void initializeTimerValues() {
        int meetingLengthPos = getIntent().getIntExtra("meetingLengthPos", 0);
        totalParticipants = getIntent().getIntExtra("numParticipants", 0);

        Logger.d("Timer: meetingLengthPos = " + meetingLengthPos);
        Logger.d("Timer: numParticipants = " + totalParticipants);

        MeetingLength meetingLength = MeetingLength.findByPosition(meetingLengthPos);
        remainingMeetingSeconds = meetingLength.getLength() * 60;
        startingIndividualSeconds = remainingMeetingSeconds / totalParticipants;
        remainingIndividualSeconds = startingIndividualSeconds;
    }

    private synchronized void updateDisplay() {
        if (individualStatusInProgress()) {
            TextView individualTimeRemaining = (TextView) findViewById(R.id.individual_time_remaining);
            individualTimeRemaining.setText(formatTime(remainingIndividualSeconds));
            individualTimeRemaining.setTextColor(determineColor(remainingIndividualSeconds));

            TextView participantNumber = (TextView) findViewById(R.id.participant_number);
            // TODO: Change to pull from resource file
            participantNumber.setText("Participant" + " " + 
                    (completedParticipants + 1) + "/" + totalParticipants);
        }

        TextView totalTimeRemaining = (TextView) findViewById(R.id.total_time_remaining);
        totalTimeRemaining.setText(formatTime(remainingMeetingSeconds));
        totalTimeRemaining.setTextColor(determineColor(remainingMeetingSeconds));
    }

    private void startTimer() {
        Timer timer = new Timer();
        TimerTask updateTimerValuesTask = new TimerTask() {
            @Override
            public void run() {
                updateTimerValues();
            }
        };
        timer.schedule(updateTimerValuesTask, 1000, 1000);
    }

    private void updateTimerValues() {
        synchronized(this) {
            if (remainingIndividualSeconds > 0)
                remainingIndividualSeconds--;

            if (remainingMeetingSeconds > 0)
                remainingMeetingSeconds--;
        }

        updateDisplayHandler.sendEmptyMessage(0);
    }

    private synchronized void processNextButtonClick() {
        completedParticipants++;

        if (completedParticipants == totalParticipants) {
            disableIndividualTimerHandler.sendEmptyMessage(0);
        } else {
            if (startingIndividualSeconds < remainingMeetingSeconds) {
                remainingIndividualSeconds = startingIndividualSeconds;
            } else {
                remainingIndividualSeconds = remainingMeetingSeconds;
            }
            updateDisplay();
        }
    }

    private synchronized void disableIndividualTimer() {
        remainingIndividualSeconds = 0;

        TextView participantNumber = (TextView) findViewById(R.id.participant_number);
        participantNumber.setText(R.string.individual_status_complete);

        TextView individualTimeRemaining = (TextView) findViewById(R.id.individual_time_remaining);
        individualTimeRemaining.setText(formatTime(remainingIndividualSeconds));
        individualTimeRemaining.setTextColor(Color.GRAY);

        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setClickable(false);
        nextButton.setTextColor(Color.GRAY);
    }

    private synchronized boolean individualStatusInProgress() {
        return completedParticipants < totalParticipants;
    }

    private void processFinishedButtonClick() {
        finish();
    }

    private String formatTime(int seconds) {
        return "" + seconds / 60 + ":" + padWithZeros(seconds % 60);
    }

    private String padWithZeros(int seconds) {
        return seconds < 10 ? "0" + seconds : "" + seconds;
    }

    private int determineColor(int seconds) {
        if (seconds == 0) {
            return Color.RED;
        } else if (seconds <= 15) {
            return Color.YELLOW;
        } else {
            return Color.GREEN;
        }
    }
}
