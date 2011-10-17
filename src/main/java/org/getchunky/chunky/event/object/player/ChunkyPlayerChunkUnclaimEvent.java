package org.getchunky.chunky.event.object.player;

import org.bukkit.event.Cancellable;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.AccessLevel;

public class ChunkyPlayerChunkUnclaimEvent extends ChunkyPlayerChunkEvent implements Cancellable {

    private boolean cancel = false;

    public ChunkyPlayerChunkUnclaimEvent(ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, AccessLevel accessLevel) {
        super(Type.PLAYER_CHUNK_UNCLAIM, chunkyPlayer, chunkyChunk, accessLevel);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }
}
