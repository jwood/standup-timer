package net.johnpwood.android.standuptimer;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class TeamDetails extends TabActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_details);

        TabHost mTabHost = getTabHost();
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("TAB 1").setContent(R.id.textview1));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("TAB 2").setContent(R.id.textview2));
        mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("TAB 3").setContent(R.id.textview3));
        
        mTabHost.setCurrentTab(0);
    }
}
