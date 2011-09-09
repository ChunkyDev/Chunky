package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.permission.ChunkyPermissionType;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

public class ChunkyPlayerBuildEvent extends ChunkyPlayerChunkEvent implements Cancellable{

    private Block block;
    private boolean cancel = false;

    public ChunkyPlayerBuildEvent(ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, Block block, ChunkyPermissionType permissionType) {
        super(Type.PLAYER_BUILD, chunkyPlayer, chunkyChunk, permissionType);
        this.block = block;
    }

    public Block getBlock() {
        return this.block;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    public boolean isCancelled() {
        return cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @param b true if you wish to cancel this event
     */
    public void setCancelled(boolean b) {
        cancel = b;
    }
}

