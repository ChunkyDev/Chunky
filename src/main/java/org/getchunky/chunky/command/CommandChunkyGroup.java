package org.getchunky.chunky.command;

import org.bukkit.command.CommandSender;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;

/**
 * @author dumptruckman
 */
public class CommandChunkyGroup implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        Language.CMD_CHUNKY_GROUP_HELP.help(sender);
    }
}
