package info.mattsaunders.apps.dexterous;

import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * Loads pokedex data in AsyncTask - calling LoadPokemon and LoadSprites
 */
public class LoadData {
    public static class CallAPI extends AsyncTask<String, String, String> {
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(MainActivity.c);
            progress.setMessage("Loading Pokedex");
            progress.setCancelable(false);
            progress.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String resultToDisplay = "";

            MainActivity.pokemonList = LoadPokemon.buildPokeList();
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
