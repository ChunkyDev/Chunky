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
import org.getchunky.chunkie.permission.PermissionRelationship;

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

    private void displayInfo(IChunkyChunk chunkyChunk, Player player) {
        Language.CHUNK_MENU_TITLE.normal(player, chunkyChunk.getCoord().toString());
        IChunkyObject owner = chunkyChunk.getOwner();

        if (owner != null)
            Language.CHUNK_MENU_OWNER.normal(player, owner.getName());
        else
            Language.CHUNK_MENU_OWNER.normal(player, Language.NO_ONE.getString());

        Language.DEFAULT_PERMISSIONS.normal(player, Language.THIS_CHUNK.getString());
        PermissionRelationship perms = ChunkyManager.getPermissions(chunkyChunk, chunkyChunk);
        if (perms != null && perms.getFlags() != null) {
            Language.sendMessage(player, perms.toLongString());
        }

        IChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);
        if (owner != null && !owner.equals(chunkyPlayer)) {
            perms = ChunkyManager.getPermissions(chunkyChunk, chunkyPlayer);
            if (perms != null && perms.getFlags() != null) {
                Language.YOUR_PERMISSIONS.normal(player, Language.THIS_CHUNK.getString());
                Language.sendMessage(player, perms.toLongString());
            }
        }
    }
}
