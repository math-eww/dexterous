package info.mattsaunders.apps.dexterous;

/**
 * Object to store ability info
 */
public class Ability {

    private String name;
    private int resource;

    public Ability(String name, int resource) {
        this.name = name;
        this.resource = resource;
    }

    public String getAbilityName() { return name; }
    public int getResource() { return resource; }
}
