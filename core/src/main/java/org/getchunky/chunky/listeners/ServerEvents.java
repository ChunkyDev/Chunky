package org.getchunky.chunky.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.command.map.ChunkRenderer;
import org.getchunky.chunky.util.Logging;

/**
 * @author dumptruckman
 */
public class ServerEvents implements Listener {

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        String[] commands = event.getCommand().split("\\s");
        Chunky.getModuleManager().parseCommand(event.getSender(), commands);
    }

    @EventHandler
    public void onMapInitialize(MapInitializeEvent event) {
        ChunkRenderer.addToMap(event.getMap());
        Logging.info("Map initialized.");
    }
}
