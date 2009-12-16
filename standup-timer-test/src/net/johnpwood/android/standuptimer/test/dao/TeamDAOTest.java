package net.johnpwood.android.standuptimer.test.dao;

import java.util.List;

import net.johnpwood.android.standuptimer.dao.DuplicateTeamException;
import net.johnpwood.android.standuptimer.dao.InvalidTeamNameException;
import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.model.Team;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;

public class TeamDAOTest extends AndroidTestCase {

    private TeamDAO dao = null;

    @Override
    protected void setUp() {
        dao = new TeamDAO(new RenamingDelegatingContext(mContext, "test_"));
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
    public void test_create_a_team_with_an_apostrophy_in_the_name() {
        Team team = new Team("John's Team");
        team = dao.save(team);
        assertNotNull(team.getId());
        assertEquals("John's Team", team.getName());
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

        List<String> teams = dao.findAllTeamNames();
        assertEquals(4, teams.size());
    }

    @MediumTest
    public void test_can_find_a_team_by_name() {
        dao.save(new Team("Test Team 1"));

        Team team = dao.findByName("Test Team 1");
        assertEquals("Test Team 1", team.getName());
        assertNotNull(team.getId());
    }

    @MediumTest
    public void test_find_by_team_returns_null_if_team_cannot_be_found() {
        Team team = dao.findByName("Blah Blah Blah");
        assertNull(team);
    }

    @MediumTest
    public void test_cannot_create_a_team_with_a_name_that_already_exists() {
        dao.save(new Team("Test Team 1"));
        try {
            dao.save(new Team("Test Team 1"));
            assertTrue("Should have thrown an exception", false);
        } catch (DuplicateTeamException e) {
            assertTrue(true);
        }
    }

    @MediumTest
    public void test_cannot_create_a_team_with_an_empty_name() {
        try {
            dao.save(new Team(""));
            assertTrue("Should have thrown an exception", false);
        } catch (InvalidTeamNameException e) {
            assertTrue(true);
        }

        try {
            dao.save(new Team("    "));
            assertTrue("Should have thrown an exception", false);
        } catch (InvalidTeamNameException e) {
            assertTrue(true);
        }
    }

    @MediumTest
    public void test_can_update_an_existing_team() {
        Team team1 = dao.save(new Team("Test Team 1"));
        dao.save(new Team(team1.getId(), "Test Team 2"));

        Team team = dao.findById(team1.getId());
        assertEquals("Test Team 2", team.getName());
    }

    @MediumTest
    public void test_can_delete_a_team() {
        Team team = dao.save(new Team("Test Team 1"));
        assertEquals(1, dao.findAllTeamNames().size());

        dao.delete(team);
        assertEquals(0, dao.findAllTeamNames().size());
    }
}
