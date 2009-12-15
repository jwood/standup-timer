package net.johnpwood.android.standuptimer.test;

import net.johnpwood.android.standuptimer.R;
import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.mock.TeamListMock;
import net.johnpwood.android.standuptimer.model.Team;

import org.easymock.EasyMock;

import android.content.DialogInterface;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.MenuItem;
import android.widget.EditText;

public class TeamListTest extends ActivityUnitTestCase<TeamListMock> {
    private TeamListMock a = null;
    private TeamDAO dao = null;

    public TeamListTest() {
        super(TeamListMock.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        a = startActivity(intent, null, null);
        dao = a.createTeamDAO();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        dao.deleteAll();
        dao.close();            
    }

    @MediumTest
    public void test_about_box_displayed_successfully() {
        MenuItem menuItem = EasyMock.createMock(MenuItem.class);
        EasyMock.expect(menuItem.getItemId()).andReturn(R.id.add_team);

        EasyMock.replay(menuItem);
        a.onOptionsItemSelected(menuItem);
        assertTrue(a.displayAddTeamDialogCalled());
        EasyMock.verify(menuItem);
    }

    public void test_can_add_new_team_via_the_add_team_dialog() {
        EditText collectedText = (EditText) a.getTextEntryView().findViewById(R.id.collected_text);
        collectedText.setText("New Team");
        
        DialogInterface.OnClickListener addTeamButtonListener = a.addTeamButtonListener();
        addTeamButtonListener.onClick(null, 0);

        assertNotNull(dao.findByName("New Team"));
        assertEquals(1, dao.findAllTeamNames().size());            
    }
}
