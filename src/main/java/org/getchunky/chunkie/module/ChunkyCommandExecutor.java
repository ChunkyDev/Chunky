package org.getchunky.chunkie.module;

import org.bukkit.command.CommandSender;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args);

}
