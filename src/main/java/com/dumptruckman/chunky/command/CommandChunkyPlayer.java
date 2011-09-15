package com.dumptruckman.chunky.command;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.object.ChunkyChunk;
import com.dumptruckman.chunky.object.ChunkyObject;
import com.dumptruckman.chunky.object.ChunkyPlayer;
import com.dumptruckman.chunky.permission.ChunkyPermissions;
import com.dumptruckman.chunky.persistance.DatabaseManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.HashSet;

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
        HashSet<ChunkyObject> chunks = chunkyPlayer.getOwnables().get(ChunkyChunk.class.getName().hashCode());

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
        Language.PLAYER_MENU_GLOBALPERMISSIONS.normal(chunkyPlayer);
        if(flags!=null)
            Language.PERMISSIONS_STATUS.normal(chunkyPlayer
                    ,flags.contains(ChunkyPermissions.Flags.BUILD)
                    ,flags.contains(ChunkyPermissions.Flags.DESTROY)
                    ,flags.contains(ChunkyPermissions.Flags.SWITCH)
                    ,flags.contains(ChunkyPermissions.Flags.ITEMUSE));
        else
           Language.PERMISSIONS_STATUS.normal(chunkyPlayer
                    ,"Not set"
                    ,"Not set"
                    ,"Not set"
                    ,"Not set");
    }

}
