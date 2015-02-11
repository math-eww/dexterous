package info.mattsaunders.apps.dexterous;

/**
 * Object to store egg group data
 */
public class EggGroup {

    private String name;
    private int resource;

    public EggGroup(String name, int resource) {
        this.name = name;
        this.resource = resource;
    }

    public String getEggGroupName() { return name; }
    public int getResource() { return resource; }
}
