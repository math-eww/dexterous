package info.mattsaunders.apps.dexterous;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class TeamBuilder extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    int pagesToShow = 1;

    static Context con;

    int teamCount = 0;
    Spinner spinner;
    ArrayAdapter<String> teamsList;
    ArrayList<String> teamNamesList;
    String curTeam;
    Bundle teamList;
    static Bundle teamDetails;
    static boolean validTeam = false;

    static final String[] NATURES = {
            "Adamant", "Bashful", "Bold", "Brave", "Calm", "Careful",
            "Docile", "Gentle", "Hardy", "Hasty", "Impish", "Jolly",
            "Lax", "Lonely", "Mild", "Modest", "Naive", "Naughty",
            "Quiet", "Quirky", "Rash", "Relaxed", "Sassy", "Serious", "Timid"
    };

    static String subfolder;
    static final String subfolderNotShiny = "xy-animated";
    static final String subfolderShiny = "xy-animated-shiny";

    static ArrayList<String> pokemonNameListTeam;
    static ArrayList<Pokemon> pokemonListTeam;
    static ArrayList<GifImageView> gifList;

    /*
    Data structures:
    teamDetails bundle -
        '0' = int index of frst pokemon ... '5' = int index of 6th pokemon
        '0shiny' = 1 for shiny, 0 for not shiny ... '5shiny' = 1 for shiny, 0 for not shiny
        '0move0' = int index of first move in movelist for first pokemon ... '5move0' = int index of first move in movelist for 6th pokemon
        ...
        '0move3' = int index of 4th move in movelist for first pokemon ... '5move3' = int index of 4th move in movelist for 6th pokemon
    teamList bundle -
        'teamCount' = num of teams
        '0' = first team... 'n' = nth team
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_builder);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(7);

        con = this;
        curTeam = "-----";
        teamNamesList = new ArrayList<>();

        //Add all teams to teamNamesList for spinner, and add a new team option at the end
        Utilities.SUBDIR = "Teams";
        Utilities.FILENAME = "team_list";
        teamList = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
        if (teamList != null) {
            teamCount = teamList.getInt("teamCount");
            for (int i = 1; i < teamCount + 1; i++) {
                teamNamesList.add(teamList.getString(String.valueOf(i)));
            }
        } else {
            teamNamesList.add("-----");
            teamList = new Bundle();
        }
        teamNamesList.add("New Team");

        //Get list of pokemon with my team toggle on:
        pokemonNameListTeam = new ArrayList<>();
        pokemonListTeam = new ArrayList<>();
        for (Pokemon poke : Global.pokemonList) {
            if (poke.getPokeballToggle3()) {
                pokemonNameListTeam.add(poke.getName());
                pokemonListTeam.add(poke);
            }
        }

    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_builder, menu);
        //TODO: option to delete team
        MenuItem item = menu.findItem(R.id.action_spinnerChangeTeam);
        item.setActionView(R.layout.team_spinner);
        spinner = (Spinner) item.getActionView().findViewById(R.id.team_spinner);
        teamsList = new ArrayAdapter<>(con, R.layout.action_bar_spinner_item, teamNamesList);
        spinner.setAdapter(teamsList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveData();
                System.out.println(parent.getItemAtPosition(position));
                curTeam = (String) parent.getItemAtPosition(position);
                if (parent.getItemAtPosition(position).equals("New Team")) {
                    //Add new team - dialog popup
                    createNewTeam();
                    teamDetails = null;
                } else if (parent.getItemAtPosition(position).equals("-----")){
                    teamDetails = null;
                    validTeam = false;
                    pagesToShow = 1;
                    resetFragments();
                } else {
                    //Open team bundle
                    Utilities.SUBDIR = "Teams";
                    Utilities.FILENAME = (String) parent.getItemAtPosition(position);
                    teamDetails = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
                    if (teamDetails == null) {
                        initTeamDetails();
                    }
                    validTeam = true;
                    pagesToShow = 7;
                    resetFragments();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    private void createNewTeam() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add New Team");
        alert.setMessage("Enter a name for the new team");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                teamList.putString(String.valueOf(teamCount + 1), input.getText().toString());
                teamNamesList.add(teamNamesList.size()-1,input.getText().toString());
                teamsList = new ArrayAdapter<>(con, R.layout.action_bar_spinner_item, teamNamesList);
                spinner.setAdapter(teamsList);
                spinner.setSelection(teamsList.getPosition(input.getText().toString()));
                teamList.putInt("teamCount",teamList.getInt("teamCount") + 1);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                spinner.setSelection(0);
            }
        });

        alert.show();
    }

    private void initTeamDetails() {
        teamDetails = new Bundle();
        for (int i = 0; i < 6; i++) {
            //init Pokemon details for each pokemon
            teamDetails.putInt(String.valueOf(i), -1); //Pokemon index
            teamDetails.putInt(i + "shiny", 0); //Shiny toggle
            teamDetails.putInt(i+"move"+"0",-1); //Move 1 index
            teamDetails.putInt(i+"move"+"1",-1); //Move 2 index
            teamDetails.putInt(i+"move"+"2",-1); //Move 3 index
            teamDetails.putInt(i+"move"+"3",-1); //Move 4 index
        }
    }

    private void resetFragments() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(7);
    }

    private void saveData() {
        Utilities.SUBDIR = "Teams";
        Utilities.FILENAME = "team_list";
        Utilities.writeJsonFile(Utilities.bundleToJsonObject(teamList));
        if (teamDetails != null) {
            Utilities.FILENAME = curTeam;
            Utilities.writeJsonFile(Utilities.bundleToJsonObject(teamDetails));
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TeamViewFragment.newInstance(position + 1);
                case 1:
                    return TeamPokemonMember.newInstance(position + 1);
                case 2:
                    return TeamPokemonMember.newInstance(position + 1);
                case 3:
                    return TeamPokemonMember.newInstance(position + 1);
                case 4:
                    return TeamPokemonMember.newInstance(position + 1);
                case 5:
                    return TeamPokemonMember.newInstance(position + 1);
                case 6:
                    return TeamPokemonMember.newInstance(position + 1);
                default:
                    return TeamViewFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            return pagesToShow;
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

    public static class TeamViewFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public static TeamViewFragment newInstance(int sectionNumber) {
            TeamViewFragment fragment = new TeamViewFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public TeamViewFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_team_builder, container, false);

            subfolder = subfolderNotShiny;

            //Get gif views to hold plus symbols/pokemon views
            GifImageView teamViewGif1 = (GifImageView) rootView.findViewById(R.id.teamViewGif1);
            GifImageView teamViewGif2 = (GifImageView) rootView.findViewById(R.id.teamViewGif2);
            GifImageView teamViewGif3 = (GifImageView) rootView.findViewById(R.id.teamViewGif3);
            GifImageView teamViewGif4 = (GifImageView) rootView.findViewById(R.id.teamViewGif4);
            GifImageView teamViewGif5 = (GifImageView) rootView.findViewById(R.id.teamViewGif5);
            GifImageView teamViewGif6 = (GifImageView) rootView.findViewById(R.id.teamViewGif6);
            //Get appropiate width
            DisplayMetrics metrics = con.getResources().getDisplayMetrics();
            int width = metrics.widthPixels / 2;
            //Set gif views to correct width - half of screen size
            teamViewGif1.setLayoutParams(new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.MATCH_PARENT));
            teamViewGif2.setLayoutParams(new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.MATCH_PARENT));
            teamViewGif3.setLayoutParams(new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.MATCH_PARENT));
            teamViewGif4.setLayoutParams(new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.MATCH_PARENT));
            teamViewGif5.setLayoutParams(new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.MATCH_PARENT));
            teamViewGif6.setLayoutParams(new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.MATCH_PARENT));
            //Set gif view scaling:
            teamViewGif1.setScaleType(ImageView.ScaleType.CENTER);
            teamViewGif1.setScaleX(3.5f); teamViewGif1.setScaleY(3.5f);
            teamViewGif2.setScaleType(ImageView.ScaleType.CENTER);
            teamViewGif2.setScaleX(3.5f); teamViewGif2.setScaleY(3.5f);
            teamViewGif3.setScaleType(ImageView.ScaleType.CENTER);
            teamViewGif3.setScaleX(3.5f); teamViewGif3.setScaleY(3.5f);
            teamViewGif4.setScaleType(ImageView.ScaleType.CENTER);
            teamViewGif4.setScaleX(3.5f); teamViewGif4.setScaleY(3.5f);
            teamViewGif5.setScaleType(ImageView.ScaleType.CENTER);
            teamViewGif5.setScaleX(3.5f); teamViewGif5.setScaleY(3.5f);
            teamViewGif6.setScaleType(ImageView.ScaleType.CENTER);
            teamViewGif6.setScaleX(3.5f); teamViewGif6.setScaleY(3.5f);
            //Make gifview array
            gifList = new ArrayList<>();
            gifList.add(teamViewGif1);
            gifList.add(teamViewGif2);
            gifList.add(teamViewGif3);
            gifList.add(teamViewGif4);
            gifList.add(teamViewGif5);
            gifList.add(teamViewGif6);

            //Check if valid team has been selected
            if (teamDetails != null) {
                //Get current team pokemon
                for (int i = 0; i < 6; i++) {
                    //Build gif sprites for current team and show them
                    int pokemonIndex = teamDetails.getInt(String.valueOf(i));
                    if (pokemonIndex != -1) {
                        try {
                            if (teamDetails.getInt(i + "shiny",0) == 1) {
                                subfolder = subfolderShiny;
                            } else {
                                subfolder = subfolderNotShiny;
                            }
                            GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolder + "/" +
                                    pokemonListTeam.get(pokemonIndex).getThreeDigitStringNumber() + ".gif");
                            gifList.get(i).setImageDrawable(gifFromAssets);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return rootView;
        }
    }

    public static class TeamPokemonMember extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private Pokemon poke = null;
        private boolean isShiny = false;
        private int position;

        public static TeamPokemonMember newInstance(int sectionNumber) {
            TeamPokemonMember fragment = new TeamPokemonMember();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public TeamPokemonMember() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_team_builder_pokemon, container, false);

            position = getArguments().getInt(ARG_SECTION_NUMBER) - 2;
            System.out.println("Position: " + position);

            if (validTeam) {
                //Shiny toggle
                CheckBox shinyToggle = (CheckBox) rootView.findViewById(R.id.teamBuilderPokemonIsShiny);
                shinyToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isShiny = isChecked;
                        if (poke != null) {
                            resetGif();
                        }
                        int toStore = 0;
                        if (isChecked) { toStore = 1; }
                        teamDetails.putInt(position+"shiny",toStore);
                    }
                });
                if (teamDetails.getInt(position+"shiny",0) == 1) {
                    shinyToggle.setChecked(true);
                }

                //Nature Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getActivity(), android.R.layout.simple_dropdown_item_1line, NATURES
                );
                final Spinner natureEntered = (Spinner) rootView.findViewById(R.id.teamBuilderPokemonNature);
                natureEntered.setAdapter(adapter);
                //TODO: set nature from loaded bundle

                //Pokemon AutoComplete EditText
                adapter = new ArrayAdapter<>(
                        getActivity(), android.R.layout.simple_dropdown_item_1line, pokemonNameListTeam
                );
                final AutoCompleteTextView pokemonEntered = (AutoCompleteTextView) rootView.findViewById(R.id.teamBuilderPokemonName);
                pokemonEntered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pokemonEntered.setText("");
                    }
                });
                pokemonEntered.setAdapter(adapter);
                pokemonEntered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId) {
                        //Set Pokemon object
                        int pokePosition = pokemonNameListTeam.indexOf(pokemonEntered.getEditableText().toString());
                        poke = pokemonListTeam.get(pokePosition);
                        //Set gif
                        resetGif();
                        teamDetails.putInt(String.valueOf(position),pokePosition);
                    }
                });
                if (teamDetails.getInt(String.valueOf(position)) != -1) {
                    poke = pokemonListTeam.get(teamDetails.getInt(String.valueOf(position)));
                    pokemonEntered.setText(poke.getName());
                }

                //TODO: move list AutoCompleteTextView & loading/saving

                //TODO: show stats - user enter EVs, IVs - and use nature to calc stats
            }

            return rootView;
        }

        private void resetGif() {
            try {
                if (isShiny) { subfolder = subfolderShiny; } else { subfolder = subfolderNotShiny; }
                GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolder + "/" +
                        poke.getThreeDigitStringNumber() + ".gif");
                System.out.println("Resetting GIF position: " + position);
                gifList.get(position).setImageDrawable(gifFromAssets);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO: fragment to show type coverages

}
