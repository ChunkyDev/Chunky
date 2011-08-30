package com.dumptruckman.chunky.event.object;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

public class ChunkyPlayerUnownedBuild extends ChunkyPlayerEvent implements Cancellable{

    private ChunkyChunk chunkyChunk;
    private Block block;
    private boolean cancel = false;

    public ChunkyPlayerUnownedBuild(final ChunkyPlayer chunkyPlayer, final ChunkyChunk chunkyChunk, final Block block) {
        super(Type.PLAYER_UNOWNED_BUILD, chunkyPlayer);
        this.chunkyChunk = chunkyChunk;
        this.block = block;
    }

    public ChunkyChunk getChunkyChunk() {
        return this.chunkyChunk;
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

