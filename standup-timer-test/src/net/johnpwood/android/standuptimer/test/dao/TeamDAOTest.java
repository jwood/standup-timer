package net.johnpwood.android.standuptimer.test.dao;

import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.model.Team;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.MediumTest;

public class TeamDAOTest extends AndroidTestCase {
    @MediumTest
    public void test_create_a_team() {
        TeamDAO testTeam = new TeamDAO(mContext);
        Team team = new Team("Test Team");
        testTeam.save(team);
    }
}
