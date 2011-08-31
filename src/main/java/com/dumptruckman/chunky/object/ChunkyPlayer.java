package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayer extends ChunkyObject {

    private ChunkyChunk lastChunk;

    public ChunkyPlayer(String name) {
        super(name);
    }

    public void setLastChunk(ChunkyChunk chunk) {
        this.lastChunk = chunk;
    }

    public ChunkyChunk getLastChunk() {
        return lastChunk;
    }

    public Player getPlayer() throws ChunkyPlayerOfflineException {
        Player player = Bukkit.getServer().getPlayer(this.getName());
        if(player == null) throw new ChunkyPlayerOfflineException();
        return player;
    }
}
