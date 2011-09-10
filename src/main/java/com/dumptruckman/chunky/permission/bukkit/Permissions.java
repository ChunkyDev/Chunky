package com.dumptruckman.chunky.permission.bukkit;

import com.dumptruckman.chunky.util.Logging;
import org.bukkit.entity.Player;

/**
 * @author dumptruckman
 */
public enum Permissions {
    CHUNKY_CLAIM ("chunky.claim"),
    CHUNKY_UNCLAIM ("chunky.unclaim"),
    PLAYER_CHUNK_LIMIT ("chunky.chunk_claim_limit"),
    PLAYER_NO_CHUNK_LIMIT ("chunky.no_chunk_limit"),
    PLAYER_BUILD_ANYWHERE ("chunky.build_anywhere"),
    ENABLED("chunky.enabled"),
    ;

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

    public static boolean hasPerm(Player player, String node) {
        return player.hasPermission(node);
    }
}
