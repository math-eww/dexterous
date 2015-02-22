package info.mattsaunders.apps.dexterous;

/**
 * Object to store ability info
 */
public class Ability {

    private String name;
    private int resource;
    private boolean isHidden = false;

    public Ability(String name, int resource, int isHidden) {
        this.name = name;
        this.resource = resource;
        if (isHidden == 1) { this.isHidden = true; }
    }

    public String getAbilityName() { return name; }
    public int getResource() { return resource; }
    public boolean getIsHidden() { return isHidden; }
}
