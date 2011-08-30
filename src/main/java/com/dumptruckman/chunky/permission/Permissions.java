package com.dumptruckman.chunky.permission;

import org.bukkit.entity.Player;

/**
 * @author dumptruckman
 */
public enum Permissions {
    CHUNKY_CLAIM ("chunky.claim"),
    PLAYER_CHUNK_LIMIT ("chunky.chunk_claim_limit"),
    PLAYER_NO_CHUNK_LIMIT ("chunky.no_chunk_limit"),
    ;

    String node;

    Permissions(String node) {
        this.node = node;
    }

    public String getNode() {
        return node;
    }

    public boolean hasPerm(Player player) {
        return player.isOp() || player.hasPermission(node);
    }

    public static boolean hasPerm(Player player, String node) {
        return player.isOp() || player.hasPermission(node);
    }
}
