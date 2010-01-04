package net.johnpwood.android.standuptimer.test;

import net.johnpwood.android.standuptimer.TeamList;
import net.johnpwood.android.standuptimer.dao.DAOFactory;
import net.johnpwood.android.standuptimer.dao.TeamDAO;
import android.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.test.RenamingDelegatingContext;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.KeyEvent;

public class TeamListTest extends ActivityInstrumentationTestCase2<TeamList> {
    private TeamList a = null;
    private TeamDAO dao = null;
    private DAOFactory daoFactory = DAOFactory.getInstance();

    public TeamListTest() {
        super("net.johnpwood.android.standuptimer", TeamList.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        a = getActivity();

        daoFactory.setCacheDAOInstances(true);
        daoFactory.setGlobalContext(new RenamingDelegatingContext(a, "test_"));
        dao = daoFactory.getTeamDAO(a);
    }

    @MediumTest
    public void test_can_add_a_new_team() {
        dao.deleteAll();
        sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_A);
        sendKeys("T E S T SPACE T E A M");
        TouchUtils.clickView(this, a.getCreateTeamDialog().getButton(AlertDialog.BUTTON_POSITIVE));
        assertNotNull(dao.findByName("test team"));
    }

}
