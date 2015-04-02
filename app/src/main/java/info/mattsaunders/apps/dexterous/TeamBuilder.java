package info.mattsaunders.apps.dexterous;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class TeamBuilder extends ActionBarActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;

    PageIndicator mIndicator;

    int pagesToShow = 1;

    static Context con;

    int teamCount = 0;
    Spinner spinner;
    ArrayAdapter<String> teamNamesListAdapter;
    ArrayList<String> teamNamesList;
    String curTeam;
    Bundle teamList;
    static Bundle teamDetails;

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

    protected Hashtable<Integer, WeakReference<Fragment>> fragmentReferences = new Hashtable<>();

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

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(7);

        mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);

        con = this;
        curTeam = "-----";
        teamNamesList = new ArrayList<>();

        //Add all teams to teamNamesList for spinner, and add a new team option at the end
        Utilities.SUBDIR = "Teams";
        Utilities.FILENAME = "team_list";
        teamList = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
        if (teamList != null && teamList.getInt("teamCount") > 0) {
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
        getMenuInflater().inflate(R.menu.menu_team_builder, menu);

        MenuItem item = menu.findItem(R.id.action_spinnerChangeTeam);
        item.setActionView(R.layout.team_spinner);
        spinner = (Spinner) item.getActionView().findViewById(R.id.team_spinner);
        teamNamesListAdapter = new ArrayAdapter<>(con, R.layout.action_bar_spinner_item, teamNamesList);
        spinner.setAdapter(teamNamesListAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("New Team")) {
                    saveData();
                    curTeam = (String) parent.getItemAtPosition(position);
                    createNewTeam();
                    teamDetails = null;
                } else if (parent.getItemAtPosition(position).equals("-----")){
                    curTeam = (String) parent.getItemAtPosition(position);
                    teamDetails = null;
                    mViewPager.setCurrentItem(0);
                    pagesToShow = 1;
                    mIndicator.notifyDataSetChanged();
                    resetFragments();
                } else {
                    saveData();
                    curTeam = (String) parent.getItemAtPosition(position);
                    Utilities.SUBDIR = "Teams";
                    Utilities.FILENAME = curTeam;
                    teamDetails = null;
                    teamDetails = Utilities.JsonObjectToBundle(Utilities.readJsonFile());
                    if (teamDetails == null) {
                        initTeamDetails();
                    }
                    pagesToShow = 7;
                    mIndicator.notifyDataSetChanged();
                    resetFragments();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Handler actionHandler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                editTeamName((String) spinner.getSelectedItem());
            }
        };

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    actionHandler.postDelayed(runnable, 500);
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    actionHandler.removeCallbacks(runnable);
                }
                return false;

            }
        });
        spinner.setOnLongClickListener(null);
        spinner.setLongClickable(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            deleteTeam();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createNewTeam() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Add New Team");
        alert.setMessage("Enter a name for the new team");

        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String inputText = input.getText().toString();
                if (teamNamesList.contains(inputText)) {
                    dialog.cancel();
                    String msgText;
                    if (inputText.equals("New Team") | inputText.equals("-----")) {
                        msgText = "You can't name your team that!";
                    } else {
                        msgText = "You already have a team named that!";
                    }
                    new AlertDialog.Builder(TeamBuilder.this)
                            .setTitle("Error")
                            .setMessage(msgText)
                            .setCancelable(false)
                            .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogTwo, int which) {
                                    //Relaunch create new team
                                    createNewTeam();
                                }
                            }).create().show();
                } else {
                    if (teamNamesList.contains("-----")) {
                        teamNamesList.remove("-----");
                    }
                    teamList.putString(String.valueOf(teamCount + 1), input.getText().toString());
                    teamNamesList.add(teamNamesList.size() - 1, input.getText().toString());
                    teamNamesListAdapter = new ArrayAdapter<>(con, R.layout.action_bar_spinner_item, teamNamesList);
                    spinner.setAdapter(teamNamesListAdapter);
                    spinner.setSelection(teamNamesListAdapter.getPosition(input.getText().toString()));
                    teamCount++;
                    teamList.putInt("teamCount", teamCount);
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                spinner.setSelection(0);
            }
        });

        alert.show();
    }

    private void deleteTeam() {
        if (!curTeam.equals("New Team") && !curTeam.equals("-----")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(TeamBuilder.this);

            alert.setTitle("Delete Team");
            alert.setMessage("Are you sure you want to delete this team?");

            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utilities.SUBDIR = "Teams";
                    Utilities.FILENAME = curTeam;
                    Utilities.deleteFile();
                    teamDetails = null;
                    teamCount--;
                    teamNamesList.remove(curTeam);
                    if (teamNamesList.size() < 2) {
                        teamList = new Bundle();
                        teamList.putInt("teamCount",teamCount);
                        teamNamesList.add(0, "-----");
                    } else {
                        teamList = new Bundle();
                        teamList.putInt("teamCount",teamCount);
                        for (int i = 1; i < teamCount + 1; i++) {
                            teamList.putString(String.valueOf(i), teamNamesList.get(i - 1));
                        }
                    }
                    teamNamesListAdapter = new ArrayAdapter<>(con, R.layout.action_bar_spinner_item, teamNamesList);
                    spinner.setAdapter(teamNamesListAdapter);
                    spinner.setSelection(0);
                    saveData(); //Somehow this is saving a team with the name ----- even though teamDetails should be null
                    dialog.dismiss();
                }
            });

            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    private void editTeamName(final String teamName) {
        if (!curTeam.equals("New Team") && !curTeam.equals("-----")) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Edit Team Name");
            alert.setMessage("Enter a new name");

            final EditText input = new EditText(this);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
            input.setText(teamName);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String inputText = input.getText().toString();
                    //Check if there is already a team called that/User tries to enter New Team or -----/User enters the same name
                    if (teamNamesList.contains(inputText)) {
                        dialog.cancel();
                        String msgText;
                        if (inputText.equals("New Team") | inputText.equals("-----")) {
                            msgText = "You can't name your team that!";
                        } else if (inputText.equals(teamName)) {
                            msgText = "New team name must be different from old team name";
                        } else {
                            msgText = "You already have a team named that!";
                        }
                        new AlertDialog.Builder(TeamBuilder.this)
                                .setTitle("Error")
                                .setMessage(msgText)
                                .setCancelable(false)
                                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogTwo, int which) {
                                        //Relaunch create new team
                                        createNewTeam();
                                    }
                                }).create().show();
                        //Else change team name
                    } else {
                        //Get index of team
                        int indexOfTeam = teamNamesList.indexOf(teamName);
                        //Remove name from teamNamesList and replace with new entered text
                        teamNamesList.remove(teamName);
                        teamNamesList.add(indexOfTeam, input.getText().toString());
                        //Remove name from teamList bundle and replace with new entered text
                        teamList.putString(String.valueOf(indexOfTeam + 1), input.getText().toString());
                        //Change file name:
                        Utilities.renameFile(teamName, input.getText().toString());
                        //Reset spinner
                        teamNamesListAdapter = new ArrayAdapter<>(con, R.layout.action_bar_spinner_item, teamNamesList);
                        spinner.setAdapter(teamNamesListAdapter);
                        spinner.setSelection(teamNamesListAdapter.getPosition(input.getText().toString()));
                    }
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });

            alert.show();
        }
    }

    private void initTeamDetails() {
        teamDetails = new Bundle();
        for (int i = 0; i < 6; i++) {
            //init Pokemon details for each pokemon
            teamDetails.putString(String.valueOf(i), ""); //Pokemon index
            teamDetails.putInt(i + "shiny", 0); //Shiny toggle
            teamDetails.putInt(i+"move"+"0",-1); //Move 1 index
            teamDetails.putInt(i+"move"+"1",-1); //Move 2 index
            teamDetails.putInt(i+"move"+"2",-1); //Move 3 index
            teamDetails.putInt(i+"move"+"3",-1); //Move 4 index
            teamDetails.putInt(i+"nature",-1);
        }
    }

    private void resetFragments() {
        mSectionsPagerAdapter.notifyDataSetChanged();
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            Object f = getFragment(i);
            if (f instanceof TeamPokemonMember) {
                TeamPokemonMember fTPM = (TeamPokemonMember) f;
                fTPM.reset();
            } else {
                TeamViewFragment fTVF = (TeamViewFragment) f;
                fTVF.reset();
            }
        }
        mViewPager.setCurrentItem(0);
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

    private Fragment getFragment(int fragmentId) {
        WeakReference<Fragment> ref = fragmentReferences.get(fragmentId);
        return ref == null ? null : ref.get();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = TeamViewFragment.newInstance(position + 1);
                    break;
                case 1:
                    fragment = TeamPokemonMember.newInstance(position + 1);
                    break;
                case 2:
                    fragment = TeamPokemonMember.newInstance(position + 1);
                    break;
                case 3:
                    fragment = TeamPokemonMember.newInstance(position + 1);
                    break;
                case 4:
                    fragment = TeamPokemonMember.newInstance(position + 1);
                    break;
                case 5:
                    fragment = TeamPokemonMember.newInstance(position + 1);
                    break;
                case 6:
                    fragment = TeamPokemonMember.newInstance(position + 1);
                    break;
                default:
                    fragment = TeamViewFragment.newInstance(position + 1);
                    break;
            }
            fragmentReferences.put(position, new WeakReference<>(fragment));
            return fragment;
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

        public void reset() {
            System.out.println("Resetting TeamViewFragment fragment at " + getArguments().getInt(ARG_SECTION_NUMBER));

            if (teamDetails != null) {
                for (int i = 0; i < 6; i++) {
                    //Build gif sprites for current team and show them
                    String pokemonName = teamDetails.getString(String.valueOf(i));
                    if (!pokemonName.equals("")) {
                        Pokemon tempPoke = null;
                        for (Pokemon temp : pokemonListTeam) {
                            if (temp.getName().equals(pokemonName)) {
                                tempPoke = temp;
                            }
                        }
                        if (tempPoke != null) {
                            try {
                                if (teamDetails.getInt(i + "shiny", 0) == 1) {
                                    subfolder = subfolderShiny;
                                } else {
                                    subfolder = subfolderNotShiny;
                                }
                                GifDrawable gifFromAssets = new GifDrawable(con.getAssets(), subfolder + "/" +
                                        tempPoke.getThreeDigitStringNumber() + ".gif");
                                gifList.get(i).setImageDrawable(gifFromAssets);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        gifList.get(i).setImageResource(R.drawable.plainball);
                    }
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    gifList.get(i).setImageResource(R.drawable.plainball);
                }
            }
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
            //Get appropriate width
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
            //Make gif view array
            gifList = new ArrayList<>();
            gifList.add(teamViewGif1);
            gifList.add(teamViewGif2);
            gifList.add(teamViewGif3);
            gifList.add(teamViewGif4);
            gifList.add(teamViewGif5);
            gifList.add(teamViewGif6);

            return rootView;
        }
    }

    public static class TeamPokemonMember extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        private Pokemon poke = null;
        private boolean isShiny = false;
        private int position;

        private CheckBox shinyToggle;
        private Spinner natureEntered;
        private AutoCompleteTextView pokemonEntered;
        private AutoCompleteTextView pokemonMovesSlot1;
        private AutoCompleteTextView pokemonMovesSlot2;
        private AutoCompleteTextView pokemonMovesSlot3;
        private AutoCompleteTextView pokemonMovesSlot4;

        private ArrayList<Move> movesList = new ArrayList<>();

        private Move moveOne;
        private Move moveTwo;
        private Move moveThree;
        private Move moveFour;

        public static TeamPokemonMember newInstance(int sectionNumber) {
            TeamPokemonMember fragment = new TeamPokemonMember();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public TeamPokemonMember() {
        }

        public void reset() {
            System.out.println("Resetting TeamPokemonMember fragment at " + getArguments().getInt(ARG_SECTION_NUMBER));

            if (teamDetails != null) {
                //Name & Poke
                if (!teamDetails.getString(String.valueOf(position)).equals("")) {
                    String savedName = teamDetails.getString(String.valueOf(position));
                    for (Pokemon tempPoke : pokemonListTeam) {
                        if (tempPoke.getName().equals(savedName)) {
                            poke = tempPoke;
                        }
                    }
                    pokemonEntered.setText(poke.getName());
                } else {
                    poke = null;
                    pokemonEntered.setText("");
                }
                //Shiny
                if (teamDetails.getInt(position+"shiny",0) == 1) {
                    shinyToggle.setChecked(true);
                    isShiny = true;
                } else {
                    shinyToggle.setChecked(false);
                    isShiny = false;
                }
                //Nature
                if (teamDetails.getInt(position + "nature",-1) != -1) {
                    natureEntered.setSelection(teamDetails.getInt(position + "nature"));
                }
                //Moves
                setMoveList();
                for (int i = 0; i < 5; i++) {
                    if (teamDetails.getInt(position+"move"+i,-1) != -1) {
                        switch (i) {
                            case 0:
                                moveOne = movesList.get(teamDetails.getInt(position+"move"+i));
                                pokemonMovesSlot1.setText(moveOne.getMoveName());
                                break;
                            case 1:
                                moveTwo = movesList.get(teamDetails.getInt(position+"move"+i));
                                pokemonMovesSlot2.setText(moveTwo.getMoveName());
                                break;
                            case 2:
                                moveThree = movesList.get(teamDetails.getInt(position+"move"+i));
                                pokemonMovesSlot3.setText(moveThree.getMoveName());
                                break;
                            case 3:
                                moveFour = movesList.get(teamDetails.getInt(position+"move"+i));
                                pokemonMovesSlot4.setText(moveFour.getMoveName());
                                break;
                        }
                    } else {
                        switch (i) {
                            case 0:
                                moveOne = null;
                                pokemonMovesSlot1.setText("");
                                break;
                            case 1:
                                moveTwo = null;
                                pokemonMovesSlot2.setText("");
                                break;
                            case 2:
                                moveThree = null;
                                pokemonMovesSlot3.setText("");
                                break;
                            case 3:
                                moveFour = null;
                                pokemonMovesSlot4.setText("");
                                break;
                        }
                    }
                }
                //IV/EVs
                //TODO: set IV/EVs from loaded bundle

                pokemonEntered.dismissDropDown();
                pokemonMovesSlot1.dismissDropDown();
                pokemonMovesSlot2.dismissDropDown();
                pokemonMovesSlot3.dismissDropDown();
                pokemonMovesSlot4.dismissDropDown();
            }
        }

        private void setMoveList() {
            if (poke != null) {
                //Add this pokemon's moveset, loading from DB if necessary
                if (poke.getMoveset() == null) { poke.setMoveset(Global.db.getMoveset(poke.getNumber())); }
                movesList = new ArrayList<>(poke.getMoveset());
                //Check if this pokemon has a prior evolution, loading from DB if necessary
                if (poke.getEvolvesFromNum() == 0) { Global.db.setEvolvesFrom(poke.getNumber()); }
                if (poke.getEvolvesFromNum() != 0) {
                    Pokemon evolvesFrom = Global.pokemonList.get(poke.getEvolvesFromNum()-1);
                    //Get moveset for a prior evolution and add to the list
                    if (evolvesFrom.getMoveset() == null) { evolvesFrom.setMoveset(Global.db.getMoveset(evolvesFrom.getNumber())); }
                    movesList.addAll(evolvesFrom.getMoveset());
                    //Check if the prior evolution has a further prior evolution, loading from DB if necessary
                    if (evolvesFrom.getEvolvesFromNum() == 0) { Global.db.setEvolvesFrom(evolvesFrom.getNumber()); }
                    if (evolvesFrom.getEvolvesFromNum() != 0) {
                        Pokemon evolvesFromFrom = Global.pokemonList.get(evolvesFrom.getEvolvesFromNum()-1);
                        //Finally, get the further prior evolution's moveset, loadingfrom DB if necessary
                        if (evolvesFromFrom.getMoveset() == null) { evolvesFromFrom.setMoveset(Global.db.getMoveset(evolvesFromFrom.getNumber())); }
                        movesList.addAll(evolvesFromFrom.getMoveset());
                    }
                }
                //Remove duplicate moves
                Set<String> moveNames = new HashSet<>();
                for (Iterator<Move> it = movesList.iterator(); it.hasNext(); ) {
                    if (!moveNames.add(it.next().getMoveName())) {
                        it.remove();
                    }
                }
                //Sort moves alphabetically
                Collections.sort(movesList, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        Move p1 = (Move) o1;
                        Move p2 = (Move) o2;
                        return p1.getMoveName().compareTo(p2.getMoveName());
                    }
                });
                //Set move names to AutoCompleteEditTexts
                MoveListAdapter adapter = new MoveListAdapter(getActivity(), movesList, true);
                pokemonMovesSlot1.setAdapter(adapter);
                adapter = new MoveListAdapter(getActivity(), movesList, true);
                pokemonMovesSlot2.setAdapter(adapter);
                adapter = new MoveListAdapter(getActivity(), movesList, true);
                pokemonMovesSlot3.setAdapter(adapter);
                adapter = new MoveListAdapter(getActivity(), movesList, true);
                pokemonMovesSlot4.setAdapter(adapter);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_team_builder_pokemon, container, false);

            position = getArguments().getInt(ARG_SECTION_NUMBER) - 2;

            //Shiny toggle setup
            shinyToggle = (CheckBox) rootView.findViewById(R.id.teamBuilderPokemonIsShiny);
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

            //Nature Spinner setup
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line, NATURES
            );
            natureEntered = (Spinner) rootView.findViewById(R.id.teamBuilderPokemonNature);
            natureEntered.setAdapter(adapter);
            natureEntered.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int positionOfItem, long id) {
                    teamDetails.putInt(position + "nature",positionOfItem);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Pokemon & Name AutoCompleteEditText setup
            adapter = new ArrayAdapter<>(
                    getActivity(), android.R.layout.simple_dropdown_item_1line, pokemonNameListTeam
            );
            pokemonEntered = (AutoCompleteTextView) rootView.findViewById(R.id.teamBuilderPokemonName);
            pokemonEntered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pokemonEntered.showDropDown();
                }
            });
            pokemonEntered.setAdapter(adapter);
            pokemonEntered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId) {
                    //Set Pokemon object
                    String pokemonName = pokemonEntered.getEditableText().toString();
                    for (Pokemon tempPoke : pokemonListTeam) {
                        if (tempPoke.getName().equals(pokemonName)) {
                            poke = tempPoke;
                            break;
                        }
                    }
                    //Set moves
                    setMoveList();
                    //Set gif
                    resetGif();
                    teamDetails.putString(String.valueOf(position),pokemonName);
                }
            });

            //Moves setup
            pokemonMovesSlot1 = (AutoCompleteTextView) rootView.findViewById(R.id.teamBuilderPokeMove1);
            pokemonMovesSlot1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pokemonMovesSlot1.showDropDown();
                }
            });
            pokemonMovesSlot1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId) {
                    //Set Move to selected move, and save data
                    moveOne = (Move) parent.getAdapter().getItem(pos);
                    pokemonMovesSlot1.setText(moveOne.getMoveName());
                    teamDetails.putInt(position + "move0", movesList.indexOf(moveOne));
                    //Calculate type effectiveness
                }
            });
            pokemonMovesSlot2 = (AutoCompleteTextView) rootView.findViewById(R.id.teamBuilderPokeMove2);
            pokemonMovesSlot2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pokemonMovesSlot2.showDropDown();
                }
            });
            pokemonMovesSlot2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId) {
                    //Set Move to selected move, and save data
                    moveTwo = (Move) parent.getAdapter().getItem(pos);
                    pokemonMovesSlot2.setText(moveTwo.getMoveName());
                    teamDetails.putInt(position + "move1", movesList.indexOf(moveTwo));
                    //Calculate type effectiveness
                }
            });
            pokemonMovesSlot3 = (AutoCompleteTextView) rootView.findViewById(R.id.teamBuilderPokeMove3);
            pokemonMovesSlot3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pokemonMovesSlot3.showDropDown();
                }
            });
            pokemonMovesSlot3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId) {
                    //Set Move to selected move, and save data
                    moveThree = (Move) parent.getAdapter().getItem(pos);
                    pokemonMovesSlot3.setText(moveThree.getMoveName());
                    teamDetails.putInt(position + "move2", movesList.indexOf(moveThree));
                    //Calculate type effectiveness
                }
            });
            pokemonMovesSlot4 = (AutoCompleteTextView) rootView.findViewById(R.id.teamBuilderPokeMove4);
            pokemonMovesSlot4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pokemonMovesSlot4.showDropDown();
                }
            });
            pokemonMovesSlot4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int pos, long rowId) {
                    //Set Move to selected move, and save data
                    moveFour = (Move) parent.getAdapter().getItem(pos);
                    pokemonMovesSlot4.setText(moveFour.getMoveName());
                    teamDetails.putInt(position + "move3", movesList.indexOf(moveFour));
                    //Calculate type effectiveness
                }
            });

            //TODO: stats, IVs, EVs EditText init

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
