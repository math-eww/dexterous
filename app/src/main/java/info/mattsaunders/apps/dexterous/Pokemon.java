package info.mattsaunders.apps.dexterous;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Pokemon object to store info for pokemon
 */
public class Pokemon implements Serializable {

    private static final long serialVersionUID = -29238982928391L;

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
    private ArrayList<Evolution> evolutions;
    private String evolvesFrom = "";
    private int evolvesFromNum;
    private ArrayList<Move> moveset;
    private ArrayList<EggGroup> eggTypes;
    private ArrayList<Ability> abilities;
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
                   ArrayList<Evolution> evolutions,
                   ArrayList<Move> moveset,
                   ArrayList<EggGroup> eggTypes,
                   ArrayList<Ability> abilities) {
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
        this.moveset = moveset;
        this.eggTypes = eggTypes;
        this.abilities = abilities;
    }

    public int getNumber() { return number; }
    public String getStringNumber() { return String.valueOf(number); }
    public String getThreeDigitStringNumber() { String strNum = String.valueOf(number); return ("000" + strNum).substring(strNum.length()); }
    public String getName() { return name; }
    public String getTypeOne() { return typeOne; }
    public String getTypeTwo() { return typeTwo; }

    public int[] getStats() { return new int[]{hp, atk, def, spatk, spdef, spd }; }
    public String getHeight() { return height; }
    public String getWeight() { return weight; }
    public ArrayList<Evolution> getEvolutions() { return evolutions; }
    public ArrayList<Ability> getAbilities() { return abilities; }
    public ArrayList<Move> getMoveset() { return moveset; }
    public ArrayList<EggGroup> getEggTypes() { return eggTypes; }

    public void setEvolvesFrom(String evoFrom) { evolvesFrom = evoFrom; }
    public String getEvolvesFrom() { return evolvesFrom; }
    public void setEvolvesFromNum(int evoFromNum) { evolvesFromNum = evoFromNum; }
    public int getEvolvesFromNum() { return evolvesFromNum; }

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
        String evolves = evolutions.toString();
        return String.valueOf(number) + " " + name + " " + typeOne + " " + typeTwo + " -- " + evolves + "\n"
                + "__________" + " HP: " + hp + " ATK: " + atk + " DEF: " + def + " SPATK: " + spatk + " SPDEF: " + spdef + " SPD " + spd;
    }
}
