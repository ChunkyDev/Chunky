package com.dumptruckman.chunky.event.object;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.event.Cancellable;

public class ChunkyPlayerUnownedBuild extends ChunkyPlayerEvent implements Cancellable{

    private ChunkyChunk chunkyChunk;
    private boolean cancel = false;

    public ChunkyPlayerUnownedBuild(final ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk) {
        super(Type.PLAYER_UNOWNED_BUILD, chunkyPlayer);
        this.chunkyChunk = chunkyChunk;
    }

    public ChunkyChunk getChunkyChunk() {
        return this.chunkyChunk;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        cancel = b;
    }
}

