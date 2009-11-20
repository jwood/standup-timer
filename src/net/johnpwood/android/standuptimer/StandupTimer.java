package net.johnpwood.android.standuptimer;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StandupTimer extends Activity implements OnClickListener {
    private final String REMAINING_INDIVIDUAL_SECONDS = "remainingIndividualSeconds";
    private final String REMAINING_MEETING_SECONDS = "remainingMeetingSeconds";
    private final String STARTING_INDIVIDUAL_SECONDS = "startingIndividualSeconds";
    private final String COMPLETED_PARTICIPANTS = "completedParticipants";
    private final String TOTAL_PARTICIPANTS = "totalParticipants";

    private int remainingIndividualSeconds = 0;
    private int remainingMeetingSeconds = 0;
    private int startingIndividualSeconds = 0;
    private int completedParticipants = 0;
    private int totalParticipants = 0;
    private boolean finished = false;

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

    @Override
    protected void onPause() {
        super.onPause();

        if (finished) {
            clearState();
        } else {
            saveState();
        }
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
        int numParticipants = getIntent().getIntExtra("numParticipants", 0);

        Logger.d("Timer: meetingLengthPos = " + meetingLengthPos);
        Logger.d("Timer: numParticipants = " + totalParticipants);

        loadState(meetingLengthPos, numParticipants);
    }

    private synchronized void updateDisplay() {
        if (individualStatusInProgress()) {
            TextView individualTimeRemaining = (TextView) findViewById(R.id.individual_time_remaining);
            individualTimeRemaining.setText(formatTime(remainingIndividualSeconds));
            individualTimeRemaining.setTextColor(determineColor(remainingIndividualSeconds));

            TextView participantNumber = (TextView) findViewById(R.id.participant_number);
            participantNumber.setText(this.getString(R.string.participant) + " " +
                    (completedParticipants + 1) + "/" + totalParticipants);
        } else {
            disableIndividualTimer();
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

    private void loadState(int meetingLengthPos, int numParticipants) {
        MeetingLength meetingLength = MeetingLength.findByPosition(meetingLengthPos);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        totalParticipants = preferences.getInt(TOTAL_PARTICIPANTS, numParticipants);
        remainingMeetingSeconds = preferences.getInt(REMAINING_MEETING_SECONDS, meetingLength.getLength() * 60);
        startingIndividualSeconds = preferences.getInt(STARTING_INDIVIDUAL_SECONDS, remainingMeetingSeconds / totalParticipants);
        remainingIndividualSeconds = preferences.getInt(REMAINING_INDIVIDUAL_SECONDS, startingIndividualSeconds);
        completedParticipants = preferences.getInt(COMPLETED_PARTICIPANTS, 0);
    }

    private void saveState() {
        SharedPreferences.Editor preferences = getPreferences(MODE_PRIVATE).edit();
        preferences.putInt(REMAINING_INDIVIDUAL_SECONDS, remainingIndividualSeconds);
        preferences.putInt(REMAINING_MEETING_SECONDS, remainingMeetingSeconds);
        preferences.putInt(STARTING_INDIVIDUAL_SECONDS, startingIndividualSeconds);
        preferences.putInt(COMPLETED_PARTICIPANTS, completedParticipants);
        preferences.putInt(TOTAL_PARTICIPANTS, totalParticipants);
        preferences.commit();
    }

    private void clearState() {
        getPreferences(MODE_PRIVATE).edit().clear().commit();
    }

    private synchronized boolean individualStatusInProgress() {
        return completedParticipants < totalParticipants;
    }

    private synchronized void processFinishedButtonClick() {
        finished = true;
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
