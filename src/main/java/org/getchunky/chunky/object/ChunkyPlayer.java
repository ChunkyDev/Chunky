package org.getchunky.chunky.object;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.MapView;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkClaimEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerChunkUnclaimEvent;
import org.getchunky.chunky.event.object.player.ChunkyPlayerClaimLimitQueryEvent;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.permission.AccessLevel;
import org.getchunky.chunky.permission.bukkit.Permissions;
import org.getchunky.chunky.util.Logging;

import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyPlayer extends ChunkyPermissibleObject {

    private static HashSet<ChunkyPlayer> claimMode = new HashSet<ChunkyPlayer>();

    private static String BONUS_CHUNK_CLAIMS = "bonus chunks claims";
    private static String FIRST_LOGIN_TIME = "first login time";
    private static String LAST_LOGIN_TIME = "last login time";
    private static String LAST_LOGOUT_TIME = "last logout time";
    private static String COMMAND_MAP_ID = "command map id";

    public static HashSet<ChunkyPlayer> getClaimModePlayers() {
        return claimMode;
    }

    private ChunkyChunk currentChunk;

    public void setCurrentChunk(ChunkyChunk chunk) {
        this.currentChunk = chunk;
    }

    public ChunkyChunk getCurrentChunk() {
        try {this.currentChunk = ChunkyManager.getChunkyChunk(getPlayer().getLocation());} catch (Exception ignore) {}
        return this.currentChunk;
    }

    public Player getPlayer() throws ChunkyPlayerOfflineException {
        Player player = Bukkit.getServer().getPlayer(this.getName());
        if (player == null) throw new ChunkyPlayerOfflineException();
        return player;
    }

    public boolean isOnline() {
        try {
            getPlayer();
            return true;
        } catch (ChunkyPlayerOfflineException e) {return false;}
    }

    public void claimCurrentChunk() {
        this.claimChunk(this.getCurrentChunk());
    }

    public void claimChunk(ChunkyChunk chunkyChunk) {
        ChunkyPlayerChunkClaimEvent event = new ChunkyPlayerChunkClaimEvent(this, chunkyChunk, AccessLevel.UNOWNED);
        event.setCancelled(false);
        Chunky.getModuleManager().callEvent(event);
        if (event.isCancelled()) return;
        if (Permissions.CHUNKY_CLAIM.hasPerm(this)) {
            // Grab the chunk claim limit for the player
            int chunkLimit = getChunkClaimLimit();
            if (Permissions.PLAYER_NO_CHUNK_LIMIT.hasPerm(this) ||
                    !this.getOwnables().containsKey(ChunkyChunk.class.getName()) ||
                    this.getOwnables().get(ChunkyChunk.class.getName()).size() < chunkLimit) {
                if (chunkyChunk.isOwned()) {
                    Language.CHUNK_OWNED.bad(this, chunkyChunk.getOwner().getName());
                    return;
                }

                if (event.isCancelled()) return;
                chunkyChunk.setOwner(this, true, true);
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

    public void unclaimCurrentChunk() {
        this.unclaimChunk(this.getCurrentChunk());
    }

    public void unclaimChunk(ChunkyChunk chunkyChunk) {
        ChunkyPlayerChunkUnclaimEvent event = new ChunkyPlayerChunkUnclaimEvent(this, chunkyChunk, AccessLevel.UNOWNED);
        event.setCancelled(false);
        Chunky.getModuleManager().callEvent(event);
        if (event.isCancelled()) return;
        if (!chunkyChunk.isOwned() || (!chunkyChunk.isOwnedBy(this) && !Permissions.ADMIN_UNCLAIM.hasPerm(this))) {
            Language.CHUNK_NOT_OWNED.bad(this, chunkyChunk.getOwner().getName());
            return;
        }
        chunkyChunk.setOwner(this.getOwner(), true, true);
        chunkyChunk.setName("");
        Logging.debug(this.getName() + " claimed " + chunkyChunk.getCoord().getX() + ":" + chunkyChunk.getCoord().getZ());
        Language.CHUNK_UNCLAIMED.good(this, chunkyChunk.getCoord().getX(), chunkyChunk.getCoord().getZ());
    }

    public Integer getChunkClaimLimit() {
        ChunkyPlayerClaimLimitQueryEvent event = new ChunkyPlayerClaimLimitQueryEvent(this, Config.getPlayerChunkLimitDefault() + getData().optInt(BONUS_CHUNK_CLAIMS));
        Chunky.getModuleManager().callEvent(event);
        return event.getLimit();
    }

    public void setBonusChunkClaims(Integer limit) {
        getData().put(BONUS_CHUNK_CLAIMS, limit);
        save();
    }

    public Long getFirstLoginTime() {
        return getData().optLong(FIRST_LOGIN_TIME);
    }

    public Long getLastLoginTime() {
        return getData().optLong(LAST_LOGIN_TIME);
    }

    public Long getLastLogoutTime() {
        return getData().optLong(LAST_LOGOUT_TIME);
    }

    public MapView getCommandMap() {
        MapView map = null;
        Short mapId = (short) getData().optInt(COMMAND_MAP_ID);
        if (mapId == null) {
            map = Bukkit.getServer().createMap(Bukkit.getServer().getWorlds().get(0));
            getData().put(COMMAND_MAP_ID, map.getId());
        } else {
            map = Bukkit.getServer().getMap(mapId);
            if (map == null) {
                getData().remove(COMMAND_MAP_ID);
                Logging.warning("Commmand map id for player pointed to an invalid map");
                map = getCommandMap();
            }
        }
        return map;
    }
}
