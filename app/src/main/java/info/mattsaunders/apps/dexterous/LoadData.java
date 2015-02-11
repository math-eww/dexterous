package info.mattsaunders.apps.dexterous;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Loads pokedex data in AsyncTask - calling LoadPokemon or PokemonGson.loadPokemonObjectList and LoadSprites
 */
public class LoadData {
    public static class CallAPI extends AsyncTask<Bundle, String, String> {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.c);
            progress.setMessage("Loading Pokedex");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected String doInBackground(Bundle... params) {
            Bundle pokeballindicator1 = params[0];
            Bundle pokeballindicator2 = params[1];
            Bundle pokeballindicator3 = params[2];
            String resultToDisplay = "";


            //MainActivity.pokemonList = LoadPokemon.buildPokeList(pokeballindicator1, pokeballindicator2, pokeballindicator3);
            PokedexDatabase db;
            db = new PokedexDatabase(MainActivity.c);
            MainActivity.pokemonList = db.getPokemonList(pokeballindicator1, pokeballindicator2, pokeballindicator3);

            for (Pokemon poke : MainActivity.pokemonList) {
                if (poke.getPokeballToggle1()) { MainActivity.caughtDex++; }
                if (poke.getPokeballToggle2()) { MainActivity.livingDex++; }
            }

            LoadSprites.loadSprites();
            //LoadEvoFrom.loadEvoFrom();

            return resultToDisplay;
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
            MainActivity.dexAdapter.setList(MainActivity.pokemonList);
            MainActivity.dexAdapter.notifyDataSetChanged();
            NavigationDrawerFragment.navDrawerAdapter.setList(new String[]{
                    MainActivity.c.getString(R.string.title_section1),
                    MainActivity.c.getString(R.string.title_section2),
                    MainActivity.c.getString(R.string.title_section3),
                    MainActivity.c.getString(R.string.title_section4),
                    MainActivity.c.getString(R.string.title_section5),
                    MainActivity.c.getString(R.string.title_section6),
            });
            NavigationDrawerFragment.navDrawerAdapter.notifyDataSetChanged();
            new LoadInBackground.CallAPI().execute();
        }
    }
}
