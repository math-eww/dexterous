package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class PokemonDetailsActivity extends ActionBarActivity {

    private static Context con;
    private static int pokePosition;
    private Menu menu;
    private Pokemon poke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        Intent intent = getIntent();
        pokePosition = intent.getIntExtra("pokemon", 0);
        poke = MainActivity.pokemonList.get(pokePosition);

        final Context c = MainActivity.c;
        con = this;

        final GifImageView pokeSprite = (GifImageView) findViewById(R.id.pokeDetailsImage);

        TextView pokeName = (TextView) findViewById(R.id.pokeDetailsName);
        TextView pokeNum = (TextView) findViewById(R.id.pokeDetailsNum);
        TextView pokeTypes = (TextView) findViewById(R.id.pokeDetailsTypes);
        TextView pokeHeightWeight = (TextView) findViewById(R.id.pokeDetailsHeighWeight);
        TextView pokeEggTypesList = (TextView) findViewById(R.id.pokeDetailsEggGroups);

        TextView pokeHp = (TextView) findViewById(R.id.pokeDetailsHp);
        TextView pokeAtk = (TextView) findViewById(R.id.pokeDetailsAtk);
        TextView pokeDef = (TextView) findViewById(R.id.pokeDetailsDef);
        TextView pokeSpAtk = (TextView) findViewById(R.id.pokeDetailsSpAtk);
        TextView pokeSpDef = (TextView) findViewById(R.id.pokeDetailsSpDef);
        TextView pokeSpd = (TextView) findViewById(R.id.pokeDetailsSpd);
        TextView pokeTtl = (TextView) findViewById(R.id.pokeDetailsTotals);

        LinearLayout evoList = (LinearLayout) findViewById(R.id.pokeDetailsEvoList);
        TextView abilitiesListText = (TextView) findViewById(R.id.pokeDetailsAbilities);

        final String subfolderNotShiny = "xy-animated";
        final String subfolderShiny = "xy-animated-shiny";
        final String gifId = poke.getThreeDigitStringNumber() + ".gif";
        pokeSprite.setScaleType(ImageView.ScaleType.CENTER);
        pokeSprite.setScaleX(3.5f); pokeSprite.setScaleY(3.5f);
        pokeSprite.setClickable(true);
        pokeSprite.setOnClickListener(new View.OnClickListener() {
            String subfolder = subfolderNotShiny;
            @Override
            public void onClick(View v) {
                if (subfolder.equals(subfolderNotShiny)) {
                    subfolder = subfolderShiny;
                    try {
                        GifDrawable gifFromAssets = new GifDrawable(getAssets(), subfolder + "/" + gifId);
                        pokeSprite.setImageDrawable(gifFromAssets);
                    } catch (IOException e) {
                        Log.e("Error in gif loading: " + gifId, e.toString());
                    }
                } else {
                    subfolder = subfolderNotShiny;
                    try {
                        GifDrawable gifFromAssets = new GifDrawable(getAssets(), subfolderNotShiny + "/" + gifId);
                        pokeSprite.setImageDrawable(gifFromAssets);
                    } catch (IOException e) {
                        Log.e("Error in gif loading: " + gifId, e.toString());
                    }
                }
            }
        });
        try {
            GifDrawable gifFromAssets = new GifDrawable(getAssets(), subfolderNotShiny + "/" + gifId);
            pokeSprite.setImageDrawable(gifFromAssets);
        } catch (IOException e) {
            Log.e("Error in gif loading: " + gifId, e.toString());
        }

        String types = poke.getTypeOne();
        if (!poke.getTypeTwo().equals("")) { types = types + " | " + poke.getTypeTwo(); }

        pokeName.setText(poke.getName());
        pokeNum.setText(poke.getThreeDigitStringNumber());
        pokeTypes.setText(types);

        String pokemonHeight = poke.getHeight();
        if (pokemonHeight.length() < 2) { pokemonHeight = ("00" + pokemonHeight).substring(pokemonHeight.length()); }
        pokemonHeight = new StringBuilder(pokemonHeight).insert(pokemonHeight.length()-1, ".").toString();
        String pokemonWeight = poke.getWeight();
        if (pokemonWeight.length() < 2) { pokemonWeight = ("00" + pokemonWeight).substring(pokemonWeight.length()); }
        pokemonWeight = new StringBuilder(pokemonWeight).insert(pokemonWeight.length()-1, ".").toString();
        pokeHeightWeight.setText("Height: " + pokemonHeight + " m" + "\n" + "Weight: " + pokemonWeight + " kg");

        pokeHp.setTypeface(Typeface.MONOSPACE);
        pokeAtk.setTypeface(Typeface.MONOSPACE);
        pokeDef.setTypeface(Typeface.MONOSPACE);
        pokeSpAtk.setTypeface(Typeface.MONOSPACE);
        pokeSpDef.setTypeface(Typeface.MONOSPACE);
        pokeSpd.setTypeface(Typeface.MONOSPACE);
        pokeTtl.setTypeface(Typeface.MONOSPACE);
        int[] stats = poke.getStats();
        int total = stats[0] + stats[1] + stats[2] + stats[3] + stats[4] + stats[5];
        pokeHp.setText(String.format("%-10s %3d", "HP:", stats[0]));
        pokeAtk.setText(String.format("%-10s %3d","Attack:",stats[1]));
        pokeDef.setText(String.format("%-10s %3d","Defense:",stats[2]));
        pokeSpAtk.setText(String.format("%-10s %3d","Sp. Atk:",stats[3]));
        pokeSpDef.setText(String.format("%-10s %3d","Sp. Def:",stats[4]));
        pokeSpd.setText(String.format("%-10s %3d","Speed:",stats[5]));
        pokeTtl.setText(String.format("%-10s %3d","Total:",total));

        if (!poke.getEvolvesFrom().equals("")) {
            TextView evoHeader = new TextView(getApplicationContext());
            evoHeader.setText("Evolves from:");
            evoHeader.setTextColor(Color.BLACK);
            evoList.addView(evoHeader);

            String to = poke.getEvolvesFrom();
            String num = String.valueOf(poke.getEvolvesFromNum());
            num = ("000" + num).substring(num.length());

            ImageView tempImage = new ImageView(getApplicationContext());
            try {
                GifDrawable tempGifFromAssets = new GifDrawable(getAssets(), "xy-animated" + "/" + num + ".gif");
                tempImage.setImageDrawable(tempGifFromAssets);
                tempImage.setMinimumWidth(3);
                tempImage.setMaxWidth(3);
                tempImage.setMinimumHeight(3);
                tempImage.setMaxHeight(3);
                tempImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } catch (IOException e) {
                Log.e("Error in gif loading (evolution): " + gifId, e.toString());
            }

            String tempTextShow = num + " " + to;

            TextView tempText = new TextView(getApplicationContext());
            tempText.setText(tempTextShow);
            tempText.setTextColor(Color.BLACK);
            tempText.setGravity(Gravity.RIGHT);
            tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            LinearLayout rowLayout = new LinearLayout(getApplicationContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.addView(tempImage);
            rowLayout.addView(tempText);

            rowLayout.setClickable(true);
            final int newPokePosition = Integer.parseInt(num) - 1;
            rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(c,PokemonDetailsActivity.class);
                    intent.putExtra("pokemon",newPokePosition);
                    c.startActivity(intent);
                    finish();
                }
            });

            evoList.addView(rowLayout);
        }

        ArrayList<Bundle> evolutionList = poke.getEvolutions();
        if (evolutionList.size() > 0) {
            TextView evoHeader = new TextView(getApplicationContext());
            evoHeader.setText("Evolves into:");
            evoHeader.setTextColor(Color.BLACK);
            evoList.addView(evoHeader);
        }
        Collections.sort(evolutionList, new Comparator() {
            public int compare(Object o1, Object o2) {
                Bundle p1 = (Bundle) o1;
                Bundle p2 = (Bundle) o2;
                return p1.getString("num").compareTo(p2.getString("num"));
            }
        });
        for (Bundle evoBundle : evolutionList) {
            int level = 0;
            String method = "";
            String detail = "";
            String mega = "";
            boolean isMega = false;
            String to = evoBundle.getString("to");
            String num = evoBundle.getString("num");
            num = ("000" + num).substring(num.length());
            if (evoBundle.containsKey("level")) {
                level = evoBundle.getInt("level");
            }
            if (evoBundle.containsKey("method")) {
                method = evoBundle.getString("method");
            }
            if (evoBundle.containsKey("detail")) {
                detail = evoBundle.getString("detail");
            }

            if (detail.equals("mega")) {
                isMega = true;
                //get substring at the end of to field
                mega = to.substring(poke.getName().length());
                //set num to current pokemon number
                num = poke.getThreeDigitStringNumber();
            }

            ImageView tempImage = new ImageView(getApplicationContext());
            try {
                GifDrawable tempGifFromAssets = new GifDrawable(getAssets(), "xy-animated" + "/" + num + mega + ".gif");
                tempImage.setImageDrawable(tempGifFromAssets);
                tempImage.setMinimumWidth(3);
                tempImage.setMaxWidth(3);
                tempImage.setMinimumHeight(3);
                tempImage.setMaxHeight(3);
                tempImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            } catch (IOException e) {
                Log.e("Error in gif loading (evolution): " + gifId, e.toString());
            }

            String tempTextShow = num + " " + to + " Method: " + method;
            if (method.equals("level_up")) { tempTextShow = num + " - " + to + "\n" + "Level: " + level; }
            if (method.equals("stone")) { tempTextShow = num + " - " + to + "\n" + "With stone " + detail; }
            if (method.equals("other")) { tempTextShow = num + " - " + to + "\n" + "Other " + detail; }

            TextView tempText = new TextView(getApplicationContext());
            tempText.setText(tempTextShow);
            tempText.setTextColor(Color.BLACK);
            tempText.setGravity(Gravity.RIGHT);
            tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            LinearLayout rowLayout = new LinearLayout(getApplicationContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.addView(tempImage);
            rowLayout.addView(tempText);

            if (!isMega) {
                rowLayout.setClickable(true);
                //final int newPokePosition = MainActivity.pokemonList.indexOf(poke);
                final int newPokePosition = Integer.parseInt(num) - 1;
                rowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(c,PokemonDetailsActivity.class);
                        intent.putExtra("pokemon",newPokePosition);
                        c.startActivity(intent);
                        finish();
                    }
                });
            }

            evoList.addView(rowLayout);
        }

        ArrayList<Bundle> abilities = poke.getAbilities();
        String abilitiesText = "Abilities: ";
        String middleSepText = "";
        boolean shouldSnip = false;
        if (abilities.size() > 1) { middleSepText = ", "; shouldSnip = true; }
        for (Bundle abil : abilities) {
            abilitiesText = abilitiesText + " " + abil.getString("name") + middleSepText;
        }
        if (shouldSnip) abilitiesText = abilitiesText.substring(0,abilitiesText.length()-2);
        abilitiesListText.setText(abilitiesText);

        ArrayList<Bundle> eggTypes = poke.getEggTypes();
        String eggTypesText = "Egg types: ";
        middleSepText = "";
        shouldSnip = false;
        if (eggTypes.size() > 1) { middleSepText = ", "; shouldSnip = true; }
        for (Bundle eggTy : eggTypes) {
            eggTypesText = eggTypesText + " " + eggTy.getString("name") + middleSepText;
        }
        if (shouldSnip) eggTypesText = eggTypesText.substring(0,eggTypesText.length()-2);
        pokeEggTypesList.setText(eggTypesText);
        //TODO: Make multiple new pages: moveset page, type effectiveness page
        //TODO: crop off extra bit of name (bit after a dash, like deoxys-attack)
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_pokemon_details, menu);

        String num;
        if (!(poke.getNumber() < 2)) {
            num = String.valueOf(poke.getNumber() - 1);
            num = ("000" + num).substring(num.length());
            setOptionTitle(R.id.action_prev, num);
        } else { hideOption(R.id.action_prev); }
        if (!(poke.getNumber() > MainActivity.pokemonList.size() - 1)) {
            num = String.valueOf(poke.getNumber() + 1);
            num = ("000" + num).substring(num.length());
            setOptionTitle(R.id.action_next, num);
        } else { hideOption(R.id.action_next); }

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

        if (id == R.id.action_ivs) {
            Intent intent = new Intent(con,IVCalculator.class);
            intent.putExtra("pokemon",pokePosition);
            con.startActivity(intent);
        }

        if (id == R.id.action_prev) {
            Intent intent = new Intent(con,PokemonDetailsActivity.class);
            intent.putExtra("pokemon",pokePosition-1);
            con.startActivity(intent);
            finish();
        }

        if (id == R.id.action_next) {
            Intent intent = new Intent(con,PokemonDetailsActivity.class);
            intent.putExtra("pokemon",pokePosition+1);
            con.startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id)
    {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu.findItem(id);
        item.setTitle(title);
    }

    private void setOptionIcon(int id, int iconRes)
    {
        MenuItem item = menu.findItem(id);
        item.setIcon(iconRes);
    }
}
