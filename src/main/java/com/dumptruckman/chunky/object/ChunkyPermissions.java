package com.dumptruckman.chunky.object;

import java.util.Arrays;
import java.util.EnumSet;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPermissions {

    public enum Flags {
        BUILD, DESTROY, ITEM_USE, SWITCH
    }

    protected EnumSet<Flags> flags;

    public ChunkyPermissions(Flags... flags) {
        this.flags = EnumSet.copyOf(Arrays.asList(flags));
    }

    public boolean contains(Flags flag) {
        return flags.contains(flag);
    }

    public void setFlag(Flags flag, boolean status) {
        if (flags.contains(flag) && !status) {
            flags.remove(flag);
        } else if (!flags.contains(flag) && status) {
            flags.add(flag);
        }
    }
}
