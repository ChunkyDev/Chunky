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

        /**
         * Retrieves the character representation of this flag
         * 
         * @return character representation of flag
         */
        private char getRep() {
            return rep;
        }

        /**
         * Retrieves a flag represented by the specified character
         * @param c Character representation of flag
         * @return Flag represented by c or null if no flag for c
         */
        public static Flags get(char c) {
            return lookup.get(c);
        }
    }

    protected EnumSet<Flags> flags;

    /**
     * Instantiates a ChunkyPermissions object.
     *
     * @param flags Optional parameter to indicate initial flags
     */
    public ChunkyPermissions(Flags... flags) {
        if (flags.length == 0) {
            this.flags = null;
            return;
        }
        this.flags.addAll(Arrays.asList(flags));
    }

    /**
     * Checks to see if this permission set has a certain flag.
     *
     * @param flag Flag to check for
     * @return true if contains, false if not contains, null if no permissions set at all
     */
    public Boolean contains(Flags flag) {
        if (flags == null) return null;
        return flags.contains(flag);
    }

    /**
     * Returns the set of flags
     *
     * @return Flag set
     */
    public EnumSet<Flags> getFlags() {
        return flags;
    }

    /**
     * Wipes the flag set
     */
    public void clearFlags() {
        flags = null;
    }

    /**
     * Sets the status of a certain flag in this permission set
     *
     * @param flag Flag to set
     * @param status Status of the flag
     */
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

    /**
     * Sets all the flags at once
     *
     * @param flags Set of new flags
     */
    public void setFlags(EnumSet<Flags> flags) {
        this.flags = flags;
    }

    /**
     * Returns a string representation of the permissions flags
     *
     * @return String representation of permission flags
     */
    public String toString() {
        if (flags == null) return Language.NO_PERMISSIONS_SET.getString();
        if (flags.isEmpty()) return Language.NO_PERMISSIONS_GRANTED.getString();
        String sFlags = "";
        for (Flags flag : flags) {
            if (!sFlags.isEmpty()) {
                sFlags += ", ";
            }
            sFlags += flag.toString();
        }
        return sFlags;
    }

    /**
     * Returns a string representation of the permissions flags that is shorter than the normal toString()
     *
     * @return Small string representation of permission flags
     */
    public String toSmallString() {
        if (flags == null) return Language.NO_PERMISSIONS_SET.getString();
        if (flags.isEmpty()) return Language.NO_PERMISSIONS_GRANTED.getString();
        String sFlags = "";
        for (Flags flag : flags) {
            if (!sFlags.isEmpty()) {
                sFlags += ", ";
            }
            sFlags += Character.toString(flag.getRep()).toUpperCase();
        }
        return sFlags;
    }
}
