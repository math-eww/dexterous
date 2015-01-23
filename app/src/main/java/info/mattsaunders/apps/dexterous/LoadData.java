package info.mattsaunders.apps.dexterous;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * Loads pokedex data in AsyncTask - calling LoadPokemon and LoadSprites
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

            MainActivity.pokemonList = LoadPokemon.buildPokeList(pokeballindicator1, pokeballindicator2, pokeballindicator3);
            LoadSprites.loadSprites();

            return resultToDisplay;
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
            MainActivity.dexAdapter.setList(MainActivity.pokemonList);
            MainActivity.dexAdapter.notifyDataSetChanged();
        }
    }
}
