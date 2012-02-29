package org.getchunky.chunkie.listeners;

import org.getchunky.chunkie.event.object.player.ChunkyPlayerBuildEvent;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerDestroyEvent;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerItemUseEvent;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerListener;
import org.getchunky.chunkie.event.object.player.ChunkyPlayerSwitchEvent;
import org.getchunky.chunkie.exceptions.ChunkyPlayerOfflineException;
import org.getchunky.chunkie.permission.AccessLevel;
import org.getchunky.chunkie.permission.bukkit.Permissions;

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
