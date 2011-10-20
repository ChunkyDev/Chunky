package org.getchunky.chunky.listeners;

import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListener;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.command.map.ChunkRenderer;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.util.Logging;

/**
 * @author dumptruckman
 */
public class ServerEvents extends ServerListener {

    @Override
    public void onServerCommand(ServerCommandEvent event) {
        String[] commands = event.getCommand().split("\\s");
        Chunky.getModuleManager().parseCommand(event.getSender(), commands);
    }

    @Override
    public void onMapInitialize(MapInitializeEvent event) {
        ChunkRenderer.addToMap(event.getMap());
        Logging.info("Map initialized.");
    }
}
