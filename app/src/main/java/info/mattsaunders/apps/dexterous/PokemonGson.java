package info.mattsaunders.apps.dexterous;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Save/Load Pokemon objects from GSON strings, for quicker loading than parsing raw JSON file
 */
public class PokemonGson {

    public static void savePokemonObject(Pokemon poke, Context context) {
        SharedPreferences pokemonSharedPreference = context.getSharedPreferences("PokemonPrefsFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor pokemonPreferenceEdit = pokemonSharedPreference.edit();

        Log.i("GSON SAVE/LOAD: ", "Beginning conversion: " + poke.getName() + ": " + poke.getNumber());
        Gson gson = new Gson();
        String objStr = gson.toJson(poke);
        Log.i("GSON SAVE/LOAD: ", "Conversion successful - Beginning Save");

        pokemonPreferenceEdit.putString(poke.getStringNumber(), objStr);
        pokemonPreferenceEdit.commit();
        Log.i("GSON SAVE/LOAD: ", "Save successful " + poke.getStringNumber());
    }

    public static Pokemon loadPokemonObject(String toLoad, Context context) {
        Log.i("GSON SAVE/LOAD: ", "Beginning load");
        Gson gson = new Gson();
        SharedPreferences pokemonSharedPreference = context.getSharedPreferences("PokemonPrefsFile",Context.MODE_PRIVATE);
        Log.i("GSON SAVE/LOAD: ", "Loading string");
        String savedObjStr = pokemonSharedPreference.getString(toLoad, null);
        Log.i("GSON SAVE/LOAD: ", "String loaded - Beginning conversion");
        if(savedObjStr != null) {
            Pokemon poke = gson.fromJson(savedObjStr, Pokemon.class);
            Log.i("GSON SAVE/LOAD: ", "Object created: " + poke.getName() + ": " + poke.getNumber());
            return poke;
        } else {
            Log.e("GSON SAVE/LOAD: ", "Saved Object String is null!");
        }
        return null;
    }

    public static void savePokemonObjectList(ArrayList<Pokemon> pokeList, Context context) {
        SharedPreferences pokemonSharedPreference = context.getSharedPreferences("PokemonPrefsFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor pokemonPreferenceEdit = pokemonSharedPreference.edit();

        Log.i("GSON SAVE: ", "Beginning conversion: List size: " + pokeList.size());
        Gson gson = new Gson();
        String objStr = gson.toJson(pokeList);
        Log.i("GSON SAVE: ", "Conversion successful - Beginning Save");

        pokemonPreferenceEdit.putString("pokemonList", objStr);
        pokemonPreferenceEdit.commit();
        Log.i("GSON SAVE: ", "Save successful");
    }

    public static ArrayList<Pokemon> loadPokemonObjectList(Context context) {
        String toLoad = "pokemonList";
        Log.i("GSON LOAD: ", "Beginning load - Loading string");
        Gson gson = new Gson();
        SharedPreferences pokemonSharedPreference = context.getSharedPreferences("PokemonPrefsFile",Context.MODE_PRIVATE);
        String savedObjStr = pokemonSharedPreference.getString(toLoad, null);
        Log.i("GSON LOAD: ", "String loaded - Beginning conversion");
        if(savedObjStr != null) {
            ArrayList<Pokemon> pokeList = gson.fromJson(savedObjStr, new TypeToken<ArrayList<Pokemon>>(){}.getType());
            Log.i("GSON LOAD: ", "Object list created: Size: " + pokeList.size());
            return pokeList;
        } else {
            Log.e("GSON LOAD: ", "Saved Object String is null!");
        }
        return null;
    }
}
