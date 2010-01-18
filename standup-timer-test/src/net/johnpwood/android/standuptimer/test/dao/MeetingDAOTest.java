package net.johnpwood.android.standuptimer.test.dao;

import java.util.GregorianCalendar;
import java.util.List;

import net.johnpwood.android.standuptimer.dao.CannotUpdateMeetingException;
import net.johnpwood.android.standuptimer.dao.MeetingDAO;
import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.Team;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;

public class MeetingDAOTest extends AndroidTestCase {

    private MeetingDAO dao = null;

    @Override
    protected void setUp() {
        dao = new MeetingDAO(new RenamingDelegatingContext(mContext, "test_"));
    }

    @Override
    protected void tearDown() {
        dao.deleteAll();
        dao.close();
    }

    @MediumTest
    public void test_create_a_meeting() {
        Meeting meeting = new Meeting(new Team("Test Team"), new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120);
        meeting = dao.save(meeting);
        
        assertNotNull(meeting.getId());
        meeting = dao.findById(meeting.getId());
        assertEquals("Test Team", meeting.getTeam().getName());
        assertEquals(new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), meeting.getDateTime());
        assertEquals(5f, meeting.getMeetingStats().getNumParticipants());
        assertEquals(240.f, meeting.getMeetingStats().getIndividualStatusLength());
        assertEquals(300f, meeting.getMeetingStats().getMeetingLength());
        assertEquals(30f, meeting.getMeetingStats().getQuickestStatus());
        assertEquals(120f, meeting.getMeetingStats().getLongestStatus());
    }

    @MediumTest
    public void test_cannot_update_a_meeting_that_has_already_been_created() {
        Meeting meeting = new Meeting(new Team("Test Team"), new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120);
        meeting = dao.save(meeting);

        try {
            dao.save(meeting);
            fail("Should have thrown an exception");
        } catch (CannotUpdateMeetingException e) {
            assertTrue(true);
        }
    }

    @MediumTest
    public void test_find_all_meetings_by_team_in_cronological_order() {
        Team team = new Team("Test Team");
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 7, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 1, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 2, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));

        List<Meeting> meetings = dao.findAllByTeam(team);
        assertEquals(5, meetings.size());
        assertEquals(new GregorianCalendar(2010, 1, 7, 10, 15, 0).getTime(), meetings.get(0).getDateTime());
        assertEquals(new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), meetings.get(1).getDateTime());
        assertEquals(new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), meetings.get(2).getDateTime());
        assertEquals(new GregorianCalendar(2010, 1, 2, 10, 15, 0).getTime(), meetings.get(3).getDateTime());
        assertEquals(new GregorianCalendar(2010, 1, 1, 10, 15, 0).getTime(), meetings.get(4).getDateTime());
    }

    @MediumTest
    public void test_delete_a_single_meeting() {
        Meeting meeting = new Meeting(new Team("Test Team"), new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120);
        meeting = dao.save(meeting);
        meeting = dao.findById(meeting.getId());
        assertNotNull(meeting.getId());

        dao.delete(meeting);
        meeting = dao.findById(meeting.getId());
        assertNull(meeting);
    }

    @MediumTest
    public void test_find_by_team_and_date() {
        Team team = new Team("Test Team");
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        Meeting expected = dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 7, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 1, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 2, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));

        Meeting actual = dao.findByTeamAndDate(team, new GregorianCalendar(2010, 1, 7, 10, 15, 0).getTime());
        assertEquals(expected.getId(), actual.getId());
    }

    @MediumTest
    public void test_delete_all_by_team() {
        Team team = new Team("Test Team");
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));
        dao.save(new Meeting(team, new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), 5, 240, 300, 30, 120));

        assertFalse(dao.findAllByTeam(team).isEmpty());
        dao.deleteAllByTeam(team);
        assertTrue(dao.findAllByTeam(team).isEmpty());
    }
}
