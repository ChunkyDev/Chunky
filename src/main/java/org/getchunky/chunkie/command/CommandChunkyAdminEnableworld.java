package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;
import org.getchunky.chunkie.object.IChunkyWorld;

/**
 * @author dumptruckman
 */
public class CommandChunkyAdminEnableworld implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        String worldName = null;
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                Language.IN_GAME_ONLY.bad(sender);
                return;
            }
            worldName = ((Player) sender).getWorld().getName();
        } else {
            worldName = Language.combineStringArray(args, " ");
        }
        IChunkyWorld world = ChunkyManager.getChunkyWorld(worldName);
        if (world == null) {
            Language.NO_SUCH_WORLD.bad(sender, worldName);
            return;
        }
        world.setEnabled(true).save();
        Language.ENABLED_WORLD.good(sender);
    }
}
