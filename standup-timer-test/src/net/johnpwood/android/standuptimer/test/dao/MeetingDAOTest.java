package net.johnpwood.android.standuptimer.test.dao;

import java.util.GregorianCalendar;

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
        assertEquals(5, meeting.getNumParticipants());
        assertEquals(240, meeting.getIndividualStatusLength());
        assertEquals(300, meeting.getMeetingLength());
        assertEquals(30, meeting.getQuickestStatus());
        assertEquals(120, meeting.getLongestStatus());
    }
}
