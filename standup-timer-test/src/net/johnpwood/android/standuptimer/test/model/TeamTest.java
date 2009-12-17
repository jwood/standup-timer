package net.johnpwood.android.standuptimer.test.model;

import java.util.List;

import net.johnpwood.android.standuptimer.dao.DAOFactory;
import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.model.Team;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.MediumTest;

public class TeamTest extends AndroidTestCase {
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
}
