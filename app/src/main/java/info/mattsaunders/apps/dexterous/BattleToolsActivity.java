package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Matt on 06/03/2015. Type effectiveness charts
 */
public class BattleToolsActivity extends ActionBarActivity {

    private LinearLayout offensiveSuper;
    private LinearLayout offensiveNotvery;
    private LinearLayout offensiveDoesnot;
    private LinearLayout defensiveSuper;
    private LinearLayout defensiveNotvery;
    private LinearLayout defensiveDoesnot;
    private Context con;

    private String selectedTypeOne = "";
    private String selectedTypeTwo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_tools);

        con = this;

        offensiveSuper = (LinearLayout) findViewById(R.id.offensiveSupereffective);
        offensiveNotvery = (LinearLayout) findViewById(R.id.offensiveNotVeryEffective);
        offensiveDoesnot = (LinearLayout) findViewById(R.id.offensiveDoesNotEffect);
        defensiveSuper = (LinearLayout) findViewById(R.id.defensiveSupereffective);
        defensiveNotvery = (LinearLayout) findViewById(R.id.defensiveNotVeryEffective);
        defensiveDoesnot = (LinearLayout) findViewById(R.id.defensiveDoesNotEffect);

        //TODO: text may be getting cut off if list is too long (ie steel and water)

        Spinner typeOneSelector = (Spinner) findViewById(R.id.typeOneSpinner);
        Spinner typeTwoSelector = (Spinner) findViewById(R.id.typeTwoSpinner);

        ArrayList<String> types = new ArrayList<>(TypeEffectiveness.typeNamesList);
        types.add(0,"");

        ArrayAdapter<String> typesList = new ArrayAdapter<>(con, R.layout.support_simple_spinner_dropdown_item, types);
        typeOneSelector.setAdapter(typesList);
        typeTwoSelector.setAdapter(typesList);

        typeOneSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTypeOne = (String) parent.getItemAtPosition(position);
                performEffectivenessCalculation(selectedTypeOne, selectedTypeTwo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        typeTwoSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTypeTwo = (String) parent.getItemAtPosition(position);
                performEffectivenessCalculation(selectedTypeOne, selectedTypeTwo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void performEffectivenessCalculation(String typeOne, String typeTwo) {

        offensiveSuper.removeAllViews();
        offensiveNotvery.removeAllViews();
        offensiveDoesnot.removeAllViews();
        defensiveSuper.removeAllViews();
        defensiveNotvery.removeAllViews();
        defensiveDoesnot.removeAllViews();

        if (!typeOne.equals("") || !typeTwo.equals("")) {
            if (typeOne.equals("")) { typeOne = typeTwo; typeTwo = ""; }

            ArrayList<String> supereffective = new ArrayList<>();
            ArrayList<String> notveryeffective = new ArrayList<>();
            ArrayList<String> doesnoteffect = new ArrayList<>();
            ArrayList<Float> supereffectivenum = new ArrayList<>();
            ArrayList<Float> notveryeffectivenum = new ArrayList<>();
            ArrayList<Float> doesnoteffectnum = new ArrayList<>();

            ArrayList<String> def_supereffective = new ArrayList<>();
            ArrayList<String> def_notveryeffective = new ArrayList<>();
            ArrayList<String> def_doesnoteffect = new ArrayList<>();
            ArrayList<Float> def_supereffectivenum = new ArrayList<>();
            ArrayList<Float> def_notveryeffectivenum = new ArrayList<>();
            ArrayList<Float> def_doesnoteffectnum = new ArrayList<>();

            //Get offensive type effectiveness
            Bundle typeEffectiveness = TypeEffectiveness.getTypeEffectiveness(typeOne, typeTwo);

            for (String type : TypeEffectiveness.typeNamesList) {
                float effectiveness = typeEffectiveness.getFloat(type);
                if (effectiveness < 1) {
                    if (effectiveness == 0) {
                        doesnoteffect.add(type);
                        doesnoteffectnum.add(effectiveness);
                    } else {
                        notveryeffective.add(type);
                        notveryeffectivenum.add(effectiveness);
                    }
                } else if (effectiveness > 1) {
                    supereffective.add(type);
                    supereffectivenum.add(effectiveness);
                }
            }

            for (int i = 0; i < supereffective.size(); i++) {
                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(supereffective.get(i) + " (" + supereffectivenum.get(i) + ")");
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.CENTER);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                offensiveSuper.addView(tempText);
            }
            for (int i = 0; i < notveryeffective.size(); i++) {
                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(notveryeffective.get(i) + " (" + notveryeffectivenum.get(i) + ")");
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.CENTER);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                offensiveNotvery.addView(tempText);
            }
            for (int i = 0; i < doesnoteffect.size(); i++) {
                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(doesnoteffect.get(i) + " (" + doesnoteffectnum.get(i) + ")");
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.CENTER);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                offensiveDoesnot.addView(tempText);
            }

            typeEffectiveness = TypeEffectiveness.getDefensiveTypeEffectiveness(typeOne, typeTwo);

            for (String type : TypeEffectiveness.typeNamesList) {
                float effectiveness = typeEffectiveness.getFloat(type);
                if (effectiveness < 1) {
                    if (effectiveness == 0) {
                        def_doesnoteffect.add(type);
                        def_doesnoteffectnum.add(effectiveness);
                    } else {
                        def_notveryeffective.add(type);
                        def_notveryeffectivenum.add(effectiveness);
                    }
                } else if (effectiveness > 1) {
                    def_supereffective.add(type);
                    def_supereffectivenum.add(effectiveness);
                }
            }

            for (int i = 0; i < def_supereffective.size(); i++) {
                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(def_supereffective.get(i) + " (" + def_supereffectivenum.get(i) + ")");
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.CENTER);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                defensiveSuper.addView(tempText);
            }
            for (int i = 0; i < def_notveryeffective.size(); i++) {
                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(def_notveryeffective.get(i) + " (" + def_notveryeffectivenum.get(i) + ")");
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.CENTER);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                defensiveNotvery.addView(tempText);
            }
            for (int i = 0; i < def_doesnoteffect.size(); i++) {
                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(def_doesnoteffect.get(i) + " (" + def_doesnoteffectnum.get(i) + ")");
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.CENTER);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                defensiveDoesnot.addView(tempText);
            }

        }

    }

    //EOF
}
