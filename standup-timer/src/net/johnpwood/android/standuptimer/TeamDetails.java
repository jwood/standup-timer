package net.johnpwood.android.standuptimer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.MeetingStats;
import net.johnpwood.android.standuptimer.model.Team;
import net.johnpwood.android.standuptimer.utils.Logger;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TeamDetails extends TabActivity {
    private static final int CONFIRM_DELETE_MEETING_DIALOG = 1;
    private static final int CONFIRM_DELETE_TEAM_DIALOG = 2;

    private Team team = null;
    private ListView meetingList = null;
    private Dialog confirmDeleteMeetingDialog = null;
    private Dialog confirmDeleteTeamDialog = null;
    private Integer positionOfMeetingToDelete = null;

    private Handler updateMeetingListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateMeetingList();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        team = Team.findByName(getIntent().getStringExtra("teamName"), this);
        meetingList = new ListView(this);
        registerForContextMenu(meetingList);

        TabHost tabHost = getTabHost();
        if (team.hasMeetings(this)) {
            tabHost.addTab(tabHost.newTabSpec("stats_tab").
                    setIndicator(this.getString(R.string.stats)).
                    setContent(createMeetingDetails(team)));

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
        getTabHost().setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.team_details_options_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meetings_context_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.delete_team_from_details:
            Logger.d("Displaying the delete team dialog box");
            showDialog(CONFIRM_DELETE_TEAM_DIALOG);
            return true;
        default:
            Logger.e("Unknown menu item selected");
            return false;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
        case R.id.delete_meeting:
            positionOfMeetingToDelete = info.position;
            showDialog(CONFIRM_DELETE_MEETING_DIALOG);
            return true;
        default:
            return super.onContextItemSelected(item);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case CONFIRM_DELETE_MEETING_DIALOG:
            if (confirmDeleteMeetingDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this meeting?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", deleteMeetingConfirmationListener());
                builder.setNegativeButton("No", cancelListener());
                confirmDeleteMeetingDialog = builder.create();
            }
            return confirmDeleteMeetingDialog;

        case CONFIRM_DELETE_TEAM_DIALOG:
            if (confirmDeleteTeamDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to delete this team?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", deleteTeamConfirmationListener());
                builder.setNegativeButton("No", cancelListener());
                confirmDeleteTeamDialog = builder.create();
            }
            return confirmDeleteTeamDialog;

        default:
            Logger.e("Attempting to create an unkonwn dialog with an id of " + id);
            return null;
        }
    }

    private TabHost.TabContentFactory createMeetingList() {
        return new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                meetingList.setAdapter(createMeetingListAdapter());
                return meetingList;
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

    protected DialogInterface.OnClickListener deleteMeetingConfirmationListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteMeeting(positionOfMeetingToDelete);
            }
        };
    }

    protected DialogInterface.OnClickListener deleteTeamConfirmationListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                team.delete(TeamDetails.this);
                finish();
            }
        };
    }

    protected DialogInterface.OnClickListener cancelListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
    }

    private void deleteMeeting(int position) {
        String dateString = (String) meetingList.getAdapter().getItem(position);

        try {
            Date date = new SimpleDateFormat(Meeting.DESCRIPTION_FORMAT).parse(dateString);
            Meeting meeting = Meeting.findByTeamAndDate(team, date, this);
            meeting.delete(this);
        } catch (ParseException e) {
            Logger.e(e.getMessage());
            Logger.e("Could not parse the date string '" + dateString + "'.  Will not attempt to delete the meeting.");
        }

        updateMeetingListHandler.sendEmptyMessage(0);
    }

    protected void updateMeetingList() {
        //TODO: Figure out how to update the tab contents
    }

    private ListAdapter createMeetingListAdapter() {
        List<String> meetingDescriptions = new ArrayList<String>();
        List<Meeting> meetings = team.findAllMeetings(TeamDetails.this);
        for (Meeting meeting : meetings) {
            meetingDescriptions.add(meeting.getDescription());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeamDetails.this,
                android.R.layout.simple_list_item_1, meetingDescriptions);
        return adapter;
    }
}
