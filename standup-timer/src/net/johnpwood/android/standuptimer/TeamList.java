package net.johnpwood.android.standuptimer;

import net.johnpwood.android.standuptimer.dao.TeamDAO;
import net.johnpwood.android.standuptimer.model.Team;
import net.johnpwood.android.standuptimer.utils.Logger;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class TeamList extends ListActivity {
    private View textEntryView = null;
    private Dialog createTeamDialog = null;

    private Handler updateTeamListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateTeamList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        updateTeamList();
        getTextEntryView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.teams_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.add_team:
            Logger.d("Displaying the add team dialog box");
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
        if (createTeamDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.add_team);
            builder.setView(getTextEntryView());
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.ok, addTeamButtonListener());
            builder.setNegativeButton(R.string.revert, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            createTeamDialog = builder.create();
        }
        return createTeamDialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        EditText collectedTextView = (EditText) getTextEntryView().findViewById(R.id.collected_text);
        collectedTextView.setText("");
    }

    protected void updateTeamList() {
        TeamDAO dao = null;
        try {
            dao = createTeamDAO();
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dao.findAllTeamNames()));
            getListView().setTextFilterEnabled(true);
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }

    synchronized protected View getTextEntryView() {
        if (textEntryView == null) {
            LayoutInflater factory = LayoutInflater.from(this);
            textEntryView = factory.inflate(R.layout.collect_text, null);
        }
        return textEntryView;
    }

    protected DialogInterface.OnClickListener addTeamButtonListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText collectedTextView = (EditText) getTextEntryView().findViewById(R.id.collected_text);
                String name = collectedTextView.getText().toString();
                TeamDAO dao = null;
                try {
                    dao = createTeamDAO();
                    dao.save(new Team(name));
                    updateTeamListHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                } finally {
                    dao.close();
                }
            }
        };
    }

    protected TeamDAO createTeamDAO() {
        return new TeamDAO(this);
    }
}
