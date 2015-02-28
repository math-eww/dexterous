package info.mattsaunders.apps.dexterous;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class PokeDetailsTabs extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private static Context con;
    private static int pokePosition;
    private Menu menu;
    private static Pokemon poke;
    private static Activity activity;
    private static PokedexDatabase db;

    private static MoveListAdapter moveListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_details_tabs);


        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);


        Intent intent = getIntent();
        pokePosition = intent.getIntExtra("pokemon", 0);
        poke = MainActivity.pokemonList.get(pokePosition);
        int pokemonNumber = poke.getNumber();

        db = MainActivity.db;
        if (poke.getMoveset() == null) { poke.setMoveset(db.getMoveset(pokemonNumber)); }
        if (poke.getAbilities() == null) { poke.setAbilities(db.getAbilities(pokemonNumber)); }
        if (poke.getEggTypes() == null) { poke.setEggTypes(db.getEggGroups(pokemonNumber)); }
        if (poke.getEvolutions() == null) { poke.setEvolutions(db.getEvolutions(pokemonNumber)); }
        if (poke.getForms() == null) { poke.setForms(db.getForms(pokemonNumber)); }
        if (poke.getEvolvesFromNum() == 0) { db.setEvolvesFrom(pokemonNumber); }

        con = this;
        activity = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poke_details_tabs, menu);

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
            Intent intent = new Intent(con,PokeDetailsTabs.class);
            intent.putExtra("pokemon",pokePosition-1);
            con.startActivity(intent);
            finish();
        }

        if (id == R.id.action_next) {
            Intent intent = new Intent(con,PokeDetailsTabs.class);
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

    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu.findItem(id);
        item.setTitle(title);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    return PokemonTypeEffectivenessFragment.newInstance(position + 1);
                case 1:
                    return MainDetails.newInstance(position + 1);
                case 2:
                    return PokemonMoveFragment.newInstance(position + 1);
                case 3:
                    return PokemonMoveTMFragment.newInstance(position + 1);
                case 4:
                    return PokemonMoveTutorFragment.newInstance(position + 1);
                case 5:
                    return PokemonMoveEggFragment.newInstance(position + 1);
                default:
                    return MainDetails.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    public static class MainDetails extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static MainDetails newInstance(int sectionNumber) {
            MainDetails fragment = new MainDetails();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MainDetails() {
        }

        private String gifNum = "";
        private String gifMega = "";
        private String getGifFile() { return gifNum + gifMega + ".gif"; }

        private ArrayList<Ability> curAbilityList = new ArrayList<>();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poke_details_tabs, container, false);

            final GifImageView pokeSprite = (GifImageView) rootView.findViewById(R.id.pokeDetailsImage);

            final TextView pokeName = (TextView) rootView.findViewById(R.id.pokeDetailsName);
            final TextView pokeNum = (TextView) rootView.findViewById(R.id.pokeDetailsNum);
            final TextView pokeTypes = (TextView) rootView.findViewById(R.id.pokeDetailsTypes);
            final TextView pokeHeightWeight = (TextView) rootView.findViewById(R.id.pokeDetailsHeighWeight);
            final TextView pokeEggTypesList = (TextView) rootView.findViewById(R.id.pokeDetailsEggGroups);

            final TextView pokeHp = (TextView) rootView.findViewById(R.id.pokeDetailsHp);
            final TextView pokeAtk = (TextView) rootView.findViewById(R.id.pokeDetailsAtk);
            final TextView pokeDef = (TextView) rootView.findViewById(R.id.pokeDetailsDef);
            final TextView pokeSpAtk = (TextView) rootView.findViewById(R.id.pokeDetailsSpAtk);
            final TextView pokeSpDef = (TextView) rootView.findViewById(R.id.pokeDetailsSpDef);
            final TextView pokeSpd = (TextView) rootView.findViewById(R.id.pokeDetailsSpd);
            final TextView pokeTtl = (TextView)rootView.findViewById(R.id.pokeDetailsTotals);

            LinearLayout evoList = (LinearLayout) rootView.findViewById(R.id.pokeDetailsEvoList);
            final TextView abilitiesListText = (TextView) rootView.findViewById(R.id.pokeDetailsAbilities);

            final String subfolderNotShiny = "xy-animated";
            final String subfolderShiny = "xy-animated-shiny";
            gifNum = poke.getThreeDigitStringNumber();
            pokeSprite.setScaleType(ImageView.ScaleType.CENTER);
            pokeSprite.setScaleX(3.5f); pokeSprite.setScaleY(3.5f);
            pokeSprite.setClickable(true);
            pokeSprite.setTag("off");
            pokeSprite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag()=="off") {
                        try {
                            GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolderShiny + "/" + getGifFile());
                            pokeSprite.setImageDrawable(gifFromAssets);
                            v.setTag("on");
                        } catch (IOException e) {
                            Log.e("Error in gif loading: " + getGifFile(), e.toString());
                        }
                    } else {
                        try {
                            GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolderNotShiny + "/" + getGifFile());
                            pokeSprite.setImageDrawable(gifFromAssets);
                            v.setTag("off");
                        } catch (IOException e) {
                            Log.e("Error in gif loading: " + getGifFile(), e.toString());
                        }
                    }
                }
            });
            try {
                GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolderNotShiny + "/" + getGifFile());
                pokeSprite.setImageDrawable(gifFromAssets);
            } catch (IOException e) {
                Log.e("Error in gif loading: " + getGifFile(), e.toString());
            }

            String types = poke.getTypeOne();
            if (!poke.getTypeTwo().equals("")) { types = types + " | " + poke.getTypeTwo(); }

            String name = poke.getName();
            if (name.contains("-")) {
                if (!name.equals("Porygon-z")|!name.equals("Mime-jr")|!name.equals("Ho-oh")|!name.equals("Mr-mime")|!name.equals("Porygon-z")) {
                    name = name.split("-")[0];
                }
            }
            pokeName.setText(name);
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

            //Show evolves from if necessary -------------------------------------------------------
            if (!poke.getEvolvesFrom().equals("")) {
                TextView evoHeader = new TextView(con.getApplicationContext());
                evoHeader.setText("Evolves from:");
                evoHeader.setTextColor(Color.BLACK);
                evoList.addView(evoHeader);

                String num = String.valueOf(poke.getEvolvesFromNum());
                num = ("000" + num).substring(num.length());

                ImageView tempImage = new ImageView(con.getApplicationContext());
                try {
                    GifDrawable tempGifFromAssets = new GifDrawable(con.getAssets(), "xy-animated" + "/" + num + ".gif");
                    tempImage.setImageDrawable(tempGifFromAssets);
                    tempImage.setMinimumWidth(3);
                    tempImage.setMaxWidth(3);
                    tempImage.setMinimumHeight(3);
                    tempImage.setMaxHeight(3);
                    tempImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } catch (IOException e) {
                    Log.e("Error in gif loading (evolution): " + getGifFile(), e.toString());
                }

                String to = poke.getEvolvesFrom();
                if (to.contains("-")) {
                    if (!to.equals("Porygon-z")|!to.equals("Mime-jr")|!to.equals("Ho-oh")|!to.equals("Mr-mime")|!to.equals("Porygon-z")) {
                        to = to.split("-")[0];
                    }
                }
                String tempTextShow = num + " " + to;

                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(tempTextShow);
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.RIGHT);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                LinearLayout rowLayout = new LinearLayout(con.getApplicationContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.addView(tempImage);
                rowLayout.addView(tempText);

                rowLayout.setClickable(true);
                final int newPokePosition = Integer.parseInt(num) - 1;
                rowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(con,PokeDetailsTabs.class);
                        intent.putExtra("pokemon",newPokePosition);
                        con.startActivity(intent);
                        activity.finish();
                    }
                });

                evoList.addView(rowLayout);
            }

            //Show evolutions if necessary ---------------------------------------------------------
            ArrayList<Evolution> evolutionList = poke.getEvolutions();
            if (evolutionList.size() > 0) {
                TextView evoHeader = new TextView(con.getApplicationContext());
                evoHeader.setText("Evolves into:");
                evoHeader.setTextColor(Color.BLACK);
                evoList.addView(evoHeader);
            }
            Collections.sort(evolutionList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Evolution p1 = (Evolution) o1;
                    Evolution p2 = (Evolution) o2;
                    return p1.getNum().compareTo(p2.getNum());
                }
            });
            for (Evolution evoBundle : evolutionList) {
                int level = 0;
                String method = "";
                String detail = "";
                String to = evoBundle.getEvolvesTo();
                String num = evoBundle.getNum();
                num = ("000" + num).substring(num.length());
                if (evoBundle.getLevel() != 0) {
                    level = evoBundle.getLevel();
                }
                if (evoBundle.getMethod() != null) {
                    method = evoBundle.getMethod();
                }
                if (evoBundle.getDetail() != null) {
                    detail = evoBundle.getDetail();
                }

                String tempTextShow = num + " " + to + "\n Method: " + method;
                switch (method) {
                    case "level-up":
                        tempTextShow = num + " - " + to + "\n" + "Level: " + level;
                        break;
                    case "use-item":
                        tempTextShow = num + " - " + to + "\n" + "With item " + detail;
                        break;
                    case "trade":
                        tempTextShow = num + " - " + to + "\n" + "Trade " + detail;
                        break;
                    case "shed":
                        tempTextShow = num + " - " + to + "\n" + "Shed " + detail;
                        break;
                }

                ImageView tempImage = new ImageView(con.getApplicationContext());
                try {
                    GifDrawable tempGifFromAssets = new GifDrawable(con.getAssets(), "xy-animated" + "/" + num + ".gif");
                    tempImage.setImageDrawable(tempGifFromAssets);
                    tempImage.setMinimumWidth(3);
                    tempImage.setMaxWidth(3);
                    tempImage.setMinimumHeight(3);
                    tempImage.setMaxHeight(3);
                    tempImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } catch (IOException e) {
                    Log.e("Error in gif loading (evolution): " + getGifFile(), e.toString());
                }

                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(tempTextShow);
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.RIGHT);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                LinearLayout rowLayout = new LinearLayout(con.getApplicationContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.addView(tempImage);
                rowLayout.addView(tempText);

                rowLayout.setClickable(true);
                final int newPokePosition = Integer.parseInt(num) - 1;
                rowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(con,PokeDetailsTabs.class);
                        intent.putExtra("pokemon",newPokePosition);
                        con.startActivity(intent);
                        activity.finish();
                    }
                });

                evoList.addView(rowLayout);
            }

            //Show forms if necessary --------------------------------------------------------------
            ArrayList<Evolution> formsList = poke.getForms();
            if (formsList.size() > 0) {
                TextView evoHeader = new TextView(con.getApplicationContext());
                evoHeader.setText("Forms:");
                evoHeader.setTextColor(Color.BLACK);
                evoList.addView(evoHeader);
            }
            Collections.sort(formsList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Evolution p1 = (Evolution) o1;
                    Evolution p2 = (Evolution) o2;
                    return p1.getNum().compareTo(p2.getNum());
                }
            });
            for (final Evolution form : formsList) {
                String method = "";
                String detail = "";
                String mega;
                String to = form.getEvolvesTo();
                String num = poke.getThreeDigitStringNumber();
                if (form.getMethod() != null) {
                    method = form.getMethod();
                }
                if (form.getDetail() != null) {
                    detail = form.getDetail();
                }

                String tempTextShow;
                if (detail.equals("mega")) {
                    //get substring at the end of to field
                    mega = to.substring(poke.getName().length());
                    //crop the extra off the name
                    to = to.substring(0,poke.getName().length());
                    to = to.substring(0, 1).toUpperCase() + to.substring(1);
                    method = method.substring(0, 1).toUpperCase() + method.substring(1);
                    tempTextShow = method + " " + to;
                } else {
                    mega = "-" + to.split("-")[1];
                    to = to.split("-")[0];
                    to = to.substring(0, 1).toUpperCase() + to.substring(1);
                    method = method.substring(0, 1).toUpperCase() + method.substring(1);
                    tempTextShow = to + "\n" + method;
                }

                ImageView tempImage = new ImageView(con.getApplicationContext());
                try {
                    GifDrawable tempGifFromAssets = new GifDrawable(con.getAssets(), "xy-animated" + "/" + num + mega + ".gif");
                    tempImage.setImageDrawable(tempGifFromAssets);
                    tempImage.setMinimumWidth(3);
                    tempImage.setMaxWidth(3);
                    tempImage.setMinimumHeight(3);
                    tempImage.setMaxHeight(3);
                    tempImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                } catch (IOException e) {
                    Log.e("Error in gif loading (evolution): " + getGifFile(), e.toString());
                }

                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(tempTextShow);
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.RIGHT);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                LinearLayout rowLayout = new LinearLayout(con.getApplicationContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.addView(tempImage);
                rowLayout.addView(tempText);

                //Show form on click
                rowLayout.setClickable(true);
                rowLayout.setTag("off");
                rowLayout.setOnClickListener(new View.OnClickListener() {
                    ArrayList<Ability> abilities;
                    int[] stats;
                    int total;
                    String types;
                    String pokemonHeight;
                    String pokemonWeight;
                    @Override
                    public void onClick(View v) {
                        View vp = (View) v.getParent();
                        for(int i=0; i<((ViewGroup)vp).getChildCount(); ++i) {
                            View nextChild = ((ViewGroup)vp).getChildAt(i);
                            if (nextChild != v) {
                                nextChild.setBackgroundColor(0x00000000);
                                nextChild.setTag("off");
                            }
                        }
                        if (!v.getTag().equals("on")) {
                            v.setBackgroundColor(getResources().getColor(R.color.evo_selected));
                            v.setTag("on");
                            //Change image to mega image - some forms don't have shiny gifs
                            if (form.getDetail().equals("mega")) {
                                gifMega = form.getEvolvesTo().substring(poke.getName().length());
                            } else {
                                gifMega = form.getEvolvesTo().substring(poke.getName().split("-")[0].length());
                            }
                            //Change abilities to ability for that form
                            abilities = db.getAbilities(form.getLevel()); //get level returns the form id number
                            //Change stats
                            stats = db.getStats(form.getLevel());
                            total = stats[0] + stats[1] + stats[2] + stats[3] + stats[4] + stats[5];
                            //Change types
                            types = db.getPokemonTypesStringFromId(form.getLevel());
                            //Change height and weight
                            String[] heightWeight = db.getPokemonHeightWeight(form.getLevel());
                            pokemonHeight = heightWeight[0];
                            pokemonWeight = heightWeight[1];
                        } else {
                            v.setTag("off");
                            v.setBackgroundColor(0x00000000);
                            gifMega = "";
                            abilities = poke.getAbilities();
                            stats = poke.getStats();
                            total = stats[0] + stats[1] + stats[2] + stats[3] + stats[4] + stats[5];
                            types = poke.getTypeOne();
                            if (!poke.getTypeTwo().equals("")) { types = types + " | " + poke.getTypeTwo(); }
                            pokemonHeight = poke.getHeight();
                            pokemonWeight = poke.getWeight();
                        }

                        //Set picture
                        try {
                            GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolderNotShiny + "/" + getGifFile());
                            pokeSprite.setImageDrawable(gifFromAssets);
                            pokeSprite.setTag("off");
                        } catch (IOException e) {
                            Log.e("Error in gif loading: " + getGifFile(), e.toString());
                        }
                        //Set abilities
                        curAbilityList = abilities;
                        String abilitiesText = "Abilities: ";
                        String middleSepText = "";
                        boolean shouldSnip = false;
                        if (abilities.size() > 1) { middleSepText = ", "; shouldSnip = true; }
                        for (Ability abil : abilities) {
                            abilitiesText = abilitiesText + " " + abil.getAbilityName() + middleSepText;
                        }
                        if (shouldSnip) abilitiesText = abilitiesText.substring(0,abilitiesText.length()-2);
                        abilitiesListText.setText(abilitiesText);
                        //Set stats
                        pokeHp.setText(String.format("%-10s %3d", "HP:", stats[0]));
                        pokeAtk.setText(String.format("%-10s %3d","Attack:",stats[1]));
                        pokeDef.setText(String.format("%-10s %3d","Defense:",stats[2]));
                        pokeSpAtk.setText(String.format("%-10s %3d","Sp. Atk:",stats[3]));
                        pokeSpDef.setText(String.format("%-10s %3d","Sp. Def:",stats[4]));
                        pokeSpd.setText(String.format("%-10s %3d","Speed:",stats[5]));
                        pokeTtl.setText(String.format("%-10s %3d","Total:",total));
                        //Set types
                        pokeTypes.setText(types);
                        //Set height and weight
                        if (pokemonHeight.length() < 2) { pokemonHeight = ("00" + pokemonHeight).substring(pokemonHeight.length()); }
                        pokemonHeight = new StringBuilder(pokemonHeight).insert(pokemonHeight.length()-1, ".").toString();
                        if (pokemonWeight.length() < 2) { pokemonWeight = ("00" + pokemonWeight).substring(pokemonWeight.length()); }
                        pokemonWeight = new StringBuilder(pokemonWeight).insert(pokemonWeight.length()-1, ".").toString();
                        pokeHeightWeight.setText("Height: " + pokemonHeight + " m" + "\n" + "Weight: " + pokemonWeight + " kg");
                    }
                });


                evoList.addView(rowLayout);
            }

            ArrayList<Ability> abilities = poke.getAbilities();
            curAbilityList = abilities;
            String abilitiesText = "Abilities: ";
            String middleSepText = "";
            boolean shouldSnip = false;
            if (abilities.size() > 1) { middleSepText = ", "; shouldSnip = true; }
            for (Ability abil : abilities) {
                abilitiesText = abilitiesText + " " + abil.getAbilityName() + middleSepText;
            }
            if (shouldSnip) abilitiesText = abilitiesText.substring(0,abilitiesText.length()-2);
            abilitiesListText.setText(abilitiesText);

            abilitiesListText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = (Activity) con;
                    android.app.FragmentManager fm = activity.getFragmentManager();
                    AbilityDialogFragment abilityDialogFragment = new AbilityDialogFragment();
                    Bundle abilityResources = new Bundle();
                    int z = 0;
                    abilityResources.putInt("total", curAbilityList.size());
                    for (Ability abil : curAbilityList) {
                        abilityResources.putInt("ability" + z,abil.getResource());
                        z++;
                    }
                    abilityDialogFragment.setArguments(abilityResources);
                    abilityDialogFragment.show(fm,"abilityDialog");
                }
            });

            ArrayList<EggGroup> eggTypes = poke.getEggTypes();
            String eggTypesText = "Egg types: ";
            middleSepText = "";
            shouldSnip = false;
            if (eggTypes.size() > 1) { middleSepText = ", "; shouldSnip = true; }
            for (EggGroup eggTy : eggTypes) {
                eggTypesText = eggTypesText + " " + eggTy.getEggGroupName() + middleSepText;
            }
            if (shouldSnip) eggTypesText = eggTypesText.substring(0,eggTypesText.length()-2);
            pokeEggTypesList.setText(eggTypesText);

            return rootView;

        }
    }

    public static class PokemonMoveFragment extends Fragment implements SearchView.OnQueryTextListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PokemonMoveFragment newInstance(int sectionNumber) {
            PokemonMoveFragment fragment = new PokemonMoveFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokemonMoveFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poke_details_tabs_moves, container, false);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchViewMoves);
            ListView l1=(ListView)rootView.findViewById(R.id.pokeMoveList);
            TextView header = (TextView)rootView.findViewById(R.id.moveListHeader);
            header.setText("MOVES: LEVEL UP");
            ArrayList<Move> movesList = poke.getMoveset();
            ArrayList<Move> showMovesList = new ArrayList<>();
            for (Move move : movesList) {
                if (move.getLearnMethod() == 1) {
                    showMovesList.add(move);
                }
            }

            Collections.sort(showMovesList, new Comparator<Move>() {
                @Override public int compare(Move p1, Move p2) {
                    return p1.getLevel()- p2.getLevel();
                }
            });

            moveListAdapter = new MoveListAdapter(con,showMovesList);
            l1.setAdapter(moveListAdapter);
            searchView.setOnQueryTextListener(this);
            return rootView;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            moveListAdapter.getFilter().filter(newText);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    }

    public static class PokemonMoveTMFragment extends Fragment implements SearchView.OnQueryTextListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PokemonMoveTMFragment newInstance(int sectionNumber) {
            PokemonMoveTMFragment fragment = new PokemonMoveTMFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokemonMoveTMFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poke_details_tabs_moves, container, false);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchViewMoves);
            ListView l1=(ListView)rootView.findViewById(R.id.pokeMoveList);
            TextView header = (TextView)rootView.findViewById(R.id.moveListHeader);
            header.setText("MOVES: TM");
            ArrayList<Move> movesList = poke.getMoveset();
            ArrayList<Move> showMovesList = new ArrayList<>();
            for (Move move : movesList) {
                if (move.getLearnMethod() == 4) {
                    showMovesList.add(move);
                }
            }

            Collections.sort(showMovesList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Move p1 = (Move) o1;
                    Move p2 = (Move) o2;
                    return p1.getMoveName().compareTo(p2.getMoveName());
                }
            });

            moveListAdapter = new MoveListAdapter(con,showMovesList);
            l1.setAdapter(moveListAdapter);
            searchView.setOnQueryTextListener(this);
            return rootView;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            moveListAdapter.getFilter().filter(newText);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    }

    public static class PokemonMoveTutorFragment extends Fragment implements SearchView.OnQueryTextListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PokemonMoveTutorFragment newInstance(int sectionNumber) {
            PokemonMoveTutorFragment fragment = new PokemonMoveTutorFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokemonMoveTutorFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poke_details_tabs_moves, container, false);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchViewMoves);
            ListView l1=(ListView)rootView.findViewById(R.id.pokeMoveList);
            TextView header = (TextView)rootView.findViewById(R.id.moveListHeader);
            header.setText("MOVES: TUTOR");
            ArrayList<Move> movesList = poke.getMoveset();
            ArrayList<Move> showMovesList = new ArrayList<>();
            for (Move move : movesList) {
                if (move.getLearnMethod() == 3) {
                    showMovesList.add(move);
                }
            }

            Collections.sort(showMovesList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Move p1 = (Move) o1;
                    Move p2 = (Move) o2;
                    return p1.getMoveName().compareTo(p2.getMoveName());
                }
            });

            moveListAdapter = new MoveListAdapter(con,showMovesList);
            l1.setAdapter(moveListAdapter);
            searchView.setOnQueryTextListener(this);
            return rootView;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            moveListAdapter.getFilter().filter(newText);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    }

    public static class PokemonMoveEggFragment extends Fragment implements SearchView.OnQueryTextListener {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PokemonMoveEggFragment newInstance(int sectionNumber) {
            PokemonMoveEggFragment fragment = new PokemonMoveEggFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokemonMoveEggFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poke_details_tabs_moves, container, false);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchViewMoves);
            ListView l1=(ListView)rootView.findViewById(R.id.pokeMoveList);
            TextView header = (TextView)rootView.findViewById(R.id.moveListHeader);
            header.setText("MOVES: EGG");
            ArrayList<Move> movesList = poke.getMoveset();
            ArrayList<Move> showMovesList = new ArrayList<>();
            for (Move move : movesList) {
                if (move.getLearnMethod() == 2) {
                    showMovesList.add(move);
                }
            }

            Collections.sort(showMovesList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Move p1 = (Move) o1;
                    Move p2 = (Move) o2;
                    return p1.getMoveName().compareTo(p2.getMoveName());
                }
            });

            moveListAdapter = new MoveListAdapter(con,showMovesList);
            l1.setAdapter(moveListAdapter);
            searchView.setOnQueryTextListener(this);
            return rootView;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            moveListAdapter.getFilter().filter(newText);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    }

    public static class PokemonTypeEffectivenessFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PokemonTypeEffectivenessFragment newInstance(int sectionNumber) {
            PokemonTypeEffectivenessFragment fragment = new PokemonTypeEffectivenessFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokemonTypeEffectivenessFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poke_details_tabs_type_effectiveness, container, false);
            LinearLayout offensiveSuper = (LinearLayout) rootView.findViewById(R.id.type_supereffective);
            LinearLayout offensiveNotvery = (LinearLayout) rootView.findViewById(R.id.type_notveryeffective);
            LinearLayout offensiveDoesnot = (LinearLayout) rootView.findViewById(R.id.type_doesnoteffect);
            LinearLayout defensiveSuper = (LinearLayout) rootView.findViewById(R.id.type_def_supereffective);
            LinearLayout defensiveNotvery = (LinearLayout) rootView.findViewById(R.id.type_def_notveryeffective);
            LinearLayout defensiveDoesnot = (LinearLayout) rootView.findViewById(R.id.type_def_doesnoteffect);

            String typeOne = poke.getTypeOne();
            String typeTwo = poke.getTypeTwo();
            typeOne = typeOne.substring(0,1).toUpperCase() + typeOne.substring(1).toLowerCase();
            if (!typeTwo.equals("")) {
                typeTwo = typeTwo.substring(0, 1).toUpperCase() + typeTwo.substring(1).toLowerCase();
            }

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

            return rootView;
        }
    }

}
