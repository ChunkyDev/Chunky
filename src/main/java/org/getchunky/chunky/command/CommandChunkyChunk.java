package org.getchunky.chunky.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.PermissionRelationship;

public class CommandChunkyChunk implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if (args.length == 0) {
            Player player = (Player) sender;
            displayInfo(ChunkyManager.getChunkyChunk(player.getLocation()), player);
            return;
        }

        if (args.length > 0) {
            Language.FEATURE_NYI.bad(sender);
            return;
        }
    }

    private void displayInfo(ChunkyChunk chunkyChunk, Player player) {
        Language.CHUNK_MENU_TITLE.normal(player, chunkyChunk.getCoord().toString());
        ChunkyObject owner = chunkyChunk.getOwner();

        if (owner != null)
            Language.CHUNK_MENU_OWNER.normal(player, owner.getName());
        else
            Language.CHUNK_MENU_OWNER.normal(player, Language.NO_ONE.getString());

        Language.DEFAULT_PERMISSIONS.normal(player, Language.THIS_CHUNK.getString());
        PermissionRelationship perms = ChunkyManager.getPermissions(chunkyChunk, chunkyChunk);
        if (perms != null && perms.getFlags() != null) {
            Language.sendMessage(player, perms.toLongString());
        }

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);
        if (owner != null && !owner.equals(chunkyPlayer)) {
            perms = ChunkyManager.getPermissions(chunkyChunk, chunkyPlayer);
            if (perms != null && perms.getFlags() != null) {
                Language.YOUR_PERMISSIONS.normal(player, Language.THIS_CHUNK.getString());
                Language.sendMessage(player, perms.toLongString());
            }
        }
    }
}
