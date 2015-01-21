package info.mattsaunders.apps.dexterous;

/**
 * Pokemon object to store info for pokemon
 */
public class Pokemon {
    private int number;
    private String name;
    private String typeOne;
    private String typeTwo;

    public Pokemon(int number, String name, String typeOne, String typeTwo) {
        this.number = number;
        this.name = name;
        this.typeOne = typeOne;
        this.typeTwo = typeTwo;
    }

    public int getNumber() { return number; }
    public String getStringNumber() { return String.valueOf(number); }
    public String getName() { return name; }
    public String getTypeOne() { return typeOne; }
    public String getTypeTwo() { return typeTwo; }
    public String getSummary() { return String.valueOf(number) + " " + name + " " + typeOne + " " + typeTwo; }
}
