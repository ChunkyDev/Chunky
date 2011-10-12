package org.getchunky.chunky.object;

import org.getchunky.chunky.config.Config;

/**
 * @author dumptruckman
 */
public class ChunkyWorld extends ChunkyObject {

    /**
     * Checks whether Chunky is enabled for this world
     *
     * @return true if Chunky is enabled for this world
     */
    public Boolean isEnabled() {
        if (!getData().has("world enabled")) {
            getData().put("world enabled", Config.getDefaultWorldEnabled());
            save();
        }
        return getData().getBoolean("world enabled");
    }

    /**
     * Sets whether Chunky is enabled for this world
     *
     * @param enabled true to enable Chunky for this world
     * @return this World
     */
    public ChunkyWorld setEnabled(boolean enabled) {
        getData().put("world enabled", enabled);
        return this;
    }
}
