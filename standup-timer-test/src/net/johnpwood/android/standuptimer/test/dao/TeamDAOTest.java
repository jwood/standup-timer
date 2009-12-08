package net.johnpwood.android.standuptimer.test.dao;

import java.util.List;

import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.model.Team;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class TeamDAOTest extends AndroidTestCase {

    private TeamDAO dao = null;

    @Override
    protected void setUp() {
        dao = new TeamDAO(mContext);
    }

    @Override
    protected void tearDown() {
        dao.deleteAll();
        dao.close();
    }

    @MediumTest
    public void test_create_a_team() {
        Team team = new Team("Test Team");
        team = dao.save(team);
        assertNotNull(team.getId());
        assertEquals("Test Team", team.getName());
    }

    @MediumTest
    public void test_find_a_team_by_id() {
        Team team = new Team("Another Test Team");
        team = dao.save(team);

        Team foundTeam = dao.findById(team.getId());
        assertEquals(team.getId(), foundTeam.getId());
        assertEquals(team.getName(), foundTeam.getName());
    }

    @MediumTest
    public void test_find_all_can_retrieve_all_teams() {
        dao.save(new Team("Test Team 1"));
        dao.save(new Team("Test Team 2"));
        dao.save(new Team("Test Team 3"));
        dao.save(new Team("Test Team 4"));

        List<Team> teams = dao.findAll();
        assertEquals(4, teams.size());
    }
}
