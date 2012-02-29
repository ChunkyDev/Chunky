package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;
import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyObject;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.bukkit.Permissions;

/**
 * @author dumptruckman, SwearWord
 */
public class CommandChunkyChunkSetName implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if (args.length == 0) {
            Language.CMD_CHUNKY_CHUNK_SET_NAME_HELP.bad(sender);
            return;
        }

        IChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer(sender.getName());
        if (cPlayer == null) return;
        IChunkyChunk cChunk = cPlayer.getCurrentChunk();
        IChunkyObject chunkOwner = cChunk.getOwner();

        if (chunkOwner == null && !Permissions.ADMIN_SET_CHUNK_NAME.hasPerm(cPlayer)) {
            Language.CHUNK_NOT_OWNED.bad(cPlayer);
            return;
        }
        if (!cPlayer.isOwnerOf(chunkOwner) && !Permissions.ADMIN_SET_CHUNK_NAME.hasPerm(cPlayer)) {
            Language.CHUNK_OWNED.bad(cPlayer, cChunk.getOwner().getName());
            return;
        }

        String name = Language.combineStringArray(args, " ");
        if (name.equalsIgnoreCase("clear")) name = "";

        cChunk.setName(name);
        Language.CHUNK_NAME_CHANGED.good(cPlayer, name);
    }
}
