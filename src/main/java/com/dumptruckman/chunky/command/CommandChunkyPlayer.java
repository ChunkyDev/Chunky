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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author dumptruckman, SwearWord
 */
public class CommandChunkyPlayer implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }
        if(args.length == 0) {
            ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer((Player)sender);
            displayInfo(chunkyPlayer,sender);
            return;
        }
        if(args.length > 0) {
            ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(args[0]);
            if(chunkyPlayer==null) return;
            displayInfo(chunkyPlayer,sender);
            return;
        }

    }

    private void displayInfo(ChunkyPlayer chunkyPlayer, CommandSender sender) {
        Language.PLAYER_MENU_TITLE.normal(sender,chunkyPlayer.getName());
        HashSet<ChunkyObject> chunks = chunkyPlayer.getOwnables().get(ChunkyChunk.class.getName());

        if(chunks != null && chunks.size() > 0) {
            Language.PLAYER_MENU_OWNEDCHUNKS.normal(sender);
            String message = "";
            int i=0;
            for(ChunkyObject o : chunks) {
                ChunkyChunk chunk = (ChunkyChunk)o;
                message += "[" + chunk.getCoord().getX() + "," + chunk.getCoord().getZ() + "] ";
                i+=1;
                if(i==6) {
                    i=0;
                    message += "n";
                }
            }
            for(String s : message.split("n")) {
                Language.sendMessage(chunkyPlayer,s.trim());
            }
        }
        displayGlobalPermissions(chunkyPlayer);
    }

    private void displayGlobalPermissions(ChunkyPlayer chunkyPlayer) {
        EnumSet<ChunkyPermissions.Flags> flags = chunkyPlayer.getDefaultPerms();
        Language.DEFAULT_PERMISSIONS.normal(chunkyPlayer, Language.YOUR_PROPERTY.getString());
        if(flags!=null)
            Language.PERMISSIONS_STATUS.normal(chunkyPlayer
                    ,flags.contains(ChunkyPermissions.Flags.BUILD)
                    ,flags.contains(ChunkyPermissions.Flags.DESTROY)
                    ,flags.contains(ChunkyPermissions.Flags.SWITCH)
                    ,flags.contains(ChunkyPermissions.Flags.ITEMUSE));
        else {
            String notSet = Language.NO_PERMISSIONS_SET.getString();
            Language.PERMISSIONS_STATUS.normal(chunkyPlayer
                    , notSet
                    , notSet
                    , notSet
                    , notSet);
        }
        HashMap<String, ChunkyPermissions> playerPermissions = ChunkyManager.getAllPermissions(chunkyPlayer.getId());
        String players = "";
        if (!playerPermissions.isEmpty()) {
            for (Map.Entry<String, ChunkyPermissions> permPlayer : playerPermissions.entrySet()) {
                String id = permPlayer.getKey();
                if (!ChunkyManager.isType(id, ChunkyPlayer.class)) continue;
                ChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer(ChunkyManager.getNameFromId(id));
                if (cPlayer == null) continue;
                if (!players.isEmpty()) players += ", ";
                ChunkyPermissions perms = ChunkyManager.getPermissions(chunkyPlayer.getId(), cPlayer.getId());
                if (perms != null)
                    players += cPlayer.getName() + ": [" + perms.toSmallString() + "]";
            }
        }
    }

}
