package info.mattsaunders.apps.dexterous;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
            LoadEvoFrom.loadEvoFrom();

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


            Pokemon pokeSave = MainActivity.pokemonList.get(0);
            Log.i("SAVE POKEMON OBJECT","Beginning save process: " + pokeSave.getName() + ": " + pokeSave.getNumber());
            PokemonObjectSerializer.writeSerializedPokemon(pokeSave);
            Log.i("SAVE POKEMON OBJECT","Ending save process: " + pokeSave.getName() + ": " + pokeSave.getNumber());
            Log.i("SAVE POKEMON OBJECT","Beginning load process: " + pokeSave.getName() + ": " + pokeSave.getNumber());
            Pokemon pokeLoad = PokemonObjectSerializer.readSerializedPokemon(String.valueOf(pokeSave.getNumber()));
            Log.i("SAVE POKEMON OBJECT", "Ending load process: " + pokeLoad.getName() + ": " + pokeLoad.getNumber());
        }
    }
}
