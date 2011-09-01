package com.dumptruckman.chunky.listeners;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.payment.Methods;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.server.ServerListener;

/**
 * @author dumptruckman
 */
public class ServerEvents extends ServerListener {

    private Methods methods = null;

    public ServerEvents() {
        methods = new Methods();
    }

    @Override
    public void onPluginDisable(PluginDisableEvent event) {
        // Check to see if the module thats being disabled is the one we are using
        if (methods != null && methods.hasMethod()) {
            Boolean check = methods.checkDisabled(event.getPlugin());

            if(check) {
                Chunky.setMethod(null);
                Logging.info("Payment method was disabled. No longer accepting payments.");
            }
        }
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
        // Check to see if we need a payment method
        if (!methods.hasMethod()) {
            if(methods.setMethod(event.getPlugin())) {
                Chunky.setMethod(methods.getMethod());
                Logging.info("Payment method found (" + Chunky.getMethod().getName() + " version: " + Chunky.getMethod().getVersion() + ")");
            }
        }
    }

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
}
