package org.getchunky.chunkie.event.object.player;

import org.bukkit.event.Cancellable;
import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.AccessLevel;

public class ChunkyPlayerChunkClaimEvent extends ChunkyPlayerChunkEvent implements Cancellable {

    private boolean cancel = false;

    public ChunkyPlayerChunkClaimEvent(IChunkyPlayer chunkyPlayer, IChunkyChunk chunkyChunk, AccessLevel accessLevel) {
        super(Type.PLAYER_CHUNK_CLAIM, chunkyPlayer, chunkyChunk, accessLevel);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }
}
