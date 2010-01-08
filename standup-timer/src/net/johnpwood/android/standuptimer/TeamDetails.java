package net.johnpwood.android.standuptimer;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class TeamDetails extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        TabHost mTabHost = getTabHost();
        
        mTabHost.addTab(mTabHost.newTabSpec("stats_tab").
                setIndicator(this.getString(R.string.stats)).
                setContent(R.id.team_stats));
        mTabHost.addTab(mTabHost.newTabSpec("meetings_tab").
                setIndicator(this.getString(R.string.meetings)).
                setContent(R.id.team_meetings_list));
        
        mTabHost.setCurrentTab(0);
    }
}
