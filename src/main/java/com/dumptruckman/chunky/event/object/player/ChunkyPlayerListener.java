package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.event.object.ChunkyObjectListener;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerListener extends ChunkyObjectListener {

    public ChunkyPlayerListener() {}

    public void onPlayerUnownedBreak(ChunkyPlayerDestroyEvent event) {}

    public void onPlayerUnownedBuild(ChunkyPlayerBuildEvent event) {}

    public void onPlayerItemUse(ChunkyPlayerItemUseEvent event) {}

    public void onPlayerSwitch(ChunkyPlayerSwitchEvent event) {}

    public void onPlayerChunkChange(ChunkyPlayerChunkChangeEvent event) {}
}
