package info.mattsaunders.apps.dexterous;

import android.os.Bundle;

import java.io.File;
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
    private boolean hasSprite = false;
    private File spriteFile;
    private boolean pokeballToggle1 = false;
    private boolean pokeballToggle2 = false;
    private boolean pokeballToggle3 = false;

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

    public int[] getStats() { return new int[]{hp, atk, def, spatk, spdef, spd }; }
    public String getHeight() { return height; }
    public String getWeight() { return weight; }
    public ArrayList<Bundle> getEvolutions() { return evolutions; }

    public boolean isHasSprite() { return hasSprite; }
    public void setSprite(File spriteF) { spriteFile = spriteF; hasSprite = true; }
    public File getSpriteFile() { return spriteFile; }

    public void setPokeballToggle1(boolean value) { pokeballToggle1 = value; }
    public void setPokeballToggle2(boolean value) { pokeballToggle2 = value; }
    public void setPokeballToggle3(boolean value) { pokeballToggle3 = value; }
    public boolean getPokeballToggle1() { return pokeballToggle1; }
    public boolean getPokeballToggle2() { return pokeballToggle2; }
    public boolean getPokeballToggle3() { return pokeballToggle3; }

    public String getSummary() {
        String evolves = "";
        evolves = evolutions.toString();
        return String.valueOf(number) + " " + name + " " + typeOne + " " + typeTwo + " -- " + evolves + "\n"
                + "__________" + " HP: " + hp + " ATK: " + atk + " DEF: " + def + " SPATK: " + spatk + " SPDEF: " + spdef + " SPD " + spd;
    }
}
