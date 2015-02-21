package info.mattsaunders.apps.dexterous;

/**
 *  Object to store move data
 */
public class Move {

    private String name;
    private int resource;
    private String learn;
    private int level;
    private String type;

    public Move (String name, int resource, String learn, String type) {
        this.name = name;
        this.resource = resource;
        this.learn = learn;
        this.type = type;
    }

    public void setLevel(int level) { this.level = level; }

    public String getMoveName() { return name; }
    public int getResource() { return resource; }
    public String getLearnMethod() { return learn; }
    public int getLevel() { return level; }
    public String getType() { return type; }
}
