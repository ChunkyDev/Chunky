package com.dumptruckman.chunky.util;

import com.dumptruckman.chunky.Chunky;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.logging.Logger;

/**
 * @author dumptruckman, SwearWord
 */
public class Logging {
    final private static Logger LOG = Logger.getLogger("Minecraft.chunky");
    private static String NAME_VERSION = "";

    public static void load(Chunky chunky) {
        PluginDescriptionFile pdf = chunky.getDescription();
        NAME_VERSION = pdf.getName() + " " + pdf.getVersion() + " ";
    }

    public static Logger getLog() {
        return LOG;
    }

    public static String getNameVersion() {
        return NAME_VERSION;
    }

    public static void info(String message) {
        LOG.info(NAME_VERSION + message);
    }

    public static void severe(String message) {
        LOG.severe(NAME_VERSION + message);
    }

}
