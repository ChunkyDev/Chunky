package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.permission.ChunkyPermissionType;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayerSwitchEvent extends ChunkyPlayerChunkEvent implements Cancellable {

    private Block switchedBlock;
    private boolean cancel = false;

    public ChunkyPlayerSwitchEvent(ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, Block switchedBlock, ChunkyPermissionType permissionType) {
        super(Type.PLAYER_SWITCH, chunkyPlayer, chunkyChunk, permissionType);
        this.switchedBlock = switchedBlock;
    }

    public final Block getSwitchedBlock() {
        return this.switchedBlock;
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
