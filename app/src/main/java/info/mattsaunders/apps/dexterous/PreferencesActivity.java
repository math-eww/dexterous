package info.mattsaunders.apps.dexterous;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                confirmationDialog("redownload Pokemon sprites",0);
            }
        });
        backupMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog("backup Pokedex markers",1);
            }
        });
        restoreMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog("restore Pokedex markers",2);
            }
        });
    }

    private void confirmationDialog(String confirmWhat, final int actionToDo) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to " + confirmWhat + "?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        switch (actionToDo) {
                            case 0:
                                finish();
                                new DownloadSprites.CallAPI().execute();
                                break;
                            case 1:
                                Utilities.backupPokeballIndicators();
                                break;
                            case 2:
                                Utilities.restorePokeballIndicators();
                                break;
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
