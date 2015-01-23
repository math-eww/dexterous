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
            new LoadData.CallAPI().execute(pokeballTog1States,pokeballTog2States,pokeballTog3States);
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
        if (id == R.id.action_redownloadInfo) {
            new DownloadPokemon.CallAPI().execute();  //DownloadPokemon -> LoadPokemon & DownloadSprites -> LoadSprites
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PokedexMainFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PokedexMissingFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            ArrayList<Pokemon> pokemonListMissing = new ArrayList();
            for (Pokemon poke : pokemonList) {
                if (!poke.getPokeballToggle1()) {
                    pokemonListMissing.add(poke);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListMissing);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PokedexCaughtFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            ArrayList<Pokemon> pokemonListCaught = new ArrayList();
            for (Pokemon poke : pokemonList) {
                if (poke.getPokeballToggle1()) {
                    pokemonListCaught.add(poke);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListCaught);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LivingDexMissingFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            ArrayList<Pokemon> pokemonListLDMissing = new ArrayList();
            for (Pokemon poke : pokemonList) {
                if (!poke.getPokeballToggle2()) {
                    pokemonListLDMissing.add(poke);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListLDMissing);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LivingDexCaughtFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            ArrayList<Pokemon> pokemonListLDCaught = new ArrayList();
            for (Pokemon poke : pokemonList) {
                if (poke.getPokeballToggle2()) {
                    pokemonListLDCaught.add(poke);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListLDCaught);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MyTeamFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            ListView l1=(ListView)rootView.findViewById(R.id.pokeList);
            ArrayList<Pokemon> pokemonListTeam = new ArrayList();
            for (Pokemon poke : pokemonList) {
                if (poke.getPokeballToggle3()) {
                    pokemonListTeam.add(poke);
                }
            }
            dexAdapter = new DexListAdapter(c,pokemonListTeam);
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
