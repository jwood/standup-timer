package net.johnpwood.android.standuptimer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class About extends Activity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        View okButton = findViewById(R.id.about_ok);
        okButton.setOnClickListener(this);
        View projectUrl = findViewById(R.id.project_url);
        projectUrl.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.about_ok:
            finish();
            break;
        case R.id.project_url:
            openProjectUrlInBrowser();
            break;
        }
    }

    private void openProjectUrlInBrowser() {
        Uri uri = Uri.parse(getString(R.string.about_project_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
