package net.johnpwood.android.standuptimer;

import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.utils.Logger;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class TeamList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);

        TeamDAO dao = null;
        try {
            dao = new TeamDAO(this);
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dao.findAllTeamNames()));
        } finally {
            dao.close();
        }

        getListView().setTextFilterEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.teams_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.add_team:
            Logger.d("Displaying the about box");
            displayAddTeamDialog();
            return true;
        default:
            Logger.e("Unknown menu item selected");
            return false;
        }
    }

    protected void displayAddTeamDialog() {
        showDialog(0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // This example shows how to add a custom layout to an AlertDialog
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.collect_text, null);
        return new AlertDialog.Builder(this).setTitle(R.string.team_label).setView(textEntryView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String mTitle = ((EditText) textEntryView.findViewById(R.id.collected_text)).getText().toString();
                        
                    }
                }).setNegativeButton(R.string.revert, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).create();
    }
}
