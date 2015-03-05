package info.mattsaunders.apps.dexterous;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;


public class IVCalculator extends ActionBarActivity {

    private static final String[] NATURES = {
            "Adamant", "Bashful", "Bold", "Brave", "Calm", "Careful",
            "Docile", "Gentle", "Hardy", "Hasty", "Impish", "Jolly",
            "Lax", "Lonely", "Mild", "Modest", "Naive", "Naughty",
            "Quiet", "Quirky", "Rash", "Relaxed", "Sassy", "Serious", "Timid"
    };
    private static final int[][] NATURES_VALUES = {
        {0,2,0,1,0,0}, //Adamant
        {0,0,0,0,0,0}, //Bashful
        {0,1,2,0,0,0}, //Bold
        {0,2,0,0,0,1}, //Brave
        {0,1,0,0,2,0}, //Calm
        {0,0,0,1,2,0}, //Careful
        {0,0,0,0,0,0}, //Docile
        {0,0,1,0,2,0}, //Gentle
        {0,0,0,0,0,0}, //Hardy
        {0,0,1,0,0,2}, //Hasty
        {0,0,2,1,0,0}, //Impish
        {0,0,0,1,0,2}, //Jolly
        {0,0,2,0,1,0}, //Lax
        {0,2,1,0,0,0}, //Lonely
        {0,0,1,2,0,0}, //Mild
        {0,1,0,2,0,0}, //Modest
        {0,0,0,0,1,2}, //Naive
        {0,2,0,0,1,0}, //Naughty
        {0,0,0,2,0,1}, //Quiet
        {0,0,0,0,0,0}, //Quirky
        {0,0,0,2,1,0}, //Rash
        {0,0,1,0,0,2}, //Relaxed
        {0,0,0,0,2,1}, //Sassy
        {0,0,0,0,0,0}, //Serious
        {0,1,0,0,0,2}, //Timid
    };
    private static int naturePostion = 100;
    private Pokemon poke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivcalculator);

        Intent intent = getIntent();
        int pokePosition = intent.getIntExtra("pokemon", 0);
        poke = Global.pokemonList.get(pokePosition);

        TextView pokeNameText = (TextView) findViewById(R.id.pokeIVname);
        TextView pokeBaseStatsText = (TextView) findViewById(R.id.pokeIVbasestats);
        TextView pokeMaxStatsText = (TextView) findViewById(R.id.pokeIVmaxstats);

        pokeBaseStatsText.setTypeface(Typeface.MONOSPACE);
        pokeMaxStatsText.setTypeface(Typeface.MONOSPACE);

        int[] baseStats = poke.getStats();
        String[] statNames = {"HP: ", "Atk: ", "Def: ", "Sp.Atk: ", "Sp.Def: ", "Spd: "};

        //Formulae:
        //Stat = ((Base * 2 + IV + (EV/4)) * Level / 100 + 5) * natureMod
        //HP = (Base * 2 + IV + (EV/4)) * Level / 100 + 10 + Level
        int stat;
        int base;
        int iv;
        int ev;
        int level;
        float natureMod;

        String baseStatsText = "Base Stats: " + "\n\n";
        String maxStatsText = "Max Stats: " + "\n\n";
        String statSeparator = "\n";
        String tempString;
        base = baseStats[0];
        iv = 31;
        ev = 255;
        level = 100;
        stat = (int) Math.floor((base * 2 + iv + (ev/4)) * level / 100 + 10 + level);
        tempString = String.format("%-10s %3d", statNames[0], stat);
        maxStatsText = maxStatsText + tempString + statSeparator;
        for (int i = 1; i < 6; i++) {
            base = baseStats[i];
            iv = 31;
            ev = 255;
            level = 100;
            natureMod = 1.1f;
            stat = (int) Math.floor(((base * 2 + iv + (ev/4)) * level / 100 + 5) * natureMod);
            tempString = String.format("%-10s %3d", statNames[i], stat);
            maxStatsText = maxStatsText + tempString + statSeparator;
        }
        for (int i = 0; i < 6; i++) {
            tempString = String.format("%-10s %3d", statNames[i], baseStats[i]);
            baseStatsText = baseStatsText + tempString + statSeparator;
        }

        pokeNameText.setText(poke.getName());
        pokeBaseStatsText.setText(baseStatsText);
        pokeMaxStatsText.setText(maxStatsText);

        //Find views for IV calculation
        EditText hpEntered = (EditText) findViewById(R.id.pokeIVhpEntered);
        EditText atkEntered = (EditText) findViewById(R.id.pokeIVatkEntered);
        EditText defEntered = (EditText) findViewById(R.id.pokeIVdefEntered);
        EditText spatkEntered = (EditText) findViewById(R.id.pokeIVspatkEntered);
        EditText spdefEntered = (EditText) findViewById(R.id.pokeIVspdefEntered);
        EditText spdEntered = (EditText) findViewById(R.id.pokeIVspdEntered);
        EditText lvlEntered = (EditText) findViewById(R.id.pokeIVlvlEntered);

        TextView hpOutput = (TextView) findViewById(R.id.pokeIVhpOutput);
        TextView atkOutput = (TextView) findViewById(R.id.pokeIVatkOutput);
        TextView defOutput = (TextView) findViewById(R.id.pokeIVdefOutput);
        TextView spatkOutput = (TextView) findViewById(R.id.pokeIVspatkOutput);
        TextView spdefOutput = (TextView) findViewById(R.id.pokeIVspdefOutput);
        TextView spdOutput = (TextView) findViewById(R.id.pokeIVspdOutput);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, NATURES
        );
        final AutoCompleteTextView natureEntered = (AutoCompleteTextView) findViewById(R.id.pokeIVnatureEditText);
        natureEntered.setAdapter(adapter);

        final TextView[] outputs = {hpOutput, atkOutput, defOutput, spatkOutput, spdefOutput, spdOutput};
        natureEntered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = natureEntered.getEditableText().toString();
                int pos = -1;
                for (int i = 0; i < NATURES.length; i++) {
                    if (NATURES[i].equals(selection)) {
                        pos = i;
                        break;
                    }
                }
                naturePostion = pos;
                System.out.println("NATURE SELECTED: " + NATURES[pos] + " VALUES: " + Arrays.toString(NATURES_VALUES[pos]));
                for (int i = 0; i < 6; i++) {
                    outputs[i].setTextColor(Color.BLACK);
                }
                for (int i = 0; i < 6; i++) {
                    if (NATURES_VALUES[pos][i] == 2) {
                        outputs[i].setTextColor(Color.RED);
                    }
                    if (NATURES_VALUES[pos][i] == 1) {
                        outputs[i].setTextColor(Color.BLUE);
                    }
                }
            }
        });

        Button calcButton = (Button) findViewById(R.id.pokeIVcalcIVsButton);
        calcButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                calculateIVs();
            }
        });
    }

    private void calculateIVs() {

        //Find views for IV calculation
        EditText hpEntered = (EditText) findViewById(R.id.pokeIVhpEntered);
        EditText atkEntered = (EditText) findViewById(R.id.pokeIVatkEntered);
        EditText defEntered = (EditText) findViewById(R.id.pokeIVdefEntered);
        EditText spatkEntered = (EditText) findViewById(R.id.pokeIVspatkEntered);
        EditText spdefEntered = (EditText) findViewById(R.id.pokeIVspdefEntered);
        EditText spdEntered = (EditText) findViewById(R.id.pokeIVspdEntered);
        EditText lvlEntered = (EditText) findViewById(R.id.pokeIVlvlEntered);

        TextView hpOutput = (TextView) findViewById(R.id.pokeIVhpOutput);
        TextView atkOutput = (TextView) findViewById(R.id.pokeIVatkOutput);
        TextView defOutput = (TextView) findViewById(R.id.pokeIVdefOutput);
        TextView spatkOutput = (TextView) findViewById(R.id.pokeIVspatkOutput);
        TextView spdefOutput = (TextView) findViewById(R.id.pokeIVspdefOutput);
        TextView spdOutput = (TextView) findViewById(R.id.pokeIVspdOutput);

        int[] baseStats = poke.getStats();
        int base;
         /*
        //Formulae to calculate IV/EV:
        //HP:
        //IV = ((HP - 10) * 100) / Level - 2*Base - EV/4 - 100
        //EV = (((HP - 10) * 100) / Level - 2*Base - IV - 100) * 4
        //Stats:
        //IV = ((Stat/Nature - 5) * 100) / Level - 2*Base - EV/4
        //EV = (((Stat/Nature - 5) * 100) / Level - 2*Base - IV) * 4
        */

        //Calculate IVs
        try {
            int userHP = Integer.parseInt(hpEntered.getText().toString());
            int userAtk = Integer.parseInt(atkEntered.getText().toString());
            int userDef = Integer.parseInt(defEntered.getText().toString());
            int userSpAtk = Integer.parseInt(spatkEntered.getText().toString());
            int userSpDef = Integer.parseInt(spdefEntered.getText().toString());
            int userSpd = Integer.parseInt(spdEntered.getText().toString());
            int userLvl = Integer.parseInt(lvlEntered.getText().toString());
            int[] userNature = NATURES_VALUES[naturePostion];

            //Calculate HP IV:
            base = baseStats[0];
            int userHPout = (int) Math.floor(((userHP - 10) * 100) / userLvl - 2 * base - 100);
            //Calculate Stat IVs:
            int [] userOut = {0,0,0,0,0};
            int [] userIn = {userHP, userAtk, userDef, userSpAtk, userSpDef, userSpd};
            for (int i = 1; i < 6; i++) {
                float userNatureMod = 1;
                if (userNature[i] == 1) { userNatureMod = 0.9f; }
                if (userNature[i] == 2) { userNatureMod = 1.1f; }
                userOut[i-1] = (int) Math.floor( ((userIn[i] / userNatureMod - 5) * 100) / userLvl - 2 * baseStats[i] );
            }
            int userAtkOut = userOut[0];
            int userDefOut = userOut[1];
            int userSpAtkOut = userOut[2];
            int userSpDefOut = userOut[3];
            int userSpdOut = userOut[4];

            //Assign values
            hpOutput.setText(String.valueOf(userHPout));
            atkOutput.setText(String.valueOf(userAtkOut));
            defOutput.setText(String.valueOf(userDefOut));
            spatkOutput.setText(String.valueOf(userSpAtkOut));
            spdefOutput.setText(String.valueOf(userSpDefOut));
            spdOutput.setText(String.valueOf(userSpdOut));
        } catch (Exception e) {
            Log.e("IV CALC ERROR: ", e.toString());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ivcalculator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
