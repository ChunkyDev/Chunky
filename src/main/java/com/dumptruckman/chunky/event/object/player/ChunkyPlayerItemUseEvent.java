package com.dumptruckman.chunky.event.object.player;

import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public class ChunkyPlayerItemUseEvent extends ChunkyPlayerChunkEvent implements Cancellable{

    private ItemStack itemUsed;
    private boolean cancel = false;

    public ChunkyPlayerItemUseEvent(final ChunkyPlayer chunkyPlayer, final ChunkyChunk chunkyChunk, final ItemStack itemUsed) {
        super(Type.PLAYER_UNOWNED_BUILD, chunkyPlayer, chunkyChunk);
        this.itemUsed= itemUsed;
    }

    /**
     * @return Returns the ItemStack that the player right clicked with.
     */
    public ItemStack getItemUsed() {
        return this.itemUsed;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean b) {
        cancel = b;
    }
}

