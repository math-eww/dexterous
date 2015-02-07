package info.mattsaunders.apps.dexterous;

import java.io.Serializable;

/**
 * Object to store pokemon evolution info
 */
public class Evolution implements Serializable {
    private String method;
    private String to;
    private String num;
    private String detail;
    private int level;

    public Evolution(String method, String to, String num, String detail, int level) {
        this.method = method;
        this.to = to;
        this.num = num;
        this.detail = detail;
        this.level = level;
    }

    public String getMethod() { return method; }
    public String getEvolvesTo() { return to; }
    public String getNum() { return num; }
    public String getDetail() { return detail; }
    public int getLevel() { return level; }
}
