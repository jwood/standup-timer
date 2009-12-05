package net.johnpwood.android.standuptimer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Teams extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teams);
        View addTeamButton = findViewById(R.id.add_team_button);
        addTeamButton.setOnClickListener(this);
    }

    public void onClick(View v) {
    }
}
