package org.getchunky.chunky.command;

import java.util.*;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.object.*;

import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.permission.bukkit.Permissions;
import org.getchunky.chunky.persistance.DatabaseManager;
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
        if (args.length != 3) {
            Language.CMD_CHUNKY_PERMISSION_HELP.bad(cPlayer);
            return;
        }

        // The target(s) ChunkyObjects that permissions are being set for
        HashSet<ChunkyObject> targets = new HashSet<ChunkyObject>();
        // sTarget refers to the object that a permissible will receive permissions for
        String sTarget = "";

        if (args[0].equalsIgnoreCase("global")) {
            // Wants to set default perms for their stuff

            // What to refer to target as "your property"
            sTarget = Language.YOUR_PROPERTY.getString();
            // Default perms means it sets the perms on the ChunkyPlayer sending the command
            targets.add(cPlayer);
        } else if (args[0].equalsIgnoreCase("this")) {
            // Wants to set perms for current chunk

            // What to refer to the target as "this chunk"
            sTarget = Language.THIS_CHUNK.getString();
            // Retrieve the current chunk
            ChunkyChunk cChunk = cPlayer.getCurrentChunk();

            // Determine if command sender is chunk owner or admin to continue
            ChunkyObject chunkOwner = cChunk.getOwner();
            if (chunkOwner == null || !cPlayer.equals(chunkOwner)) {
                if (!Permissions.ADMIN_SETPERM.hasPerm(cPlayer)) {
                    if (chunkOwner != null) {
                        Language.CHUNK_OWNED.bad(cPlayer, chunkOwner.getName());
                    } else {
                        Language.CHUNK_NOT_OWNED.bad(cPlayer);
                    }
                    return;
                }
            }
            // Setting perms for the current chunk
            targets.add(cChunk);
        } else if (args[0].equalsIgnoreCase("all")) {
            // Wants to set perms for currently owned chunks

            // What to refer to the target as "your current property"
            sTarget = Language.YOUR_CURRENT_PROPERTY.getString();

            // Grabs all the players current property, errors out if null
            targets = cPlayer.getOwnables().get(ChunkyChunk.class.getName());
            if (targets == null || targets.isEmpty()) {
                Language.CHUNK_NONE_OWNED.bad(cPlayer);
                return;
            }
        } else {
            // Wants to set perms for named chunks
            // TODO
            Language.FEATURE_NYI.bad(cPlayer);
            return;
        }


        // Converts the flags into an EnumSet<ChunkyPermissions.Flags>
        EnumSet<ChunkyPermissions.Flags> flags = EnumSet.noneOf(ChunkyPermissions.Flags.class);
        if (args[1].equalsIgnoreCase("clear")) {
            // Flags of "clear" means it will null out the flag set.  This is necessary to indicate that permissions are "not set" for this relationship.
            flags = null;
        } else {
            for (char perm : args[1].toLowerCase().toCharArray()) {
                ChunkyPermissions.Flags flag = ChunkyPermissions.Flags.get(perm);
                if (flag == null) continue;
                flags.add(flag);
            }
        }

        // sTargetForPermissible refers to the object the permissible is gaining/losing permissions for from their perspective
        String sTargetForPermissible = "";
        // sPermObject refers to the target for the sender's perspective
        String sPermObject = "";
        // The permissions for the target/object relationship for displaying to sender and target
        ChunkyPermissions perms = null;
        
        if (args[2].equalsIgnoreCase("global")) {
            // "everyone"
            sPermObject = Language.EVERYONE.getString();
            for (ChunkyObject target : targets) {
                target.setDefaultPerms(flags);
                if (perms == null)
                    perms = ChunkyManager.getPermissions(target.getId(), target.getId());
            }
        } else if (args[2].equalsIgnoreCase("all")) {
            // "all specific players"
            sPermObject = Language.ALL_SPECIFIC_PLAYERS.getString();

            // Incremental counter for determining first for loop
            int i = 0;
            for (ChunkyObject target : targets) {
                if (i == 0) {
                    // On the first loop through, get word reference for current target
                    if (target instanceof ChunkyChunk) {
                        sTargetForPermissible = Language.CHUNK_AT.getString(((ChunkyChunk)target).getCoord());
                    } else if (target instanceof ChunkyPlayer) {
                        sTargetForPermissible = Language.THEIR_PROPERTY.getString();
                    }
                }

                // Grab the permissions for the current target object and loop through the objects with permissions in order to set those perms
                Set<String> objectPerms = ChunkyManager.getAllPermissions(target.getId()).keySet();
                for (String permObjectId : objectPerms) {
                    if (flags == null) {
                        ChunkyManager.getPermissions(target.getId(), permObjectId).clearFlags();
                        DatabaseManager.getDatabase().removePermissions(target.getId(), permObjectId);
                        return;
                    } else {
                        ChunkyManager.setPermissions(target.getId(), permObjectId, flags);
                    }

                    if (i == 0) {
                        perms = ChunkyManager.getPermissions(target.getId(), permObjectId);
                        ChunkyObject tempPlayer = ChunkyManager.getObject(permObjectId);
                        if (tempPlayer != null && tempPlayer instanceof ChunkyPlayer) {
                            if (targets.size() == 1) {
                                Language.PERMS_FOR_YOU.normal((ChunkyPlayer) tempPlayer, cPlayer.getName(), perms, sTargetForPermissible);
                            } else {
                                Language.PERMS_FOR_YOU.normal((ChunkyPlayer) tempPlayer, cPlayer.getName(), perms, Language.ALL_THEIR_CURRENT_PROPERTY.getString());
                            }
                        }
                    }
                }
                i++;
            }
        } else {
            // Specific player "name"

            // Grab ChunkyPlayer from args
            ChunkyPlayer permPlayer = ChunkyManager.getChunkyPlayer(args[2]);
            if (permPlayer == null) {
                Language.NO_SUCH_PLAYER.bad(cPlayer, args[2]);
                return;
            }
            sPermObject = permPlayer.getName();

            for (ChunkyObject target : targets) {
                permPlayer.setPerms(target, flags);
                if (sTargetForPermissible.isEmpty()) {
                    perms = ChunkyManager.getPermissions(target.getId(), permPlayer.getId());
                    if (targets.size() == 1) {
                        if (target instanceof ChunkyChunk) {
                            sTargetForPermissible = Language.CHUNK_AT.getString(((ChunkyChunk)target).getCoord());
                        } else if (target instanceof ChunkyPlayer) {
                            sTargetForPermissible = Language.THEIR_PROPERTY.getString();
                        }
                    } else {
                        sTargetForPermissible = Language.ALL_THEIR_CURRENT_PROPERTY.toString();
                    }
                }
            }

            Language.PERMS_FOR_YOU.normal(permPlayer, cPlayer.getName(), perms, sTargetForPermissible);
        }
        
        if (perms != null)
            Language.PERMISSIONS.good(cPlayer, sTarget, perms, sPermObject);
    }
}