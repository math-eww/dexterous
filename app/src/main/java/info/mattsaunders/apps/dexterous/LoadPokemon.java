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
    public static ArrayList<Pokemon> buildPokeList(Bundle pokeball1, Bundle pokeball2, Bundle pokeball3) {
        ArrayList<Pokemon> pokelist = new ArrayList();
        Utilities.SUBDIR = "POKEMON/";
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
            ArrayList<Bundle> abilities = new ArrayList();
            ArrayList<Bundle> moveset = new ArrayList();
            ArrayList<Bundle> eggs = new ArrayList();
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
                    String num;
                    String detail;
                    for (int j = 0; j < evos.length(); j++) {
                        Bundle tempBundle = new Bundle();
                        JSONObject tempobj = evos.getJSONObject(j);
                        if (tempobj.has("level")) {
                            level = tempobj.getInt("level");
                            tempBundle.putInt("level", level);
                        }
                        if (tempobj.has("method")) {
                            method = tempobj.getString("method");
                            if (method != null) {
                                tempBundle.putString("method", method);
                            }
                        }
                        if (tempobj.has("to")) {
                            to = tempobj.getString("to");
                            if (to != null) {
                                tempBundle.putString("to", to);
                            }
                        }
                        if (tempobj.has("resource_uri")) {
                            num = tempobj.getString("resource_uri");
                            num = num.substring(16, num.length() - 1);
                            if (num != null) {
                                tempBundle.putString("num", num);
                            }
                        }
                        if (tempobj.has("detail")) {
                            detail = tempobj.getString("detail");
                            if (detail != null) {
                                tempBundle.putString("detail", detail);
                            }
                        }
                        evolutions.add(tempBundle);
                    }

                }

                //Get JSONArray of abilities:
                JSONArray abils = jsonObject.getJSONArray("abilities");
                if (abils.length() > 0) {
                    for (int j = 0; j < abils.length(); j++) {
                        Bundle tempBundle = new Bundle();
                        JSONObject tempobj = abils.getJSONObject(j);
                        tempBundle.putString("name",tempobj.getString("name"));
                        tempBundle.putString("resource",tempobj.getString("resource_uri"));
                        abilities.add(tempBundle);
                    }

                }

                //Get JSONArray of moves:
                JSONArray moves = jsonObject.getJSONArray("moves");
                if (moves.length() > 0) {
                    for (int j = 0; j < moves.length(); j++) {
                        Bundle tempBundle = new Bundle();
                        JSONObject tempobj = moves.getJSONObject(j);
                        tempBundle.putString("name",tempobj.getString("name"));
                        tempBundle.putString("resource",tempobj.getString("resource_uri"));
                        tempBundle.putString("learn",tempobj.getString("learn_type"));
                        if (tempobj.has("level")) { tempBundle.putInt("level",tempobj.getInt("level")); }
                        moveset.add(tempBundle);
                    }

                }

                //Get JSONArray of eggtypes:
                JSONArray eggType = jsonObject.getJSONArray("egg_groups");
                if (eggType.length() > 0) {
                    for (int j = 0; j < eggType.length(); j++) {
                        Bundle tempBundle = new Bundle();
                        JSONObject tempobj = eggType.getJSONObject(j);
                        tempBundle.putString("name",tempobj.getString("name"));
                        tempBundle.putString("resource",tempobj.getString("resource_uri"));
                        eggs.add(tempBundle);
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
                    evolutions,
                    moveset,
                    eggs,
                    abilities));
        }
        for (Pokemon poke : pokelist) {
            //System.out.println(poke.getSummary());
            //Set toggle states here
            if (pokeball1 != null) {
                if (pokeball1.getInt(poke.getStringNumber()) == 1) {
                    poke.setPokeballToggle1(true);
                    MainActivity.caughtDex++;
                }
            }
            if (pokeball2 != null) {
                if (pokeball2.getInt(poke.getStringNumber()) == 1) {
                    poke.setPokeballToggle2(true);
                    MainActivity.livingDex++;
                }
            }
            if (pokeball3 != null) {
                if (pokeball3.getInt(poke.getStringNumber()) == 1) {
                    poke.setPokeballToggle3(true);
                }
            }
        }
        Utilities.SUBDIR = "";
        System.out.println("Loaded Pokemon: " + pokelist.size());
        return pokelist;
    }
}
