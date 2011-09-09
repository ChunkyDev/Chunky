package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.permission.ChunkyPermissionType;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class ChunkyPlayerItemUseEvent extends ChunkyPlayerChunkEvent implements Cancellable{

    private ItemStack itemUsed;
    private boolean cancel = false;

    public ChunkyPlayerItemUseEvent(ChunkyPlayer chunkyPlayer, ChunkyChunk chunkyChunk, ItemStack itemUsed, ChunkyPermissionType permissionType) {
        super(Type.PLAYER_BUILD, chunkyPlayer, chunkyChunk, permissionType);
        this.itemUsed= itemUsed;
    }

    /**
     * @return Returns the ItemStack that the player right clicked with.
     */
    public ItemStack getItemUsed() {
        return this.itemUsed;
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

