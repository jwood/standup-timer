package net.johnpwood.android.standuptimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.MeetingStats;
import net.johnpwood.android.standuptimer.model.Team;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MeetingDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_details);
    
        String teamName = getIntent().getStringExtra("teamName");
        String meetingTime = getIntent().getStringExtra("meetingTime");

        Team team = Team.findByName(teamName, this);
        Date date = null;
        try {
            date = new SimpleDateFormat(Meeting.DESCRIPTION_FORMAT).parse(meetingTime);
        } catch (ParseException e) {
            throw new RuntimeException();
        }

        Meeting meeting = Meeting.findByTeamAndDate(team, date, this);
        MeetingStats stats = meeting.getMeetingStats();

        ((TextView) findViewById(R.id.meeting_details_team_name_label)).setText(getString(R.string.team_name));
        ((TextView) findViewById(R.id.meeting_details_team_name)).setText(team.getName());

        ((TextView) findViewById(R.id.number_of_participants_label)).setText(getString(R.string.number_of_participants));
        ((TextView) findViewById(R.id.number_of_participants)).setText(Integer.toString((int) stats.getNumParticipants()));

        ((TextView) findViewById(R.id.meeting_length_label)).setText(getString(R.string.meeting_length));
        ((TextView) findViewById(R.id.meeting_length)).setText(Integer.toString((int) stats.getMeetingLength()));

        ((TextView) findViewById(R.id.individual_status_length_label)).setText(getString(R.string.individual_status_length));
        ((TextView) findViewById(R.id.individual_status_length)).setText(Integer.toString((int) stats.getIndividualStatusLength()));

        ((TextView) findViewById(R.id.quickest_status_label)).setText(getString(R.string.quickest_status));
        ((TextView) findViewById(R.id.quickest_status)).setText(Integer.toString((int) stats.getQuickestStatus()));

        ((TextView) findViewById(R.id.longest_status_label)).setText(getString(R.string.longest_status));
        ((TextView) findViewById(R.id.longest_status)).setText(Integer.toString((int) stats.getLongestStatus()));
    }
}
