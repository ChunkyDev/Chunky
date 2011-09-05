package com.dumptruckman.chunky.object;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPermissible {

    protected ChunkyPermissibleObject object;
    protected EnumSet<Flags> flags;

    public ChunkyPermissible(ChunkyPermissibleObject object, Flags...flags) {
        this.object = object;
        this.flags = EnumSet.copyOf(Arrays.asList(flags));
    }

    public enum Flags {
        BUILD, DESTROY, ITEM_USE, SWITCH
    }

    public boolean contains(Flags flag) {
        return flags.contains(flag);
    }
}
