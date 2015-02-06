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
    private static Context c;
    private static Activity activity;

    private static MoveListAdapter moveListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_details_tabs);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        Intent intent = getIntent();
        pokePosition = intent.getIntExtra("pokemon", 0);
        poke = MainActivity.pokemonList.get(pokePosition);

        final Context c = MainActivity.c;
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return MainDetails.newInstance(position + 1);

            switch (position) {
                case 0:
                    return MainDetails.newInstance(position + 1);
                case 1:
                    return PokemonMoveFragment.newInstance(position + 1);
                case 2:
                    return PokemonMoveTMFragment.newInstance(position + 1);
                case 3:
                    return PokemonMoveTutorFragment.newInstance(position + 1);
                default:
                    return MainDetails.newInstance(position + 1);

                /*
                case 2:
                    fragment = PokedexCaughtFragment.newInstance(position + 1);
                    break;
                case 3:
                    fragment = LivingDexMissingFragment.newInstance(position + 1);
                    break;
                case 4:
                    fragment = LivingDexCaughtFragment.newInstance(position + 1);
                    break;
                case 5:
                    fragment = MyTeamFragment.newInstance(position + 1);
                    break;
                */
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_poke_details_tabs, container, false);

            final GifImageView pokeSprite = (GifImageView) rootView.findViewById(R.id.pokeDetailsImage);

            TextView pokeName = (TextView) rootView.findViewById(R.id.pokeDetailsName);
            TextView pokeNum = (TextView) rootView.findViewById(R.id.pokeDetailsNum);
            TextView pokeTypes = (TextView) rootView.findViewById(R.id.pokeDetailsTypes);
            TextView pokeHeightWeight = (TextView) rootView.findViewById(R.id.pokeDetailsHeighWeight);
            TextView pokeEggTypesList = (TextView) rootView.findViewById(R.id.pokeDetailsEggGroups);

            TextView pokeHp = (TextView) rootView.findViewById(R.id.pokeDetailsHp);
            TextView pokeAtk = (TextView) rootView.findViewById(R.id.pokeDetailsAtk);
            TextView pokeDef = (TextView) rootView.findViewById(R.id.pokeDetailsDef);
            TextView pokeSpAtk = (TextView) rootView.findViewById(R.id.pokeDetailsSpAtk);
            TextView pokeSpDef = (TextView) rootView.findViewById(R.id.pokeDetailsSpDef);
            TextView pokeSpd = (TextView) rootView.findViewById(R.id.pokeDetailsSpd);
            TextView pokeTtl = (TextView)rootView.findViewById(R.id.pokeDetailsTotals);

            LinearLayout evoList = (LinearLayout) rootView.findViewById(R.id.pokeDetailsEvoList);
            TextView abilitiesListText = (TextView) rootView.findViewById(R.id.pokeDetailsAbilities);

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
                            GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolder + "/" + gifId);
                            pokeSprite.setImageDrawable(gifFromAssets);
                        } catch (IOException e) {
                            Log.e("Error in gif loading: " + gifId, e.toString());
                        }
                    } else {
                        subfolder = subfolderNotShiny;
                        try {
                            GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolderNotShiny + "/" + gifId);
                            pokeSprite.setImageDrawable(gifFromAssets);
                        } catch (IOException e) {
                            Log.e("Error in gif loading: " + gifId, e.toString());
                        }
                    }
                }
            });
            try {
                GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolderNotShiny + "/" + gifId);
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
                TextView evoHeader = new TextView(con.getApplicationContext());
                evoHeader.setText("Evolves from:");
                evoHeader.setTextColor(Color.BLACK);
                evoList.addView(evoHeader);

                String to = poke.getEvolvesFrom();
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
                    Log.e("Error in gif loading (evolution): " + gifId, e.toString());
                }

                String tempTextShow = num + " " + to;

                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(tempTextShow);
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.RIGHT);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

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

            ArrayList<Bundle> evolutionList = poke.getEvolutions();
            if (evolutionList.size() > 0) {
                TextView evoHeader = new TextView(con.getApplicationContext());
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
                    Log.e("Error in gif loading (evolution): " + gifId, e.toString());
                }

                String tempTextShow = num + " " + to + " Method: " + method;
                if (method.equals("level_up")) { tempTextShow = num + " - " + to + "\n" + "Level: " + level; }
                if (method.equals("stone")) { tempTextShow = num + " - " + to + "\n" + "With stone " + detail; }
                if (method.equals("other")) { tempTextShow = num + " - " + to + "\n" + "Other " + detail; }

                TextView tempText = new TextView(con.getApplicationContext());
                tempText.setText(tempTextShow);
                tempText.setTextColor(Color.BLACK);
                tempText.setGravity(Gravity.RIGHT);
                tempText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                LinearLayout rowLayout = new LinearLayout(con.getApplicationContext());
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
                            Intent intent = new Intent(con,PokeDetailsTabs.class);
                            intent.putExtra("pokemon",newPokePosition);
                            con.startActivity(intent);
                            activity.finish();
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

            return rootView;

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PokemonMoveFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            ArrayList<Bundle> movesList = poke.getMoveset();
            ArrayList<Bundle> showMovesList = new ArrayList<Bundle>();
            for (Bundle bundle : movesList) {
                if (bundle.getString("learn").equals("level up")) {
                    showMovesList.add(bundle);
                }
            }

            Collections.sort(showMovesList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Bundle p1 = (Bundle) o1;
                    Bundle p2 = (Bundle) o2;
                    return String.valueOf(p1.getInt("level")).compareTo(String.valueOf(p2.getInt("level")));
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PokemonMoveTMFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            ArrayList<Bundle> movesList = poke.getMoveset();
            ArrayList<Bundle> showMovesList = new ArrayList<Bundle>();
            for (Bundle bundle : movesList) {
                if (bundle.getString("learn").equals("machine")) {
                    showMovesList.add(bundle);
                }
            }

            Collections.sort(showMovesList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Bundle p1 = (Bundle) o1;
                    Bundle p2 = (Bundle) o2;
                    return p1.getString("name").compareTo(p2.getString("name"));
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PokemonMoveTutorFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            ArrayList<Bundle> movesList = poke.getMoveset();
            ArrayList<Bundle> showMovesList = new ArrayList<Bundle>();
            for (Bundle bundle : movesList) {
                if (bundle.getString("learn").equals("tutor")) {
                    showMovesList.add(bundle);
                }
            }

            Collections.sort(showMovesList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Bundle p1 = (Bundle) o1;
                    Bundle p2 = (Bundle) o2;
                    return p1.getString("name").compareTo(p2.getString("name"));
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

}
