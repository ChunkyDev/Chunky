package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;

/**
 * @author dumptruckman
 */
public class CommandChunkyGroup implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        Language.CMD_CHUNKY_GROUP_HELP.help(sender);
    }
}
