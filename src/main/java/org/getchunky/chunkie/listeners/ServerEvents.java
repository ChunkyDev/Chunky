package org.getchunky.chunkie.listeners;

import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListener;
import org.getchunky.chunkie.Chunky;
import org.getchunky.chunkie.command.map.ChunkRenderer;
import org.getchunky.chunkie.util.Logging;

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
