package info.mattsaunders.apps.dexterous;

import android.os.AsyncTask;

/**
 * Saves Pokemon object list data as separate task
 */
public class SaveData {
    public static class CallAPI extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... params) {
            String resultToDisplay = "";

            PokemonGson.savePokemonObjectList(MainActivity.pokemonList,MainActivity.c);

            return resultToDisplay;
        }
        protected void onPostExecute(String result) {
        }
    }
}
