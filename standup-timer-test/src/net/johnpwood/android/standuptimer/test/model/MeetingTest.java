package net.johnpwood.android.standuptimer.test.model;

import java.util.GregorianCalendar;
import java.util.List;

import net.johnpwood.android.standuptimer.dao.DAOFactory;
import net.johnpwood.android.standuptimer.dao.DatabaseConstants;
import net.johnpwood.android.standuptimer.dao.MeetingDAO;
import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.Team;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;

public class MeetingTest extends AndroidTestCase implements DatabaseConstants {
    private MeetingDAO dao = null;
    private DAOFactory daoFactory = DAOFactory.getInstance();

    @Override
    protected void setUp() {
        daoFactory.setGlobalContext(new RenamingDelegatingContext(mContext, "test_"));
        daoFactory.setCacheDAOInstances(true);
        dao = daoFactory.getMeetingDAO(mContext);
    }

    @Override
    protected void tearDown() {
        dao.deleteAll();
        dao.close();
    }

    @MediumTest
    public void test_save_a_meeting() {
        Meeting meeting = new Meeting(new Team("Test Team"), new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120);
        meeting = meeting.save(mContext);
        assertNotNull(meeting.getId());
    }

    @MediumTest
    public void test_delete_a_meeting() {
        Team team = new Team("Test Team");

        Meeting meeting = new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120);
        meeting = meeting.save(mContext);
        assertEquals(1, Meeting.findAllByTeam(team, mContext).size());

        meeting.delete(mContext);
        assertEquals(0, Meeting.findAllByTeam(team, mContext).size());
    }

    @MediumTest
    public void test_find_all_meetings_by_team() {
        Team team = new Team("Test Team");
        new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);
        new Meeting(team, new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;
        new Meeting(team, new GregorianCalendar(2010, 1, 7, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;
        new Meeting(team, new GregorianCalendar(2010, 1, 1, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;
        new Meeting(team, new GregorianCalendar(2010, 1, 2, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;

        List<Meeting> meetings = Meeting.findAllByTeam(team, mContext);
        assertEquals(5, meetings.size());
        assertEquals(new GregorianCalendar(2010, 1, 7, 10, 15, 0).getTime(), meetings.get(0).getDateTime());
        assertEquals(new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), meetings.get(1).getDateTime());
        assertEquals(new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), meetings.get(2).getDateTime());
        assertEquals(new GregorianCalendar(2010, 1, 2, 10, 15, 0).getTime(), meetings.get(3).getDateTime());
        assertEquals(new GregorianCalendar(2010, 1, 1, 10, 15, 0).getTime(), meetings.get(4).getDateTime());
    }

    @MediumTest
    public void test_find_by_team_and_date() {
        Team team = new Team("Test Team");
        new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);
        new Meeting(team, new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;
        Meeting expected = new Meeting(team, new GregorianCalendar(2010, 1, 7, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;
        new Meeting(team, new GregorianCalendar(2010, 1, 1, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;
        new Meeting(team, new GregorianCalendar(2010, 1, 2, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;

        Meeting actual = Meeting.findByTeamAndDate(team, new GregorianCalendar(2010, 1, 7, 10, 15, 0).getTime(), mContext);
        assertEquals(expected.getId(), actual.getId());
    }

    @MediumTest
    public void test_delete_all_by_team() {
        Team team = new Team("Test Team");
        new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);
        new Meeting(team, new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;

        assertFalse(Meeting.findAllByTeam(team, mContext).isEmpty());
        Meeting.deleteAllByTeam(team, mContext);
        assertTrue(Meeting.findAllByTeam(team, mContext).isEmpty());
    }
}
