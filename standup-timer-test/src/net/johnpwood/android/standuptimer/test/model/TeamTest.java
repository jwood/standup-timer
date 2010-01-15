package net.johnpwood.android.standuptimer.test.model;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.johnpwood.android.standuptimer.dao.DAOFactory;
import net.johnpwood.android.standuptimer.dao.DatabaseConstants;
import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.model.Meeting;
import net.johnpwood.android.standuptimer.model.MeetingStats;
import net.johnpwood.android.standuptimer.model.Team;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;

public class TeamTest extends AndroidTestCase implements DatabaseConstants {
    private TeamDAO dao = null;
    private DAOFactory daoFactory = DAOFactory.getInstance();

    @Override
    protected void setUp() {
        daoFactory.setGlobalContext(new RenamingDelegatingContext(mContext, "test_"));
        daoFactory.setCacheDAOInstances(true);
        dao = daoFactory.getTeamDAO(mContext);
    }

    @Override
    protected void tearDown() {
        dao.deleteAll();
        dao.close();
    }

    @MediumTest
    public void test_create_a_team() {
        Team team = Team.create("Test Team", mContext);
        assertNotNull(team.getId());
    }

    @MediumTest
    public void test_delete_a_team() {
        Team team = Team.create("Test Team", mContext);
        assertEquals(1, Team.findAllTeamNames(mContext).size());
        team.delete(mContext);
        assertEquals(0, Team.findAllTeamNames(mContext).size());        
    }

    @MediumTest
    public void test_deleting_a_team_deletes_its_meetings_as_well() {
        Team team = Team.create("Test Team", mContext);
        new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);
        new Meeting(team, new GregorianCalendar(2010, 1, 4, 10, 15, 0).getTime(), 5, 240, 300, 30, 120).save(mContext);;

        assertFalse(Meeting.findAllByTeam(team, mContext).isEmpty());
        team.delete(mContext);
        assertEquals(0, Team.findAllTeamNames(mContext).size());
        assertTrue(Meeting.findAllByTeam(team, mContext).isEmpty());
    }

    @MediumTest
    public void test_find_a_team_by_name() {
        Team.create("Test Team", mContext);
        assertNotNull(Team.findByName("Test Team", mContext));
    }
    
    @MediumTest
    public void test_find_all_team_names() {
        Team.create("Test Team 1", mContext);
        Team.create("Test Team 2", mContext);
        Team.create("Test Team 3", mContext);
        List<String> teamNames = Team.findAllTeamNames(mContext);

        assertEquals(3, teamNames.size());
        assertTrue(teamNames.contains("Test Team 1"));
        assertTrue(teamNames.contains("Test Team 2"));
        assertTrue(teamNames.contains("Test Team 3"));
    }

    @MediumTest
    public void test_get_average_meeting_stats() {
        Team team = Team.create("Test Team", mContext);
        Date dateTime = new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime();
        new Meeting(team, dateTime, 5, 301, 343, 30, 65).save(mContext);
        new Meeting(team, dateTime, 8, 534, 550, 32, 120).save(mContext);
        new Meeting(team, dateTime, 2, 234, 300, 23, 122).save(mContext);
        new Meeting(team, dateTime, 3, 765, 765, 15, 78).save(mContext);
        new Meeting(team, dateTime, 9, 444, 445, 10, 93).save(mContext);

        MeetingStats averageStats = team.getAverageMeetingStats(mContext);
        assertEquals(5.4f, averageStats.getNumParticipants());
        assertEquals(455.6f, averageStats.getIndividualStatusLength());
        assertEquals(480.6f, averageStats.getMeetingLength());
        assertEquals(22f, averageStats.getQuickestStatus());
        assertEquals(95.6f, averageStats.getLongestStatus());
    }

    @MediumTest
    public void test_has_meetings() {
        Team team = Team.create("Test Team No Meetings", mContext);
        assertFalse(team.hasMeetings(mContext));
        new Meeting(team, new GregorianCalendar(2010, 1, 5, 10, 15, 0).getTime(), 5, 301, 343, 30, 65).save(mContext);
        assertTrue(team.hasMeetings(mContext));
    }
}
