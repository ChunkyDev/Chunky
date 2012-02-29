package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;
import org.getchunky.chunkie.object.IChunkyPlayer;

import java.util.Arrays;
import java.util.List;

/**
 * @author dumptruckman
 */
public class CommandChunkyPlayerSetMode implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        if (args.length == 0) {
            Language.CMD_CHUNKY_PLAYER_SET_MODE_HELP.bad(sender);
            return;
        }

        IChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer((Player) sender);

        List<String> modes = Arrays.asList(args);
        for (String mode : modes) {
            mode = mode.toLowerCase();
        }

        boolean cleared = false;
        if (modes.contains("claim")) {
            IChunkyPlayer.getClaimModePlayers().add(cPlayer);
        }
        if (modes.contains("clear")) {
            cleared = true;
            IChunkyPlayer.getClaimModePlayers().remove(cPlayer);
        }

        if (!cleared)
            Language.PLAYER_MODE_SET.good(cPlayer);
        else
            Language.PLAYER_MODE_CLEAR.good(cPlayer);
    }
}
