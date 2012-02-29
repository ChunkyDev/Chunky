package org.getchunky.chunkie.permission.bukkit;

import org.bukkit.entity.Player;
import org.getchunky.chunkie.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunkie.object.IChunkyPlayer;

/**
 * @author dumptruckman
 */
public enum Permissions {
    CHUNKY_CLAIM("chunky.claim"),
    CHUNKY_UNCLAIM("chunky.unclaim"),
    PLAYER_CHUNK_LIMIT("chunky.chunk_claim_limit"),
    PLAYER_NO_CHUNK_LIMIT("chunky.admin.unlimited"),
    PLAYER_BUILD_ANYWHERE("chunky.admin.modify"),
    ADMIN_UNCLAIM("chunky.admin.unclaim"),
    ADMIN_SETPERM("chunky.admin.setperm"),
    ADMIN_SET_CHUNK_NAME("chunky.admin.set_chunk_name"),
    CMD_ADMIN("chunky.admin.command"),;

    String node;

    Permissions(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public boolean hasPerm(Player player) {
        //Logging.debug(player.getName() + " has perm: " + node + ": " + player.hasPermission(node));
        return player.hasPermission(node);
    }

    public boolean hasPerm(IChunkyPlayer cPlayer) {
        //Logging.debug(player.getName() + " has perm: " + node + ": " + player.hasPermission(node));
        try {
            return cPlayer.getPlayer().hasPermission(node);
        } catch (ChunkyPlayerOfflineException ignore) {
            return false;
        }
    }

    public static boolean hasPerm(Player player, String node) {
        return player.hasPermission(node);
    }

    public static boolean hasPerm(IChunkyPlayer chunkyPlayer, String node) {
        try {
            return chunkyPlayer.getPlayer().hasPermission(node);
        } catch (ChunkyPlayerOfflineException ignore) {
            return false;
        }
    }
}
