package info.mattsaunders.apps.dexterous;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PreferencesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //Find buttons
        Button redownloadSprites = (Button) findViewById(R.id.pref_redownloadSprites);
        Button backupMarkers = (Button) findViewById(R.id.pref_backupMarkers);
        Button restoreMarkers = (Button) findViewById(R.id.pref_restoreMarkers);
        Button toggleMarkers = (Button) findViewById(R.id.pref_changeMarkers);
        Button aboutApp = (Button) findViewById(R.id.pref_aboutApp);
        //Set button click listeners
        redownloadSprites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog("redownload Pokemon sprites", 0, -1, false);
            }
        });
        backupMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog("backup Pokedex markers", 1, -1, false);
            }
        });
        restoreMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationDialog("restore Pokedex markers", 2, -1, false);
            }
        });
        toggleMarkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDialog();
            }
        });
        aboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAbout();
            }
        });
    }

    private void confirmationDialog(String confirmWhat, final int actionToDo, final int extra, final boolean extraTwo) {
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
                            case 3:
                                Utilities.checkAllPokeballIndicators(extra,extraTwo);
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void toggleDialog() {
        final CharSequence[] items = {"Pokedex","Living Dex","My Team"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select which indicators to toggle");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                /*
                switch(item) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
                */
                toggleTrueOrFalse(item);
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void toggleTrueOrFalse(final int indicatorIndex) {
        final String toChange;
        switch (indicatorIndex) {
            case 0:
                toChange = "Pokedex";
                break;
            case 1:
                toChange = "Living Dex";
                break;
            case 2:
                toChange = "My Team";
                break;
            default:
                toChange = "";
                break;
        }
        new AlertDialog.Builder(this)
                .setMessage("Toggle all " + toChange + " indicators on or off?")
                .setCancelable(false)
                .setPositiveButton("On", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Confirm, then make the change
                        confirmationDialog("toggle all " + toChange + " indicators on", 3, indicatorIndex, true);
                    }
                })
                .setNegativeButton("Off", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Confirm, then make the change
                        confirmationDialog("toggle all " + toChange + " indicators off", 3, indicatorIndex, false);
                    }
                })
                .show();
    }

    private void showAbout() {
        View messageView = getLayoutInflater().inflate(R.layout.about_dialog_fragment, null, false);
        TextView description = (TextView) messageView.findViewById(R.id.about_appDescription);
        TextView credits = (TextView) messageView.findViewById(R.id.about_appCredits);
        TextView copyright = (TextView) messageView.findViewById(R.id.about_appCopyright);

        description.setMovementMethod(LinkMovementMethod.getInstance());
        credits.setMovementMethod(LinkMovementMethod.getInstance());
        copyright.setMovementMethod(LinkMovementMethod.getInstance());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_pokedex);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }
}
