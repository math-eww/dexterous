package info.mattsaunders.apps.dexterous;

import java.util.ArrayList;

/**
 * Evolution loader - iterate through pokemon list and add evolved from variable
 */
public class LoadEvoFrom {
    public static void loadEvoFrom() {
        ArrayList<Pokemon> pokemonList = MainActivity.pokemonList;
        for (Pokemon poke : pokemonList) {
            ArrayList<Evolution> evolutionList = poke.getEvolutions();
            for (Evolution evoBundle : evolutionList) {
                String to = evoBundle.getEvolvesTo();
                String num = evoBundle.getNum();
                String detail = "";
                if (!evoBundle.getDetail().equals("")) {
                    detail = evoBundle.getDetail();
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
