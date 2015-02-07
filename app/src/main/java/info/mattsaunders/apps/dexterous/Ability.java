package info.mattsaunders.apps.dexterous;

import java.io.Serializable;

/**
 * Object to store ability info
 */
public class Ability implements Serializable {
    private String name;
    private String resource;

    public Ability(String name, String resource) {
        this.name = name;
        this.resource = resource;
    }

    public String getAbilityName() { return name; }
    public String getResource() { return resource; }
}