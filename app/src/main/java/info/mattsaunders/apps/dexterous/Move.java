package info.mattsaunders.apps.dexterous;

import java.io.Serializable;

/**
 *  Object to store move data
 */
public class Move implements Serializable {

    private static final long serialVersionUID = -29238982928392L;
    
    private String name;
    private String resource;
    private String learn;
    private int level;

    public Move (String name, String resource, String learn) {
        this.name = name;
        this.resource = resource;
        this.learn = learn;
    }

    public void setLevel(int level) { this.level = level; }

    public String getMoveName() { return name; }
    public String getResource() { return resource; }
    public String getLearnMethod() { return learn; }
    public int getLevel() { return level; }
}
