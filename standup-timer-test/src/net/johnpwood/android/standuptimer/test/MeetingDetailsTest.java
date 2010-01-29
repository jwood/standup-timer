package net.johnpwood.android.standuptimer.test;

import java.util.GregorianCalendar;

import net.johnpwood.android.standuptimer.MeetingDetails;
import net.johnpwood.android.standuptimer.R;
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
import android.widget.TextView;

public class MeetingDetailsTest extends ActivityInstrumentationTestCase2<MeetingDetails> {
    private MeetingDetails a = null;
    private MeetingDAO meetingDao = null;
    private TeamDAO teamDao = null;
    private Meeting meetingToDelete = null;
    private DAOFactory daoFactory = DAOFactory.getInstance();

    public MeetingDetailsTest() {
        super("net.johnpwood.android.standuptimer", MeetingDetails.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        daoFactory.setCacheDAOInstances(true);        
        createTestData();

        Intent intent = new Intent();
        intent.putExtra("teamName", meetingToDelete.getTeam().getName());
        intent.putExtra("meetingTime", meetingToDelete.getDescription());
        setActivityIntent(intent);
        a = getActivity();

        daoFactory.setGlobalContext(new RenamingDelegatingContext(a, "test_"));
        meetingDao = daoFactory.getMeetingDAO(a);
        teamDao = daoFactory.getTeamDAO(a);
    }

    @Override
    protected void tearDown() throws Exception {
        meetingDao.deleteAll();
        teamDao.deleteAll();
        super.tearDown();
    }

    @MediumTest
    public void test_stats_tab_should_show_stats_for_the_meeting() {
        assertEquals("Test Team", ((TextView) a.findViewById(R.id.meeting_details_team_name)).getText());
        assertEquals("01/07/2010 10:16:00am", ((TextView) a.findViewById(R.id.meeting_time)).getText());
        assertEquals("2", ((TextView) a.findViewById(R.id.number_of_participants)).getText());
        assertEquals("3:54", ((TextView) a.findViewById(R.id.individual_status_length)).getText());
        assertEquals("5:00", ((TextView) a.findViewById(R.id.meeting_length)).getText());
        assertEquals("0:23", ((TextView) a.findViewById(R.id.quickest_status)).getText());
        assertEquals("2:02", ((TextView) a.findViewById(R.id.longest_status)).getText());
    }

    @MediumTest
    public void test_can_delete_an_existing_meeting() {
        sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_D);
        TouchUtils.clickView(this, a.getConfirmDeleteMeetingDialog().getButton(AlertDialog.BUTTON_POSITIVE));
        assertNull(meetingDao.findById(meetingToDelete.getId()));
    }

    @MediumTest
    public void test_deleting_a_team_can_be_aborted() {
        sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_D);
        TouchUtils.clickView(this, a.getConfirmDeleteMeetingDialog().getButton(AlertDialog.BUTTON_NEGATIVE));
        assertNotNull(meetingDao.findById(meetingToDelete.getId()));
    }

    private void createTestData() {
        Context context = new RenamingDelegatingContext(getInstrumentation().getTargetContext(), "test_");
        TeamDAO teamFixturesDao = daoFactory.getTeamDAO(context);
        MeetingDAO meetingFixturesDao = daoFactory.getMeetingDAO(context);

        Team team = teamFixturesDao.save(new Team("Test Team"));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 0, 5, 10, 15, 0).getTime(), 5, 301, 343, 30, 65));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 0, 6, 10, 17, 0).getTime(), 8, 534, 550, 32, 120));
        meetingToDelete = meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 0, 7, 10, 16, 0).getTime(), 2, 234, 300, 23, 122));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 0, 8, 10, 14, 0).getTime(), 3, 765, 765, 15, 78));
        meetingFixturesDao.save(new Meeting(team, new GregorianCalendar(2010, 0, 9, 10, 12, 0).getTime(), 9, 444, 445, 10, 93));        
    }
}
