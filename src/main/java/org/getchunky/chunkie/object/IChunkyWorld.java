package org.getchunky.chunkie.object;

import org.getchunky.chunkie.config.Config;

/**
 * @author dumptruckman
 */
public class IChunkyWorld extends IChunkyObject {

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
    public IChunkyWorld setEnabled(boolean enabled) {
        getData().put("world enabled", enabled);
        return this;
    }
}
