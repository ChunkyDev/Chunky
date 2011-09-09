package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.event.object.player.*;
import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import com.dumptruckman.chunky.permission.ChunkyPermissionType;
import com.dumptruckman.chunky.permission.bukkit.Permissions;

public class ChunkyPlayerEvents extends ChunkyPlayerListener {
    @Override
    public void onPlayerUnownedBreak(ChunkyPlayerDestroyEvent event) {
        try {
            if(!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if(event.getPermissionType().equals(ChunkyPermissionType.NONE)) event.setCancelled(true);
    }

    @Override
    public void onPlayerUnownedBuild(ChunkyPlayerBuildEvent event) {
        try {
            if(!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if(event.getPermissionType().equals(ChunkyPermissionType.NONE)) event.setCancelled(true);
    }

    @Override
    public void onPlayerItemUse(ChunkyPlayerItemUseEvent event) {
        try {
            if(!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if(event.getPermissionType().equals(ChunkyPermissionType.NONE)) event.setCancelled(true);
    }

    @Override
    public void onPlayerSwitch(ChunkyPlayerSwitchEvent event) {
        try {
            if(!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if(event.getPermissionType().equals(ChunkyPermissionType.NONE)) event.setCancelled(true);
    }
}
