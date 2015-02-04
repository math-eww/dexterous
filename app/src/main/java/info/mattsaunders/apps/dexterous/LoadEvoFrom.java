package info.mattsaunders.apps.dexterous;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Evolution loader - iterate through pokemon list and add evolved from variable
 */
public class LoadEvoFrom {
    public static void loadEvoFrom() {
        ArrayList<Pokemon> pokemonList = MainActivity.pokemonList;
        for (Pokemon poke : pokemonList) {
            ArrayList<Bundle> evolutionList = poke.getEvolutions();
            for (Bundle evoBundle : evolutionList) {
                String to = evoBundle.getString("to");
                String num = evoBundle.getString("num");
                String detail = "";
                if (evoBundle.containsKey("detail")) {
                    detail = evoBundle.getString("detail");
                }
                if (!detail.equals("mega")) {
                    Pokemon targetPoke = pokemonList.get(Integer.parseInt(num) - 1);
                    targetPoke.setEvolvesFrom(poke.getName());
                    targetPoke.setEvolvesFromNum(poke.getNumber());
                    //System.out.println(poke.getName() + " evolves to " + to + ": " + num);
                    //System.out.println("Setting to target: " + targetPoke.getName() + ": " + targetPoke.getNumber());
                }
            }
        }
    }
}
