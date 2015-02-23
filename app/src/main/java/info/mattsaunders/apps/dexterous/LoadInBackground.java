package info.mattsaunders.apps.dexterous;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Loads additional info to Pokemon objects in main list - moveset, evolutions, abilities, & egg types
 */
public class LoadInBackground {
    public static class CallAPI extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... params) {
            String resultToDisplay = "";

            //Load data for each pokemon in background so user doesn't have to wait -
            //-----moveset, evolutions, abilities, & egg types
            PokedexDatabase db = MainActivity.db;
            for (Pokemon poke : MainActivity.pokemonList) {
                int pokemonNumber = poke.getNumber();
                if (poke.getMoveset() == null) { poke.setMoveset(db.getMoveset(pokemonNumber)); }
                if (poke.getAbilities() == null) { poke.setAbilities(db.getAbilities(pokemonNumber)); }
                if (poke.getEggTypes() == null) { poke.setEggTypes(db.getEggGroups(pokemonNumber)); }
                if (poke.getEvolutions() == null) { poke.setEvolutions(db.getEvolutions(pokemonNumber)); }
                if (poke.getEvolvesFromNum() == 0) { db.setEvolvesFrom(pokemonNumber); }
                //Log.i("Background Load", "Loaded: " + poke.getName() + "'s additional information");
            }

            return resultToDisplay;
        }
        protected void onPostExecute(String result) {
            /*
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
            */
        }
    }
}
