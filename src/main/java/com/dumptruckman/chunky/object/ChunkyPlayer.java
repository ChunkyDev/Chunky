package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayer extends ChunkyObject {

    private ChunkyChunk currentChunk;

    public ChunkyPlayer(String name) {
        super(name);
    }

    public void setCurrentChunk(ChunkyChunk chunk) {
        this.currentChunk = chunk;
    }

    public ChunkyChunk getCurrentChunk() {
        return this.currentChunk;
    }

    public Player getPlayer() throws ChunkyPlayerOfflineException {
        Player player = Bukkit.getServer().getPlayer(this.getName());
        if(player == null) throw new ChunkyPlayerOfflineException();
        return player;
    }
}
