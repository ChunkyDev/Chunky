package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.getchunky.chunkie.Chunky;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;
import org.getchunky.chunkie.util.Logging;

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
