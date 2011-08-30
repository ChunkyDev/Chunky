package com.dumptruckman.chunky.object;

import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayer extends ChunkyObject implements ChunkyChunkOwner {

    private ChunkyChunk lastChunk;
    private HashSet<ChunkyChunk> chunks;

    public ChunkyPlayer(String name) {
        chunks = new HashSet<ChunkyChunk>();
        setName(name);
    }

    public void setLastChunk(ChunkyChunk chunk) {
        this.lastChunk = chunk;
    }

    public ChunkyChunk getLastChunk() {
        return lastChunk;
    }

    public void addChunk(ChunkyChunk chunk) {
        if (!chunks.contains(chunk)) {
            chunks.add(chunk);
        } else {
            // TODO
        }
    }

    public boolean removeChunk(ChunkyChunk chunk) {
        return chunks.remove(chunk);
    }

    public boolean ownsChunk(ChunkyChunk chunk) {
        return chunks.contains(chunk);
    }

    public Player getPlayer() throws ChunkyPlayerOfflineException {
        Player player = Bukkit.getServer().getPlayer(this.getName());
        if(player == null) throw new ChunkyPlayerOfflineException();
        return player;
    }
}
