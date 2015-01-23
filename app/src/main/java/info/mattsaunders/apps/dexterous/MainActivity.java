package info.mattsaunders.apps.dexterous;

import android.app.Activity;
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
import android.widget.ListView;

import java.util.ArrayList;


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
    public static ArrayList<Pokemon> pokemonList = new ArrayList();
    public static DexListAdapter dexAdapter;
    public static Bundle pokeballTog1States;
    public static Bundle pokeballTog2States;
    public static Bundle pokeballTog3States;

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

        //Load 3 bundles with pokemon pokeball toggles
        Utilities.FILENAME = "pokeball_1_states";
        pokeballTog1States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
        Utilities.FILENAME = "pokeball_2_states";
        pokeballTog2States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
        Utilities.FILENAME = "pokeball_3_states";
        pokeballTog3States = Utilities.JsonObjectToBundle(Utilities.readJsonFile());

        //Check if pokemon files already downloaded
        if (!Utilities.readSettingsFile().equals("1")) {
            new DownloadPokemon.CallAPI().execute();  //DownloadPokemon -> LoadPokemon & DownloadSprites -> LoadSprites
            Utilities.writeSettingsFile("1");
        } else {
            new LoadData.CallAPI().execute();
        }
    }

    @Override
    protected void onPause() {
        //Save 3 bundles with pokemon pokeball toggles
        pokeballTog1States = new Bundle();
        pokeballTog2States = new Bundle();
        pokeballTog3States = new Bundle();
        for (Pokemon poke : pokemonList) {
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
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
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
            return true;
        }
        if (id == R.id.action_redownloadPokemon) {
            new DownloadPokemon.CallAPI().execute();
            pokemonList = LoadPokemon.buildPokeList();
            return true;
        }
        if (id == R.id.action_redownloadSprites) {
            new DownloadSprites.CallAPI().execute();
            LoadSprites.loadSprites();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pokelist, container, false);
            ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            dexAdapter = new DexListAdapter(c,pokemonList);
            l1.setAdapter(dexAdapter);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
