package info.mattsaunders.apps.dexterous;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class PreferencesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //Find buttons
        Button redownloadSprites = (Button) findViewById(R.id.pref_redownloadSprites);
        Button backupMarkers = (Button) findViewById(R.id.pref_backupMarkers);
        Button restoreMarkers = (Button) findViewById(R.id.pref_restoreMarkers);
        //Set button click listeners
        redownloadSprites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                new DownloadSprites.CallAPI().execute();
            }
        });
        backupMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.backupPokeballIndicators();
            }
        });
        restoreMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.restorePokeballIndicators();
            }
        });
    }
}
