package info.mattsaunders.apps.dexterous;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Pokemon object to store info for pokemon
 */
public class Pokemon {
    private int number;
    private String name;
    private String typeOne;
    private String typeTwo;
    private int hp;
    private int atk;
    private int def;
    private int spatk;
    private int spdef;
    private int spd;
    private String height;
    private String weight;
    private ArrayList<Bundle> evolutions;

    public Pokemon(int number,
                   String name,
                   String typeOne,
                   String typeTwo,
                   int hp,
                   int atk,
                   int def,
                   int spatk,
                   int spdef,
                   int spd,
                   String height,
                   String weight,
                   ArrayList<Bundle> evolutions) {
        this.number = number;
        this.name = name;
        this.typeOne = typeOne;
        this.typeTwo = typeTwo;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spatk = spatk;
        this.spdef = spdef;
        this.spd = spd;
        this.height = height;
        this.weight = weight;
        this.evolutions = evolutions;
    }

    public int getNumber() { return number; }
    public String getStringNumber() { return String.valueOf(number); }
    public String getName() { return name; }
    public String getTypeOne() { return typeOne; }
    public String getTypeTwo() { return typeTwo; }

    public String getSummary() {
        String evolves = "";
        /*
        for (Bundle evo : evolutions) {
            if (evo.containsKey("to")) {
                evolves = evolves + evo.getString("to");
            }
        }
        */
        evolves = evolutions.toString();
        return String.valueOf(number) + " " + name + " " + typeOne + " " + typeTwo + " -- " + evolves + "\n"
                + "__________" + " HP: " + hp + " ATK: " + atk + " DEF: " + def + " SPATK: " + spatk + " SPDEF: " + spdef + " SPD " + spd;
    }
}
