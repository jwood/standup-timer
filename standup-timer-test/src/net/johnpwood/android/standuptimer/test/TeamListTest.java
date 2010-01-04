package net.johnpwood.android.standuptimer.test;

import net.johnpwood.android.standuptimer.R;
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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        a = getActivity();

        daoFactory.setCacheDAOInstances(true);
        daoFactory.setGlobalContext(new RenamingDelegatingContext(a, "test_"));
        dao = daoFactory.getTeamDAO(a);
    }

    @Override
    protected void tearDown() throws Exception {
        dao.deleteAll();
        super.tearDown();
    }

    @MediumTest
    public void test_can_add_a_new_team() {
        sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_A);
        sendKeys("T E S T SPACE T E A M");
        TouchUtils.clickView(this, a.getCreateTeamDialog().getButton(AlertDialog.BUTTON_POSITIVE));
        assertNotNull(dao.findByName("test team"));
    }

    @MediumTest
    public void test_can_delete_an_existing_team() {
        sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_A);
        sendKeys("T E S T SPACE T E A M");
        TouchUtils.clickView(this, a.getCreateTeamDialog().getButton(AlertDialog.BUTTON_POSITIVE));

        TouchUtils.longClickView(this, a.getListView().getChildAt(0));
        getInstrumentation().waitForIdleSync();
        getInstrumentation().invokeContextMenuAction(a, R.id.delete_team, 0);
        getInstrumentation().waitForIdleSync();

        TouchUtils.clickView(this, a.getConfirmDeleteTeamDialog().getButton(AlertDialog.BUTTON_POSITIVE));
        assertNull(dao.findByName("test team"));
    }

    @MediumTest
    public void test_deleting_a_team_can_be_aborted() {
        sendKeys(KeyEvent.KEYCODE_MENU, KeyEvent.KEYCODE_A);
        sendKeys("T E S T SPACE T E A M");
        TouchUtils.clickView(this, a.getCreateTeamDialog().getButton(AlertDialog.BUTTON_POSITIVE));

        TouchUtils.longClickView(this, a.getListView().getChildAt(0));
        getInstrumentation().waitForIdleSync();
        getInstrumentation().invokeContextMenuAction(a, R.id.delete_team, 0);
        getInstrumentation().waitForIdleSync();

        TouchUtils.clickView(this, a.getConfirmDeleteTeamDialog().getButton(AlertDialog.BUTTON_NEGATIVE));
        assertNotNull(dao.findByName("test team"));
    }
}
