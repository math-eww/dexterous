package info.mattsaunders.apps.dexterous;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class to load downloaded Pokemon info
 */
public class LoadPokemon {
    public static ArrayList<Pokemon> buildPokeList() {
        ArrayList<Pokemon> pokelist = new ArrayList();
        for (int i = 1; i <= DownloadPokemon.getTOTAL_POKES(); i++) {
            Utilities.FILENAME = String.valueOf(i);
            JSONObject jsonObject = Utilities.readJsonFile();
            String name = "";
            String type1 = "";
            String type2 = "";
            //Try to parse JSON object:
            try {
                name = jsonObject.getString("name");
            } catch (JSONException e) {
                Log.e("JSON LOADING", "Failed to read JSON: " + e);
            }
            pokelist.add(new Pokemon(i,name,type1,type2));
        }
        for (Pokemon poke : pokelist) {
            System.out.println(poke.getSummary());
        }
        return pokelist;
    }
}
