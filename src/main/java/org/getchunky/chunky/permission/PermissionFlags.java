package org.getchunky.chunky.permission;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * @author dumptruckman
 */
public class PermissionFlags {

    private static HashMap<String, PermissionFlag> flagsMap = new HashMap<String, PermissionFlag>();
    private static HashMap<String, PermissionFlag> lookupMap = new HashMap<String, PermissionFlag>();

    public static PermissionFlag BUILD = new PermissionFlag("Build", "B");
    public static PermissionFlag DESTROY = new PermissionFlag("Destroy", "D");
    public static PermissionFlag ITEM_USE = new PermissionFlag("Item Use", "I");
    public static PermissionFlag SWITCH = new PermissionFlag("Switch", "S");

    static {
        Field[] fields = PermissionFlags.class.getFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())) {
                try {
                    Object value = f.get(null);
                    if (value instanceof PermissionFlag) {
                        PermissionFlag flag = (PermissionFlag)value;
                        flagsMap.put(flag.getName(), flag);
                        lookupMap.put(flag.getTag(), flag);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void registerPermissionFlag(PermissionFlag flag) {
        flagsMap.put(flag.getName(), flag);
        lookupMap.put(flag.getTag(), flag);
    }

    public static PermissionFlag getFlag(String name) {
        return flagsMap.get(name);
    }
}
