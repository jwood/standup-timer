package net.johnpwood.android.standuptimer;

import java.util.ArrayList;
import java.util.List;

import net.johnpwood.android.standuptimer.model.Meeting;
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
        if (team.hasMeetings(this)) {
            tabHost.addTab(tabHost.newTabSpec("stats_tab").
                    setIndicator(this.getString(R.string.stats)).
                    setContent(R.id.team_stats));
            tabHost.addTab(tabHost.newTabSpec("meetings_tab").
                    setIndicator(this.getString(R.string.meetings)).
                    setContent(createMeetingList()));
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

    private TabHost.TabContentFactory createMeetingList() {
        return new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                List<Meeting> meetings = team.findAllMeetings(TeamDetails.this);
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
}
