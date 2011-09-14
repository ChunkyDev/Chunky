package com.dumptruckman.chunky.command;

import java.util.*;

import com.dumptruckman.chunky.ChunkyManager;
import com.dumptruckman.chunky.locale.Language;
import com.dumptruckman.chunky.module.ChunkyCommand;
import com.dumptruckman.chunky.module.ChunkyCommandExecutor;
import com.dumptruckman.chunky.object.*;

import com.dumptruckman.chunky.permission.ChunkyPermissions;
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

        setPerms(ChunkyManager.getChunkyPlayer((Player) sender), args);
    }

    public void setPerms(ChunkyPlayer cPlayer, String[] args){

        String permissions = "";
        ChunkyObject target = null;

        if (args.length != 0) {
            String[] tokens = args[0].split(":", 2);
            // Not global, just flags
            if (tokens.length == 1){
                if (!cPlayer.getCurrentChunk().isDirectlyOwnedBy(cPlayer)){
                    Language.CHUNK_NOT_OWNED.bad(cPlayer);
                    return;
                }
                target = cPlayer.getCurrentChunk();
                permissions = tokens[0];
            }

            else if (tokens.length == 2){
                // Flags are global
                if (tokens[0].equals("*")){
                    HashMap<Integer, HashSet<ChunkyObject>> ownables = cPlayer.getOwnables();
                    if (!ownables.containsKey(ChunkyChunk.class.getName().hashCode())){
                        Language.CHUNK_NONE_OWNED.bad(cPlayer);
                        return;
                    }
                    target = cPlayer;
                }
                // Specific chunk name
                else {
                    Language.FEATURE_NYI.bad(cPlayer);
                    return;
                }
                permissions = tokens[1];
            } else {
                // WILL NEVER REACH THIS POINT
                return;
            }
        } else {
            target = cPlayer.getCurrentChunk();
        }

        // State is either add, subtract or set flags
        int state = 0;
        if (permissions.startsWith("-")) state = -1;
        if (permissions.startsWith("+")) state = 1;
        // Set of flags
        EnumSet<ChunkyPermissions.Flags> flags = EnumSet.noneOf(ChunkyPermissions.Flags.class);
        if (permissions.equalsIgnoreCase("clear")) {
            state = 0;
            flags = null;
        } else {
            for (char perm : permissions.toLowerCase().toCharArray()) {
                ChunkyPermissions.Flags flag = ChunkyPermissions.Flags.get(perm);
                if (flag == null) continue;
                flags.add(flag);
            }
        }

        String sTarget = "";
        String sPermObject = "";
        ChunkyPermissions perms = null;
        if (target instanceof ChunkyChunk) {
            sTarget = Language.THIS_CHUNK.getString();
        } else if (target instanceof ChunkyPlayer) {
            sTarget = Language.YOUR_PROPERTY.getString();
        }
        // No player or group defined
        if (args.length <= 1){
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
            sPermObject = Language.EVERYONE.getString();
            perms = ChunkyManager.getPermissions(target.hashCode(), target.hashCode());
        } else if (args.length == 2) {
            ChunkyPermissibleObject object = null;
            // Groups
            if (args[1].startsWith("g:")){
                Language.FEATURE_NYI.bad(cPlayer);
                return;
            }
            // Player
            else {
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

            sPermObject = object.getName();
            perms = ChunkyManager.getPermissions(target.hashCode(), object.hashCode());

            if (object instanceof ChunkyPlayer) {
                String sTarget2 = "";
                if (target instanceof ChunkyChunk) {
                    sTarget2 = Language.CHUNK_AT.getString(((ChunkyChunk)target).getCoord());
                } else if (target instanceof ChunkyPlayer) {
                    sTarget2 = Language.THEIR_PROPERTY.getString();
                }
                Language.PERMS_FOR_YOU.good((ChunkyPlayer)object, cPlayer.getName(), perms, sTarget2);
            }
        } else if (args.length > 2){
            Language.CMD_CHUNKY_PERMISSION_HELP.bad(cPlayer);
            return;
        }
        Language.PERMISSIONS.good(cPlayer, sTarget, perms, sPermObject);
    }
}