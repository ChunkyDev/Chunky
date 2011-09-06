package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.event.object.player.*;
import com.dumptruckman.chunky.exceptions.ChunkyPlayerOfflineException;
import com.dumptruckman.chunky.permission.Permissions;

public class ChunkyPlayerEvents extends ChunkyPlayerListener {
    @Override
    public void onPlayerUnownedBreak(ChunkyPlayerUnownedBreakEvent event) {
        if(event.isCancelled()) return;
        try {
            if(!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onPlayerUnownedBuild(ChunkyPlayerUnownedBuildEvent event) {
        if(event.isCancelled()) return;
        try {
            if(!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onPlayerItemUse(ChunkyPlayerItemUseEvent event) {
        if(event.isCancelled()) return;
        try {
            if(!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if(event.getChunkyChunk().isOwnedBy(event.getChunkyPlayer())) return;
        event.setCancelled(true);
    }

    @Override
    public void onPlayerSwitch(ChunkyPlayerSwitchEvent event) {
        if(event.isCancelled()) return;
        try {
            if(!Permissions.PLAYER_BUILD_ANYWHERE.hasPerm(event.getChunkyPlayer().getPlayer())) return;
        } catch (ChunkyPlayerOfflineException e) {
            return;
        }
        if(event.getChunkyChunk().isOwnedBy(event.getChunkyPlayer())) return;
        event.setCancelled(true);
    }
}
