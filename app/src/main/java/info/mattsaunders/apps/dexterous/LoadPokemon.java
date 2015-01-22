package info.mattsaunders.apps.dexterous;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
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
            int hp = 0;
            int atk = 0;
            int def = 0;
            int spatk = 0;
            int spdef = 0;
            int spd = 0;
            String height = "";
            String weight = "";
            //Bundle[] evolutions = new Bundle[0];
            ArrayList<Bundle> evolutions = new ArrayList();
            //Try to parse JSON object:
            try {
                //Get name:
                name = jsonObject.getString("name");
                //Get JSONArray of types:
                JSONArray types = jsonObject.getJSONArray("types");
                type1 = types.getJSONObject(0).getString("name");
                if (types.length() > 1) {
                    type2 = types.getJSONObject(1).getString("name");
                }
                //Get stats:
                hp = jsonObject.getInt("hp");
                atk = jsonObject.getInt("attack");
                def = jsonObject.getInt("defense");
                spatk = jsonObject.getInt("sp_atk");
                spdef = jsonObject.getInt("sp_def");
                spd = jsonObject.getInt("speed");
                //Get height and weight:
                height = jsonObject.getString("height");
                weight = jsonObject.getString("weight");
                //Get JSONArray of evolutions:
                JSONArray evos = jsonObject.getJSONArray("evolutions");
                if (evos.length() > 0) {
                    int level = 0;
                    String method;
                    String to;
                    Bundle tempBundle = new Bundle();

                    for (int j = 0; j < evos.length(); j++) {
                        JSONObject tempobj = evos.getJSONObject(j);
                        level = tempobj.getInt("level");
                        method = tempobj.getString("method");
                        to = tempobj.getString("to");
                        /*
                        System.out.println("Name        -------------------------" + name);
                        System.out.println("Bundle Array-------------------------" + evolutions.size());
                        System.out.println("JSON Array  -------------------------" + evos.length());
                        System.out.println("Level       -------------------------" + level);
                        System.out.println("Method      -------------------------" + method);
                        System.out.println("To          -------------------------" + to);
                        */
                        if (tempobj.has("level")) {
                            //evolutions.get(j).putInt("level", level);
                            tempBundle.putInt("level", level);
                            System.out.println("Put level");
                        }
                        if (method != null) {
                            //evolutions.get(j).putString("method", method);
                            tempBundle.putString("method", method);
                            System.out.println("Put method");
                        }
                        if (to != null) {
                            //evolutions.get(j).putString("to", to);
                            tempBundle.putString("to", to);
                            System.out.println("Put to");
                        }
                        evolutions.add(tempBundle);
                        /*
                        if (evos.getJSONObject(j).has("level")) {
                            evolutions[j].putInt("level", evos.getJSONObject(j).getInt("level"));
                        }
                        if (evos.getJSONObject(j).has("method")) {
                            evolutions[j].putString("method", evos.getJSONObject(j).getString("method"));
                        }
                        if (evos.getJSONObject(j).has("to")) {
                            evolutions[j].putString("to", evos.getJSONObject(j).getString("to"));
                        }
                        */
                    }

                }
            } catch (JSONException e) {
                Log.e("JSON LOADING", "Failed to read JSON: " + e);
            }
            pokelist.add(new Pokemon(i,
                    name,
                    type1,
                    type2,
                    hp,
                    atk,
                    def,
                    spatk,
                    spdef,
                    spd,
                    height,
                    weight,
                    evolutions));
        }
        for (Pokemon poke : pokelist) {
            System.out.println(poke.getSummary());
        }
        return pokelist;
    }
}
