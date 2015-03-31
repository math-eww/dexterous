package info.mattsaunders.apps.dexterous;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.winsontan520.wversionmanager.library.OnReceiveListener;
import com.winsontan520.wversionmanager.library.WVersionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public static Context c;
    public static DexListAdapter dexAdapter;
    public static Bundle pokeballTog1States;
    public static Bundle pokeballTog2States;
    public static Bundle pokeballTog3States;
    public static int caughtDex = 0;
    public static int livingDex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        c = this;

        if (Global.pokemonList.size() < Global.TOTAL_POKES) {
            Global.db = new PokedexDatabase(this);
            //Load 3 bundles with pokemon pokeball toggles
            Utilities.FILENAME = "pokeball_1_states";
            pokeballTog1States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
            Utilities.FILENAME = "pokeball_2_states";
            pokeballTog2States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
            Utilities.FILENAME = "pokeball_3_states";
            pokeballTog3States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());

            //Check if pokemon files already downloaded
            if (!Utilities.readSettingsFile().equals("1")) {
                new LoadData.CallAPI(true).execute(pokeballTog1States, pokeballTog2States, pokeballTog3States);
                new DownloadSprites.CallAPI().execute();
                Utilities.writeSettingsFile("1");
            } else {
                new LoadData.CallAPI(false).execute(pokeballTog1States, pokeballTog2States, pokeballTog3States);
            }
        }

        //Check version against server to see if there is an update available'
        WVersionManager versionManager = new WVersionManager(this);
        versionManager.setUpdateNowLabel("Download");
        versionManager.setRemindMeLaterLabel("Later");
        versionManager.setIgnoreThisVersionLabel("Skip");
        versionManager.setUpdateUrl("http://mattsaunders.info/dexterous/download/Dexterous.apk"); // this is the link will execute when update now clicked. default will go to google play based on your package name.
        //versionManager.setReminderTimer(10); // this mean checkVersion() will not take effect within 10 minutes
        versionManager.setVersionContentUrl("http://mattsaunders.info/dexterous/download/version.json");
        versionManager.setOnReceiveListener(new OnReceiveListener() {
            @Override
            public boolean onReceive(int status, String result) {
                return true; // return true if you want to use library's default logic & dialog
            }
        });
        versionManager.checkVersion();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onPause() {
        //Save 3 bundles with pokemon pokeball toggles
        pokeballTog1States = new Bundle();
        pokeballTog2States = new Bundle();
        pokeballTog3States = new Bundle();
        for (Pokemon poke : Global.pokemonList) {
            if (poke.getPokeballToggle1()) {
                pokeballTog1States.putInt(poke.getStringNumber(),1);
            } else {
                pokeballTog1States.putInt(poke.getStringNumber(),0);
            }
            if (poke.getPokeballToggle2()) {
                pokeballTog2States.putInt(poke.getStringNumber(),1);
            } else {
                pokeballTog2States.putInt(poke.getStringNumber(),0);
            }
            if (poke.getPokeballToggle3()) {
                pokeballTog3States.putInt(poke.getStringNumber(),1);
            } else {
                pokeballTog3States.putInt(poke.getStringNumber(),0);
            }
        }
        Utilities.SUBDIR = "";
        Utilities.FILENAME = "pokeball_1_states";
        Utilities.writeJsonFile(Utilities.bundleToJsonObject(pokeballTog1States));
        Utilities.FILENAME = "pokeball_2_states";
        Utilities.writeJsonFile(Utilities.bundleToJsonObject(pokeballTog2States));
        Utilities.FILENAME = "pokeball_3_states";
        Utilities.writeJsonFile(Utilities.bundleToJsonObject(pokeballTog3States));
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Load 3 bundles with pokemon pokeball toggles
        Utilities.SUBDIR = "";
        Utilities.FILENAME = "pokeball_1_states";
        pokeballTog1States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
        Utilities.FILENAME = "pokeball_2_states";
        pokeballTog2States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
        Utilities.FILENAME = "pokeball_3_states";
        pokeballTog3States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = PokedexMainFragment.newInstance(position + 1);
        //Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = PokedexMainFragment.newInstance(position + 1);
                break;
            case 1:
                fragment = PokedexMissingFragment.newInstance(position + 1);
                break;
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
            case 6:
                fragment = MovesFragment.newInstance(position + 1);
                break;
            case 7:
                fragment = PokemonByTypeFragment.newInstance(position + 1);
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                break;
            case 8:
                mTitle = getString(R.string.title_section8);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_openBreedingTools) {
            Intent intent = new Intent(this, BreedingToolsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_openBattleTools) {
            Intent intent = new Intent(this, BattleToolsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_openTeamBuilder) {
            Intent intent = new Intent(this, TeamBuilder.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static int getCaughtDex() { return caughtDex; }
    public static int getLivingDex() { return livingDex; }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PokedexMainFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayList<Pokemon> fragPokeList;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PokedexMainFragment newInstance(int sectionNumber) {
            PokedexMainFragment fragment = new PokedexMainFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokedexMainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
            final ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            fragPokeList = new ArrayList<>();
            Global.curPokeList.clear();
            for (Pokemon poke : Global.pokemonList) {
                fragPokeList.add(poke);
                Global.curPokeList.add(poke.getNumber() - 1);
            }
            dexAdapter = new DexListAdapter(c,fragPokeList);
            l1.setAdapter(dexAdapter);
            searchView.setOnQueryTextListener(this);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            ArrayAdapter<String> sortOptions = new ArrayAdapter<> (c,
                    android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
                    "Number", "Name", "Type One", "Type Two" } );
            spinner.setAdapter(sortOptions);
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (fragPokeList.size() < Global.pokemonList.size()) {
                        for (Pokemon poke : Global.pokemonList) { fragPokeList.add(poke); }
                    }
                    switch (parent.getItemAtPosition(position).toString()) {
                        case "Number":
                            Collections.sort(fragPokeList, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getThreeDigitStringNumber().compareTo(p2.getThreeDigitStringNumber());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,fragPokeList);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Name":
                            Collections.sort(fragPokeList, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getName().compareTo(p2.getName());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,fragPokeList);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type One":
                            Collections.sort(fragPokeList, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeOne().compareTo(p2.getTypeOne());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,fragPokeList);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type Two":
                            Collections.sort(fragPokeList, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeTwo().compareTo(p2.getTypeTwo());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,fragPokeList);
                            l1.setAdapter(dexAdapter);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            dexAdapter.getFilter().filter(newText);
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
    public static class PokedexMissingFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayList<Pokemon> pokemonListMissing;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PokedexMissingFragment newInstance(int sectionNumber) {
            PokedexMissingFragment fragment = new PokedexMissingFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokedexMissingFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            final ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            pokemonListMissing = new ArrayList<>();
            Global.curPokeList.clear();
            for (Pokemon poke : Global.pokemonList) {
                if (!poke.getPokeballToggle1()) {
                    pokemonListMissing.add(poke);
                    Global.curPokeList.add(poke.getNumber() - 1);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListMissing);
            l1.setAdapter(dexAdapter);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(this);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            ArrayAdapter<String> sortOptions = new ArrayAdapter<> (c,
                    android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
                    "Number", "Name", "Type One", "Type Two" } );
            spinner.setAdapter(sortOptions);
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getItemAtPosition(position).toString()) {
                        case "Number":
                            Collections.sort(pokemonListMissing, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getThreeDigitStringNumber().compareTo(p2.getThreeDigitStringNumber());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListMissing);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Name":
                            Collections.sort(pokemonListMissing, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getName().compareTo(p2.getName());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListMissing);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type One":
                            Collections.sort(pokemonListMissing, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeOne().compareTo(p2.getTypeOne());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListMissing);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type Two":
                            Collections.sort(pokemonListMissing, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeTwo().compareTo(p2.getTypeTwo());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListMissing);
                            l1.setAdapter(dexAdapter);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            dexAdapter.getFilter().filter(newText);
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
    public static class PokedexCaughtFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayList<Pokemon> pokemonListCaught;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PokedexCaughtFragment newInstance(int sectionNumber) {
            PokedexCaughtFragment fragment = new PokedexCaughtFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokedexCaughtFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            final ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            pokemonListCaught = new ArrayList<>();
            Global.curPokeList.clear();
            for (Pokemon poke : Global.pokemonList) {
                if (poke.getPokeballToggle1()) {
                    pokemonListCaught.add(poke);
                    Global.curPokeList.add(poke.getNumber() - 1);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListCaught);
            l1.setAdapter(dexAdapter);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(this);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            ArrayAdapter<String> sortOptions = new ArrayAdapter<> (c,
                    android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
                    "Number", "Name", "Type One", "Type Two" } );
            spinner.setAdapter(sortOptions);
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getItemAtPosition(position).toString()) {
                        case "Number":
                            Collections.sort(pokemonListCaught, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getThreeDigitStringNumber().compareTo(p2.getThreeDigitStringNumber());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListCaught);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Name":
                            Collections.sort(pokemonListCaught, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getName().compareTo(p2.getName());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListCaught);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type One":
                            Collections.sort(pokemonListCaught, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeOne().compareTo(p2.getTypeOne());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListCaught);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type Two":
                            Collections.sort(pokemonListCaught, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeTwo().compareTo(p2.getTypeTwo());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListCaught);
                            l1.setAdapter(dexAdapter);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            dexAdapter.getFilter().filter(newText);
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
    public static class LivingDexMissingFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayList<Pokemon> pokemonListLDMissing;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LivingDexMissingFragment newInstance(int sectionNumber) {
            LivingDexMissingFragment fragment = new LivingDexMissingFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public LivingDexMissingFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            final ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            pokemonListLDMissing = new ArrayList<>();
            Global.curPokeList.clear();
            for (Pokemon poke : Global.pokemonList) {
                if (!poke.getPokeballToggle2()) {
                    pokemonListLDMissing.add(poke);
                    Global.curPokeList.add(poke.getNumber() - 1);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListLDMissing);
            l1.setAdapter(dexAdapter);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(this);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            ArrayAdapter<String> sortOptions = new ArrayAdapter<> (c,
                    android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
                    "Number", "Name", "Type One", "Type Two" } );
            spinner.setAdapter(sortOptions);
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getItemAtPosition(position).toString()) {
                        case "Number":
                            Collections.sort(pokemonListLDMissing, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getThreeDigitStringNumber().compareTo(p2.getThreeDigitStringNumber());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListLDMissing);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Name":
                            Collections.sort(pokemonListLDMissing, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getName().compareTo(p2.getName());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListLDMissing);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type One":
                            Collections.sort(pokemonListLDMissing, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeOne().compareTo(p2.getTypeOne());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListLDMissing);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type Two":
                            Collections.sort(pokemonListLDMissing, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeTwo().compareTo(p2.getTypeTwo());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListLDMissing);
                            l1.setAdapter(dexAdapter);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            dexAdapter.getFilter().filter(newText);
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
    public static class LivingDexCaughtFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayList<Pokemon> pokemonListLDCaught;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LivingDexCaughtFragment newInstance(int sectionNumber) {
            LivingDexCaughtFragment fragment = new LivingDexCaughtFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public LivingDexCaughtFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            final ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            pokemonListLDCaught = new ArrayList<>();
            Global.curPokeList.clear();
            for (Pokemon poke : Global.pokemonList) {
                if (poke.getPokeballToggle2()) {
                    pokemonListLDCaught.add(poke);
                    Global.curPokeList.add(poke.getNumber() - 1);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListLDCaught);
            l1.setAdapter(dexAdapter);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(this);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            ArrayAdapter<String> sortOptions = new ArrayAdapter<> (c,
                    android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
                    "Number", "Name", "Type One", "Type Two" } );
            spinner.setAdapter(sortOptions);
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getItemAtPosition(position).toString()) {
                        case "Number":
                            Collections.sort(pokemonListLDCaught, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getThreeDigitStringNumber().compareTo(p2.getThreeDigitStringNumber());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListLDCaught);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Name":
                            Collections.sort(pokemonListLDCaught, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getName().compareTo(p2.getName());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListLDCaught);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type One":
                            Collections.sort(pokemonListLDCaught, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeOne().compareTo(p2.getTypeOne());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListLDCaught);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type Two":
                            Collections.sort(pokemonListLDCaught, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeTwo().compareTo(p2.getTypeTwo());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListLDCaught);
                            l1.setAdapter(dexAdapter);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            dexAdapter.getFilter().filter(newText);
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
    public static class MyTeamFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayList<Pokemon> pokemonListTeam;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MyTeamFragment newInstance(int sectionNumber) {
            MyTeamFragment fragment = new MyTeamFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MyTeamFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            final ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            pokemonListTeam = new ArrayList<>();
            Global.curPokeList.clear();
            for (Pokemon poke : Global.pokemonList) {
                if (poke.getPokeballToggle3()) {
                    pokemonListTeam.add(poke);
                    Global.curPokeList.add(poke.getNumber() - 1);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListTeam);
            l1.setAdapter(dexAdapter);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(this);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            ArrayAdapter<String> sortOptions = new ArrayAdapter<> (c,
                    android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
                    "Number", "Name", "Type One", "Type Two" } );
            spinner.setAdapter(sortOptions);
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getItemAtPosition(position).toString()) {
                        case "Number":
                            Collections.sort(pokemonListTeam, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getThreeDigitStringNumber().compareTo(p2.getThreeDigitStringNumber());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListTeam);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Name":
                            Collections.sort(pokemonListTeam, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getName().compareTo(p2.getName());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListTeam);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type One":
                            Collections.sort(pokemonListTeam, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeOne().compareTo(p2.getTypeOne());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListTeam);
                            l1.setAdapter(dexAdapter);
                            break;
                        case "Type Two":
                            Collections.sort(pokemonListTeam, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Pokemon p1 = (Pokemon) o1;
                                    Pokemon p2 = (Pokemon) o2;
                                    return p1.getTypeTwo().compareTo(p2.getTypeTwo());
                                }
                            });
                            dexAdapter = new DexListAdapter(c,pokemonListTeam);
                            l1.setAdapter(dexAdapter);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            dexAdapter.getFilter().filter(newText);
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
    public static class PokemonByTypeFragment extends Fragment implements SearchView.OnQueryTextListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private ArrayList<Pokemon> fragPokeList;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PokemonByTypeFragment newInstance(int sectionNumber) {
            PokemonByTypeFragment fragment = new PokemonByTypeFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PokemonByTypeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
            final ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            fragPokeList = new ArrayList<>();
            Global.curPokeList.clear();
            for (Pokemon poke : Global.pokemonList) {
                fragPokeList.add(poke);
                Global.curPokeList.add(poke.getNumber() - 1);
            }
            dexAdapter = new DexListAdapter(c,fragPokeList);
            l1.setAdapter(dexAdapter);
            searchView.setOnQueryTextListener(this);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            ArrayAdapter<String> sortOptions = new ArrayAdapter<> (c,
                    android.R.layout.simple_list_item_1, android.R.id.text1, TypeEffectiveness.typeNamesList );
            spinner.setAdapter(sortOptions);
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedType = parent.getItemAtPosition(position).toString();
                    fragPokeList.clear();
                    for (Pokemon poke : Global.pokemonList) {
                        if (poke.getTypeOne().equalsIgnoreCase(selectedType)) { fragPokeList.add(poke); }
                        if (poke.getTypeTwo().equalsIgnoreCase(selectedType)) { fragPokeList.add(poke); }
                    }
                    dexAdapter = new DexListAdapter(c,fragPokeList);
                    l1.setAdapter(dexAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            dexAdapter.getFilter().filter(newText);
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    }

    public static class MovesFragment extends Fragment implements SearchView.OnQueryTextListener {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private MoveListAdapter moveListAdapter;
        private ArrayList<Move> movesList;

        public static MovesFragment newInstance(int sectionNumber) {
            MovesFragment fragment = new MovesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public MovesFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            final ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            movesList = Global.db.getMovesList();
            Collections.sort(movesList, new Comparator() {
                public int compare(Object o1, Object o2) {
                    Move p1 = (Move) o1;
                    Move p2 = (Move) o2;
                    return p1.getMoveName().compareTo(p2.getMoveName());
                }
            });
            moveListAdapter = new MoveListAdapter(c,movesList,false);
            l1.setAdapter(moveListAdapter);
            SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);
            searchView.setOnQueryTextListener(this);
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortSpinner);
            ArrayAdapter<String> sortOptions = new ArrayAdapter<> (c,
                    android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {
                    "Name", "Type" } );
            spinner.setAdapter(sortOptions);
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getItemAtPosition(position).toString()) {
                        case "Name":
                            Collections.sort(movesList, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Move p1 = (Move) o1;
                                    Move p2 = (Move) o2;
                                    return p1.getMoveName().compareTo(p2.getMoveName());
                                }
                            });
                            moveListAdapter = new MoveListAdapter(c,movesList,false);
                            l1.setAdapter(moveListAdapter);
                            break;
                        case "Type":
                            Collections.sort(movesList, new Comparator() {
                                public int compare(Object o1, Object o2) {
                                    Move p1 = (Move) o1;
                                    Move p2 = (Move) o2;
                                    return p1.getType().compareTo(p2.getType());
                                }
                            });
                            moveListAdapter = new MoveListAdapter(c,movesList,false);
                            l1.setAdapter(moveListAdapter);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
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
