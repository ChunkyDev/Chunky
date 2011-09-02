package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.event.object.ChunkyObjectListener;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerListener extends ChunkyObjectListener {

    public ChunkyPlayerListener() {}

    public void onPlayerUnownedBreak(ChunkyPlayerUnownedBreakEvent event) {}

    public void onPlayerUnownedBuild(ChunkyPlayerUnownedBuildEvent event) {}

    public void onPlayerChunkChange(ChunkyPlayerChunkChangeEvent event) {}
}
