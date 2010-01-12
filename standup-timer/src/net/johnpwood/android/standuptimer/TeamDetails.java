package net.johnpwood.android.standuptimer;

import java.util.ArrayList;
import java.util.List;

import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.MeetingStats;
import net.johnpwood.android.standuptimer.model.Team;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class TeamDetails extends TabActivity {

    private Team team = null;
    private ListView teamList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        team = Team.findByName(getIntent().getStringExtra("teamName"), this);
        teamList = new ListView(this);

        TabHost tabHost = getTabHost();
        List<Meeting> meetings = team.findAllMeetings(TeamDetails.this);
        if (meetings.size() > 0) {
            tabHost.addTab(tabHost.newTabSpec("stats_tab").
                    setIndicator(this.getString(R.string.stats)).
                    setContent(createMeetingDetails(team)));

            tabHost.addTab(tabHost.newTabSpec("meetings_tab").
                    setIndicator(this.getString(R.string.meetings)).
                    setContent(createMeetingList(meetings)));
        } else {
            ((TextView) this.findViewById(R.id.no_team_meeting_stats)).setText(getString(R.string.no_meeting_stats));
            tabHost.addTab(tabHost.newTabSpec("stats_tab").
                    setIndicator(this.getString(R.string.stats)).
                    setContent(R.id.no_team_meeting_stats));

            ((TextView) this.findViewById(R.id.no_team_meetings)).setText(getString(R.string.no_meetings));
            tabHost.addTab(tabHost.newTabSpec("meetings_tab").
                    setIndicator(this.getString(R.string.meetings)).
                    setContent(R.id.no_team_meetings));
        }
        tabHost.setCurrentTab(0);
    }

    private TabHost.TabContentFactory createMeetingList(final List<Meeting> meetings) {
        return new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                List<String> meetingDescriptions = new ArrayList<String>();
                for (Meeting meeting : meetings) {
                    meetingDescriptions.add(meeting.getDescription());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamDetails.this,
                        android.R.layout.simple_list_item_1, meetingDescriptions);
                teamList.setAdapter(adapter);
                return teamList;
            }
        };
    }

    private TabHost.TabContentFactory createMeetingDetails(final Team team) {
        return new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                MeetingStats stats = team.getAverageMeetingStats(TeamDetails.this);

                ((TextView) findViewById(R.id.meeting_team_name_label)).setText(getString(R.string.team_name));
                ((TextView) findViewById(R.id.meeting_team_name)).setText(team.getName());

                ((TextView) findViewById(R.id.avg_number_of_participants_label)).setText(getString(R.string.avg_number_of_participants));
                ((TextView) findViewById(R.id.avg_number_of_participants)).setText(Float.toString(stats.getNumParticipants()));

                ((TextView) findViewById(R.id.avg_meeting_length_label)).setText(getString(R.string.avg_meeting_length));
                ((TextView) findViewById(R.id.avg_meeting_length)).setText(Float.toString(stats.getMeetingLength()));

                ((TextView) findViewById(R.id.avg_individual_status_length_label)).setText(getString(R.string.avg_individual_status_length));
                ((TextView) findViewById(R.id.avg_individual_status_length)).setText(Float.toString(stats.getIndividualStatusLength()));

                ((TextView) findViewById(R.id.avg_quickest_status_label)).setText(getString(R.string.avg_quickest_status));
                ((TextView) findViewById(R.id.avg_quickest_status)).setText(Float.toString(stats.getQuickestStatus()));

                ((TextView) findViewById(R.id.avg_longest_status_label)).setText(getString(R.string.avg_longest_status));
                ((TextView) findViewById(R.id.avg_longest_status)).setText(Float.toString(stats.getLongestStatus()));

                return findViewById(R.id.team_stats);
            }
        };
    }
}
