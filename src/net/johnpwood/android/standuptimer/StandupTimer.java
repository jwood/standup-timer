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
// TODO: Grey out individual clock and disable Next button when next is clicked and no more participants
// TODO: Show the status of participants (1 of 5, 2 of 5, etc)
// TODO: Go back to home screen when Finish button is clicked
// TODO: Clean up the views

public class StandupTimer extends Activity implements OnClickListener {
    private int remainingIndividualSeconds = 0;
    private int remainingMeetingSeconds = 0;
    private int startingIndividualSeconds = 0;
    private int remainingParticipants = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateDisplay();
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
        int numParticipants = getIntent().getIntExtra("numParticipants", 0);

        Logger.d("Timer: meetingLengthPos = " + meetingLengthPos);
        Logger.d("Timer: numParticipants = " + numParticipants);

        MeetingLength meetingLength = MeetingLength.findByPosition(meetingLengthPos);
        remainingMeetingSeconds = meetingLength.getLength() * 60;
        startingIndividualSeconds = remainingMeetingSeconds / numParticipants;
        remainingIndividualSeconds = startingIndividualSeconds;
        remainingParticipants = numParticipants;
    }

    private void updateDisplay() {
        TextView individualTimeRemaining = (TextView) findViewById(R.id.individual_time_remaining);
        individualTimeRemaining.setText(formatTime(remainingIndividualSeconds));
        individualTimeRemaining.setTextColor(determineColor(remainingIndividualSeconds));

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

        handler.sendEmptyMessage(0);
    }

    private void processNextButtonClick() {
        remainingParticipants--;
        if (remainingParticipants == 0) {
            disableIndividualClock();
        } else {
            synchronized(this) {
                if (startingIndividualSeconds < remainingMeetingSeconds) {
                    remainingIndividualSeconds = startingIndividualSeconds;
                } else {
                    remainingIndividualSeconds = remainingMeetingSeconds;
                }
            }
            updateDisplay();
        }
    }

    private void disableIndividualClock() {
        Button nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setClickable(false);
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
