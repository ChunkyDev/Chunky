package org.getchunky.chunky.util;

import org.getchunky.chunky.config.Config;

/**
 * @author dumptruckman, SwearWord
 */
public class MinecraftTools {

    /**
     * Converts seconds to ticks.
     *
     * @param seconds
     * @return seconds * 20 (ticks)
     */
    public static long convertSecondsToTicks(long seconds) {
        return seconds * 20;
    }

    /**
     * Determines if an item is a switchable item
     *
     * @param id Item id
     * @return true if item can be switched
     */
    public static Boolean isSwitchable(int id) {
        return Config.getSwitchables().contains(Integer.toString(id));
    }

    /**
     * Determines if an item is usable
     *
     * @param id Item id
     * @return true if item can be switched
     */
    public static Boolean isUsable(int id) {
        return Config.getUsables().contains(Integer.toString(id));
    }
}
