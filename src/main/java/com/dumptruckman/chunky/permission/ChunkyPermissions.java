package com.dumptruckman.chunky.permission;

import com.dumptruckman.chunky.locale.Language;

import java.util.*;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPermissions {

    public enum Flags {
        BUILD('b'), DESTROY('d'), ITEMUSE('i'), SWITCH('s');

        private char rep;

        private static final Map<Character, Flags> lookup = new HashMap<Character, Flags>();

        static {
            for(Flags f : EnumSet.allOf(Flags.class))
                lookup.put(f.getRep(), f);
        }

        Flags(char rep) {
            this.rep = rep;
        }

        private char getRep() {
            return rep;
        }

        public static Flags get(char c) {
            return lookup.get(c);
        }
    }

    protected EnumSet<Flags> flags;

    public ChunkyPermissions(Flags... flags) {
        if (flags.length == 0) {
            this.flags = null;
            return;
        }
        this.flags.addAll(Arrays.asList(flags));
    }

    public Boolean contains(Flags flag) {
        if (flags == null) return null;
        return flags.contains(flag);
    }

    public EnumSet<Flags> getFlags() {
        return flags;
    }

    public void clearFlags() {
        flags = null;
    }

    public void setFlag(Flags flag, boolean status) {
        if (flags == null) {
            flags = EnumSet.noneOf(Flags.class);
        }
        if (flags.contains(flag) && !status) {
            flags.remove(flag);
        } else if (!flags.contains(flag) && status) {
            flags.add(flag);
        }
    }

    public void setFlags(EnumSet<Flags> flags) {
        this.flags = flags;
    }

    public String toString() {
        if (flags == null) return Language.NO_PERMISSIONS_SET.getString();
        String sFlags = "";
        for (Flags flag : flags) {
            if (!sFlags.isEmpty()) {
                sFlags += ", ";
            }
            sFlags += flag.toString();
        }
        return sFlags;
    }
}
