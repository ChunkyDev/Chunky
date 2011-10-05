package org.getchunky.chunky.object;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkClaimEvent;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.permission.ChunkyAccessLevel;
import org.getchunky.chunky.permission.bukkit.Permissions;
import org.getchunky.chunky.util.Logging;

import java.util.HashSet;
import java.util.Map;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayer extends ChunkyPermissibleObject {

    private static HashSet<ChunkyPlayer> claimMode = new HashSet<ChunkyPlayer>();

    public static HashSet<ChunkyPlayer> getClaimModePlayers() {
        return claimMode;
    }

    private ChunkyChunk currentChunk;

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

    public void claimCurrentChunk() {
        ChunkyChunk chunkyChunk = this.getCurrentChunk();
        ChunkyPlayerChunkClaimEvent event = new ChunkyPlayerChunkClaimEvent(this,chunkyChunk, ChunkyAccessLevel.UNOWNED);
        event.setCancelled(false);
        Chunky.getModuleManager().callEvent(event);
        if(event.isCancelled()) return;
        if (Permissions.CHUNKY_CLAIM.hasPerm(this)) {
            // Grab the chunk claim limit for the player
            int chunkLimit = Config.getPlayerChunkLimitDefault();
            for (Map.Entry<String,Integer> limit : Config.getCustomPlayerChunkLimits().entrySet()) {
                if (Permissions.hasPerm(this, Permissions.PLAYER_CHUNK_LIMIT.getNode() + "." + limit.getKey())) {
                    chunkLimit = limit.getValue();
                    break;
                }
            }
            if (Permissions.PLAYER_NO_CHUNK_LIMIT.hasPerm(this) ||
                    !this.getOwnables().containsKey(ChunkyChunk.class.getName()) ||
                    this.getOwnables().get(ChunkyChunk.class.getName()).size() < chunkLimit) {
                if (chunkyChunk.isOwned()) {
                    Language.CHUNK_OWNED.bad(this, chunkyChunk.getOwner().getName());
                    return;
                }

                if(event.isCancelled()) return;
                chunkyChunk.setOwner(this, true,true);
                chunkyChunk.setName("");
                Logging.debug(this.getName() + " claimed " + chunkyChunk.getCoord().getX() + ":" + chunkyChunk.getCoord().getZ());
                Language.CHUNK_CLAIMED.good(this, chunkyChunk.getCoord().getX(), chunkyChunk.getCoord().getZ());
            } else {
                Language.CHUNK_LIMIT_REACHED.bad(this, chunkLimit);
            }
        } else {
            Language.NO_COMMAND_PERMISSION.bad(this);
        }
    }
}
