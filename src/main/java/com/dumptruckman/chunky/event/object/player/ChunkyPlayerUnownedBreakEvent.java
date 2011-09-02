package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

public class ChunkyPlayerUnownedBreakEvent extends ChunkyPlayerChunkEvent implements Cancellable{

    private Block block;
    private boolean cancel = false;

    public ChunkyPlayerUnownedBreakEvent(ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, Block block) {
        super(Type.PLAYER_UNOWNED_BUILD, chunkyPlayer, chunkyChunk);
        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        cancel = b;
    }
}

