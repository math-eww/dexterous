package info.mattsaunders.apps.dexterous;

import java.io.Serializable;

/**
 * Object to store egg group data
 */
public class EggGroup implements Serializable {
    private String name;
    private String resource;

    public EggGroup(String name, String resource) {
        this.name = name;
        this.resource = resource;
    }

    public String getEggGroupName() { return name; }
    public String getResource() { return resource; }
}
