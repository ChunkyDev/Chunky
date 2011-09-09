package com.dumptruckman.chunky.command;

import java.util.*;

import com.dumptruckman.chunky.Chunky;
import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.exceptions.ChunkyException;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.object.*;

import com.dumptruckman.chunky.persistance.DatabaseManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author dumptruckman, SwearWord
 */
public class CommandChunkyPermission implements ChunkyCommandExecutor {

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)){
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if (args.length == 0){
            // TODO: Maybe this should show a readout of permissions given or something.
            Language.CMD_CHUNKY_PERMISSION_HELP.help(sender);
            return;
        }
        
        setPerms(ChunkyManager.getChunkyPlayer((Player) sender), args);
    }
        
    public void setPerms(ChunkyPlayer cPlayer, String[] args){

        String permissions = "";
        String[] tokens = args[0].split(":", 2);
        ChunkyObject target = null;
        
        if (tokens.length == 1){
            if (!cPlayer.getCurrentChunk().isDirectlyOwnedBy(cPlayer)){
                Language.CHUNK_NOT_OWNED.bad(cPlayer);
                return;
            }
            target = cPlayer.getCurrentChunk();
            permissions = tokens[0];
        } else if (tokens.length == 2){
            if (tokens[0].equals("*")){
                HashMap<Integer, HashSet<ChunkyObject>> ownables = cPlayer.getOwnables();
                if (!ownables.containsKey(ChunkyChunk.class.getName().hashCode())){
                    Language.CHUNK_NONE_OWNED.bad(cPlayer);
                    return;
                }
                target = cPlayer;
            } else {
                Language.FEATURE_NYI.bad(cPlayer);
                return;
            }
            permissions = tokens[1];
        } else {
            // WILL NEVER REACH THIS POINT
            return;
        }
        
        int state = 0;
        if (permissions.startsWith("-")) state = -1;
        if (permissions.startsWith("+")) state = 1;
        EnumSet<ChunkyPermissions.Flags> flags = EnumSet.noneOf(ChunkyPermissions.Flags.class);
        for (char perm : permissions.toCharArray()) {
            ChunkyPermissions.Flags flag = ChunkyPermissions.Flags.get(perm);
            if (flag == null) continue;
            flags.add(flag);
        }

        // No player or group defined
        if (args.length == 1){
            switch (state) {
                case -1:
                    for (ChunkyPermissions.Flags flag : flags) {
                        target.setDefaultPerm(flag, false);
                    }
                    break;

                case 1:
                    for (ChunkyPermissions.Flags flag : flags) {
                        target.setDefaultPerm(flag, true);
                    }
                    break;

                case 0:
                    target.setDefaultPerms(flags);
                    break;
            }
        } else if (args.length == 2) {
            ChunkyPermissibleObject object = null;
            if (args[1].startsWith("g:")){
                Language.FEATURE_NYI.bad(cPlayer);
                return;
            } else {
                object = ChunkyManager.getChunkyPlayer(args[1]);
            }

            switch (state) {
                case -1:
                    for (ChunkyPermissions.Flags flag : flags) {
                        object.setPerm(target, flag, false);
                    }
                    break;

                case 1:
                    for (ChunkyPermissions.Flags flag : flags) {
                        object.setPerm(target, flag, true);
                    }
                    break;

                case 0:
                    object.setPerms(target, flags);
                    break;
            }
        } else if (args.length > 2){
            Language.CMD_CHUNKY_PERMISSION_HELP.bad(cPlayer);
            return;
        }
    }
}