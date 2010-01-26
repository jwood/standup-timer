package net.johnpwood.android.standuptimer.test;

import java.util.GregorianCalendar;

import net.johnpwood.android.standuptimer.R;
import net.johnpwood.android.standuptimer.TeamDetails;
import net.johnpwood.android.standuptimer.dao.DAOFactory;
import net.johnpwood.android.standuptimer.dao.MeetingDAO;
import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.Team;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

public class TeamDetailsTest extends ActivityInstrumentationTestCase2<TeamDetails> {
    private TeamDetails a = null;
    private TeamDAO teamDao = null;
    private MeetingDAO meetingDao = null;
    private DAOFactory daoFactory = DAOFactory.getInstance();

    public TeamDetailsTest() {
        super("net.johnpwood.android.standuptimer", TeamDetails.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        daoFactory.setCacheDAOInstances(true);        
    }

    private void setUpWithoutTestData() {
        Intent intent = new Intent();
        intent.putExtra("teamName", "Test Team");
        setActivityIntent(intent);
        a = getActivity();

        daoFactory.setGlobalContext(new RenamingDelegatingContext(a, "test_"));
        teamDao = daoFactory.getTeamDAO(a);
        meetingDao = daoFactory.getMeetingDAO(a);
    }

    private void setUpWithTestData() {
        createTestData();
        setUpWithoutTestData();
    }

    @Override
    protected void tearDown() throws Exception {
        teamDao.deleteAll();
        meetingDao.deleteAll();
        super.tearDown();
    }

    @MediumTest
    public void test_should_show_no_stats_or_meetings_if_team_has_no_meetings() throws Throwable {
        setUpWithoutTestData();
        assertEquals(a.getString(R.string.no_meeting_stats), ((TextView) a.findViewById(R.id.meeting_team_name_label)).getText());

        runTestOnUiThread(new Runnable() {
           public void run() {
               TabHost tabHost = (TabHost) a.findViewById(android.R.id.tabhost);
               tabHost.setCurrentTabByTag("meetings_tab");
               ListView meetingListView = (ListView) tabHost.getCurrentView(); 
               assertTrue(meetingListView.getAdapter().isEmpty());
           }
        });
    }

    @MediumTest
    public void test_meetings_tab_should_list_the_teams_meetings() throws Throwable {
        setUpWithTestData();

        runTestOnUiThread(new Runnable() {
            public void run() {
                TabHost tabHost = (TabHost) a.findViewById(android.R.id.tabhost);
                tabHost.setCurrentTabByTag("meetings_tab");
                ListView meetingListView = (ListView) tabHost.getCurrentView(); 
                assertEquals(5, meetingListView.getAdapter().getCount());
            }
        });
    }

    @MediumTest
    public void test_stats_tab_should_show_stats_for_the_team_meetings() {
        setUpWithTestData();

        assertEquals("Test Team", ((TextView) a.findViewById(R.id.meeting_team_name)).getText());
        assertEquals("5", ((TextView) a.findViewById(R.id.number_of_meetings)).getText());
        assertEquals("5.4", ((TextView) a.findViewById(R.id.avg_number_of_participants)).getText());
        assertEquals("7:35", ((TextView) a.findViewById(R.id.avg_individual_status_length)).getText());
        assertEquals("8:00", ((TextView) a.findViewById(R.id.avg_meeting_length)).getText());
        assertEquals("0:22", ((TextView) a.findViewById(R.id.avg_quickest_status)).getText());
        assertEquals("1:35", ((TextView) a.findViewById(R.id.avg_longest_status)).getText());
    }

    @MediumTest
    public void test_can_delete_an_existing_team() {
        setUpWithTestData();

        sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_D);
        TouchUtils.clickView(this, a.getConfirmDeleteTeamDialog().getButton(AlertDialog.BUTTON_POSITIVE));
        assertNull(teamDao.findByName("Test Team"));
    }

    @MediumTest
    public void test_deleting_a_team_can_be_aborted() {
        setUpWithTestData();

        sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_D);
        TouchUtils.clickView(this, a.getConfirmDeleteTeamDialog().getButton(AlertDialog.BUTTON_NEGATIVE));
        assertNotNull(teamDao.findByName("Test Team"));
    }

    private void createTestData() {
        Context context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        TeamDAO teamFixturesDao = daoFactory.getTeamDAO(context);
        MeetingDAO meetingFixturesDao = daoFactory.getMeetingDAO(context);

        Team team = teamFixturesDao.save(new Team("Test Team"));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 301, 343, 30, 65));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 1, 6, 10, 17, 0).getTime(), 8, 534, 550, 32, 120));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 1, 7, 10, 16, 0).getTime(), 2, 234, 300, 23, 122));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 1, 8, 10, 14, 0).getTime(), 3, 765, 765, 15, 78));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 1, 9, 10, 12, 0).getTime(), 9, 444, 445, 10, 93));        
    }
}
