package org.getchunky.chunky.listeners;

import org.bukkit.event.server.*;
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
        ChunkyCommand chunkyCommand = Chunky.getModuleManager().getCommandByName(commands[0]);
        if (chunkyCommand == null) return;

        String currentName = commands[0];
        int i;
        for (i = 1; i < commands.length; i++) {
            currentName += "." + commands[i];
            ChunkyCommand currentCommand = chunkyCommand.getChild(currentName);
            if (currentCommand == null) break;
            chunkyCommand = currentCommand;
        }
    }

    @Override
    public void onMapInitialize(MapInitializeEvent event) {
        ChunkRenderer.addToMap(event.getMap());
        Logging.info("Map initialized.");
    }
}
