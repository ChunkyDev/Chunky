package org.getchunky.chunky.event.object.player;

import org.bukkit.event.Cancellable;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyAccessLevel;

public class ChunkyPlayerChunkClaimEvent extends ChunkyPlayerChunkEvent implements Cancellable{

    private boolean cancel = false;

    public ChunkyPlayerChunkClaimEvent(ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, ChunkyAccessLevel accessLevel) {
        super(Type.PLAYER_CHUNK_CLAIM, chunkyPlayer, chunkyChunk, accessLevel);
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean b) {
        this.cancel = b;
    }
}
