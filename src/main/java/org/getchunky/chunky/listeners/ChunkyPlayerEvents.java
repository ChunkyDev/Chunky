package org.getchunky.chunky.listeners;

import org.getchunky.chunky.event.object.player.*;
import org.getchunky.chunky.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunky.permission.AccessLevel;
import org.getchunky.chunky.permission.bukkit.Permissions;

public class ChunkyPlayerEvents extends ChunkyPlayerListener {
    @Override
    public void onPlayerUnownedBreak(ChunkyPlayerDestroyEvent event) {
        try {
            if (!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if (event.getAccessLevel().equals(AccessLevel.NONE)) event.setCancelled(true);
    }

    @Override
    public void onPlayerUnownedBuild(ChunkyPlayerBuildEvent event) {
        try {
            if (!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if (event.getAccessLevel().equals(AccessLevel.NONE)) event.setCancelled(true);
    }

    @Override
    public void onPlayerItemUse(ChunkyPlayerItemUseEvent event) {
        try {
            if (!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if (event.getAccessLevel().equals(AccessLevel.NONE)) event.setCancelled(true);
    }

    @Override
    public void onPlayerSwitch(ChunkyPlayerSwitchEvent event) {
        try {
            if (!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if (event.getAccessLevel().equals(AccessLevel.NONE)) event.setCancelled(true);
    }


}
