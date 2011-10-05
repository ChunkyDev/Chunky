package org.getchunky.chunky.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.util.Logging;

/**
 * @author dumptruckman
 */
public class CommandChunky implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        String modules = "";
        for (JavaPlugin module : Chunky.getModuleManager().getRegisteredModules()) {
            if (!modules.isEmpty()) modules += ", ";
            modules += module.getDescription().getName() + " " + module.getDescription().getVersion();
        }
        Language.CMD_CHUNKY_DESC.normal(sender, Logging.getNameVersion(), modules);
    }
}
