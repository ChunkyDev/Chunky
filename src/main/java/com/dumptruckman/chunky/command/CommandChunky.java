package com.dumptruckman.chunky.command;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.util.Logging;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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
        Language.CMD_CHUNKY.normal(sender, Logging.getNameVersion(), modules);
    }
}
