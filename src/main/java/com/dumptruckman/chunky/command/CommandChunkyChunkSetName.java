package com.dumptruckman.chunky.command;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.bukkit.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author dumptruckman, SwearWord
 */
public class CommandChunkyChunkSetName implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if (args.length == 0) {
            Language.CMD_CHUNKY_CHUNK_SET_NAME_HELP.bad(sender);
            return;
        }

        ChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer(sender.getName());
        if (cPlayer == null) return;
        ChunkyChunk cChunk = cPlayer.getCurrentChunk();
        ChunkyObject chunkOwner = cChunk.getOwner();

        if (chunkOwner == null && !Permissions.ADMIN_SET_CHUNK_NAME.hasPerm(cPlayer)) {
            Language.CHUNK_NOT_OWNED.bad(cPlayer);
            return;
        }
        if (!cPlayer.equals(chunkOwner) && !Permissions.ADMIN_SET_CHUNK_NAME.hasPerm(cPlayer)) {
            Language.CHUNK_OWNED.bad(cPlayer, cChunk.getOwner().getName());
            return;
        }

        String name = "";
        for (String arg : args) {
            if (!name.isEmpty()) name += " ";
            name += arg;
        }

        cChunk.setName(name);
        Language.CHUNK_NAME_CHANGED.good(cPlayer, name);
    }
}
