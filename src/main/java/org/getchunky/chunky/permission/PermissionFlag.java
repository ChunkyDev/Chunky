package org.getchunky.chunky.permission;

/**
 * @author dumptruckman
 */
public class PermissionFlag {

    String name;
    String tag;

    public PermissionFlag(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }


}
