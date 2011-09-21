package com.dumptruckman.chunky.command;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class CommandChunkyChunk implements ChunkyCommandExecutor{

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if(args.length == 0) {
            Player player = (Player)sender;
            displayInfo(ChunkyManager.getChunk(player.getLocation()), player);
            return;
        }

        if(args.length > 0) {
            Language.FEATURE_NYI.bad(sender);
            return;
        }
    }

    private void displayInfo(ChunkyChunk chunkyChunk, Player player) {
        Language.CHUNK_MENU_TITLE.normal(player, chunkyChunk.getCoord().toString());
        ChunkyObject owner = chunkyChunk.getOwner();
        Language.CHUNK_MENU_OWNER.normal(player, owner.getName());

        Language.DEFAULT_PERMISSIONS.normal(player, Language.THIS_CHUNK.getString());
        ChunkyPermissions perms = ChunkyManager.getPermissions(chunkyChunk.getId(), chunkyChunk.getId());
        Language.PERMISSIONS_STATUS.normal(player
                    ,perms.contains(ChunkyPermissions.Flags.BUILD)
                    ,perms.contains(ChunkyPermissions.Flags.DESTROY)
                    ,perms.contains(ChunkyPermissions.Flags.SWITCH)
                    ,perms.contains(ChunkyPermissions.Flags.ITEMUSE));

        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);
        if (!chunkyChunk.getOwner().equals(chunkyPlayer)) {
            perms = ChunkyManager.getPermissions(chunkyChunk.getId(), chunkyPlayer.getId());
            if (perms != null && perms.getFlags() != null) {
                Language.YOUR_PERMISSIONS.normal(player, Language.THIS_CHUNK.getString());
                Language.PERMISSIONS_STATUS.normal(player, perms.contains(ChunkyPermissions.Flags.BUILD)
                            ,perms.contains(ChunkyPermissions.Flags.DESTROY)
                            ,perms.contains(ChunkyPermissions.Flags.SWITCH)
                            ,perms.contains(ChunkyPermissions.Flags.ITEMUSE));
            }
        }
    }
}
