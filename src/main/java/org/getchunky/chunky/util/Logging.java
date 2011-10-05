package org.getchunky.chunky.util;

import org.bukkit.plugin.PluginDescriptionFile;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.config.Config;

import java.util.logging.Logger;

/**
 * @author dumptruckman, SwearWord
 */
public class Logging {
    final private static Logger LOG = Logger.getLogger("Minecraft.chunky");
    private static String NAME = "";
    private static String VERSION = "";

    /**
     * Prepares the log for use.
     */
    public static void load() {
        PluginDescriptionFile pdf = Chunky.getInstance().getDescription();
        NAME = pdf.getName();
        VERSION = pdf.getVersion();
    }

    /**
     * Adds the plugin name and optionally the version number to the log message.
     *
     * @param message     Log message
     * @param showVersion Whether to show version in log message
     * @return Modified message
     */
    private static String getString(String message, boolean showVersion) {
        String string = "[" + NAME;
        if (showVersion) string += " " + VERSION;
        return string += "] " + message;
    }

    /**
     * Returns the logger object.
     *
     * @return Logger object
     */
    public static Logger getLog() {
        return LOG;
    }

    /**
     * Returns the Name and Version as a combined string
     *
     * @return "$Name v$Version"
     */
    public static String getNameVersion() {
        return NAME + " " + VERSION;
    }

    /**
     * Info level logging.
     *
     * @param message Log message
     */
    public static void info(String message) {
        info(message, false);
    }

    /**
     * Info level logging.
     *
     * @param message     Log message
     * @param showVersion True adds version into message
     */
    public static void info(String message, boolean showVersion) {
        LOG.info(getString(message, showVersion));
    }

    /**
     * Info level logging.  Only displays if debug mode is enabled.
     *
     * @param message Log message
     */
    public static void debug(String message) {
        if (Config.isDebugging())
            LOG.info(getString(message, true));
    }

    /**
     * Warning level logging.
     *
     * @param message Log message
     */
    public static void warning(String message) {
        warning(message, false);
    }

    /**
     * Warning level logging.
     *
     * @param message     Log message
     * @param showVersion True adds version into message
     */
    public static void warning(String message, boolean showVersion) {
        LOG.warning(getString(message, showVersion));
    }

    /**
     * Severe level logging.
     *
     * @param message Log message
     */
    public static void severe(String message) {
        severe(message, false);
    }

    /**
     * Severe level logging.
     *
     * @param message     Log message
     * @param showVersion True adds version into message
     */
    public static void severe(String message, boolean showVersion) {
        LOG.severe(getString(message, showVersion));
    }

}
