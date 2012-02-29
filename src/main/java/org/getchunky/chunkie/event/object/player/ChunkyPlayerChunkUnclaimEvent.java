package org.getchunky.chunkie.event.object.player;

import org.bukkit.event.Cancellable;
import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.AccessLevel;

public class ChunkyPlayerChunkUnclaimEvent extends ChunkyPlayerChunkEvent implements Cancellable {

    private boolean cancel = false;

    public ChunkyPlayerChunkUnclaimEvent(IChunkyPlayer chunkyPlayer, IChunkyChunk chunkyChunk, AccessLevel accessLevel) {
        super(Type.PLAYER_CHUNK_UNCLAIM, chunkyPlayer, chunkyChunk, accessLevel);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }
}
