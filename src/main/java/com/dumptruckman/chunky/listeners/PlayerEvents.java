package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.object.ChunkyCoordinates;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;


public class PlayerEvents extends PlayerListener{

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        ChunkyCoordinates coords = new ChunkyCoordinates(event.getTo());

    }
}
