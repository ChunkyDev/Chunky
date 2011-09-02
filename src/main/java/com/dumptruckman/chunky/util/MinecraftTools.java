package com.dumptruckman.chunky.util;

import com.dumptruckman.chunky.config.Config;

/**
 * @author dumptruckman, SwearWord
 */
public class MinecraftTools {

    public static long convertSecondsToTicks(long seconds) {
        return seconds * 20;
    }

    public static Boolean isSwitchable(int id) {
        return Config.getSwitchables().contains(Integer.toString(id));
    }

    public static Boolean isUsable(int id) {
        return Config.getUsables().contains(Integer.toString(id));
    }
}
