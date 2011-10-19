package org.getchunky.chunky.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.module.ChunkyCommand;
import org.getchunky.chunky.module.ChunkyCommandExecutor;
import org.getchunky.chunky.module.ChunkyPermissions;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyGroup;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.PermissionFlag;
import org.getchunky.chunky.permission.PermissionRelationship;
import org.getchunky.chunky.permission.bukkit.Permissions;
import org.getchunky.chunky.util.Logging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dumptruckman, SwearWord
 */
public class CommandChunkyPermission implements ChunkyCommandExecutor {

    String sTarget;
    String sTargetForPermissible;

    public void onCommand(CommandSender sender, ChunkyCommand command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Language.IN_GAME_ONLY.bad(sender);
            return;
        }

        if (args.length < 3) {
            Language.CMD_CHUNKY_PERMISSION_HELP.bad(sender);
            return;
        }

        // sTarget refers to the object that a permissible will receive permissions for
        sTarget = "";

        ChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer((Player) sender);

        HashSet<ChunkyObject> targets = getTargets(cPlayer, args[0]);
        if (targets.isEmpty()) return;
        HashMap<PermissionFlag, Boolean> permissions = getPermissions(cPlayer, args[1]);
        if (permissions != null && permissions.isEmpty()) return;
        setPermissions(cPlayer, args[2], targets, permissions);
    }

    private HashSet<ChunkyObject> getTargets(ChunkyPlayer cPlayer, String targetString) {
        // The target(s) ChunkyObjects that permissions are being set for
        HashSet<ChunkyObject> targets = new HashSet<ChunkyObject>();

        if (targetString.equalsIgnoreCase("global")) {
            // Wants to set default perms for their stuff

            // What to refer to target as "your property"
            sTarget = Language.YOUR_PROPERTY.getString();
            // Default perms means it sets the perms on the ChunkyPlayer sending the command
            targets.add(cPlayer);
        } else if (targetString.equalsIgnoreCase("this")) {
            // Wants to set perms for current chunk

            // What to refer to the target as "this chunk"
            sTarget = Language.THIS_CHUNK.getString();
            // Retrieve the current chunk
            ChunkyChunk cChunk = cPlayer.getCurrentChunk();

            // Determine if command sender is chunk owner or admin to continue
            ChunkyObject chunkOwner = cChunk.getOwner();
            if (chunkOwner == null || !cChunk.isOwnedBy(cPlayer) || !cPlayer.hasPerm(cChunk, ChunkyPermissions.OWNER)) {
                if (!Permissions.ADMIN_SETPERM.hasPerm(cPlayer)) {
                    if (chunkOwner != null) {
                        Language.CHUNK_OWNED.bad(cPlayer, chunkOwner.getName());
                    } else {
                        Language.CHUNK_NOT_OWNED.bad(cPlayer);
                    }
                    return targets;
                }
            }
            // Setting perms for the current chunk
            targets.add(cChunk);
        } else if (targetString.equalsIgnoreCase("all")) {
            // Wants to set perms for currently owned chunks

            // What to refer to the target as "your current property"
            sTarget = Language.YOUR_CURRENT_PROPERTY.getString();

            // Grabs all the players current property, errors out if null
            targets = cPlayer.getOwnables().get(ChunkyChunk.class.getName());
            if (targets == null || targets.isEmpty()) {
                Language.CHUNK_NONE_OWNED.bad(cPlayer);
                return targets;
            }
        } else if (targetString.startsWith("c:")) {
            targetString = targetString.substring(2);
            HashSet<ChunkyObject> chunks = cPlayer.getOwnables().get(ChunkyChunk.class.getName());
            for (ChunkyObject chunk : chunks) {
                if (chunk.getName().equalsIgnoreCase(targetString))
                    targets.add(chunk);
            }
            if (targets.isEmpty()) {
                Language.NO_SUCH_CHUNKS.bad(cPlayer, targetString);
                return targets;
            }
            // What to refer to the target as "your current property"
            sTarget = Language.CHUNKS_NAMED.getString(targetString);
        } else {
            Language.CMD_CHUNKY_PERMISSION_HELP.bad(cPlayer);
        }

        return targets;
    }

    private HashMap<PermissionFlag, Boolean> getPermissions(ChunkyPlayer cPlayer, String permString) {
        // Converts the flags into an EnumSet<PermissionRelationship.Flags>
        HashMap<PermissionFlag, Boolean> permissions = new HashMap<PermissionFlag, Boolean>();

        if (permString.equalsIgnoreCase("clear")) {
            // Flags of "clear" means it will null out the flag set.  This is necessary to indicate that permissions are "not set" for this relationship.
            permissions = null;
        } else {
            boolean add;
            if (permString.startsWith("+")) {
                add = true;
            } else if (permString.startsWith("-")) {
                add = false;
            } else {
                Language.CMD_CHUNKY_PERMISSION_ADD_SUBTRACT.bad(cPlayer);
                Language.CMD_CHUNKY_PERMISSION_HELP_REMINDER.help(cPlayer);
                return permissions;
            }
            String[] flagsString = permString.toLowerCase().substring(1).split(",");
            for (String flagString : flagsString) {
                PermissionFlag flag = ChunkyPermissions.getFlagByTag(flagString);
                if (flag == null) continue;
                permissions.put(flag, add);
            }
            if (permissions.isEmpty()) {
                Language.CMD_CHUNKY_PERMISSION_SPECIFY_FLAGS.bad(cPlayer);
                Language.CMD_CHUNKY_PERMISSION_HELP_REMINDER.help(cPlayer);
                return permissions;
            }
        }

        return permissions;
    }

    private void setPermissions(ChunkyPlayer cPlayer, String permissibleString, HashSet<ChunkyObject> targets, HashMap<PermissionFlag, Boolean> permissions) {

        // sTargetForPermissible refers to the object the permissible is gaining/losing permissions for from their perspective
        sTargetForPermissible = "";
        // sPermObject refers to the target for the sender's perspective
        String sPermObject = "";
        // The permissions for the target/object relationship for displaying to sender and target
        PermissionRelationship perms = null;

        if (permissibleString.equalsIgnoreCase("global")) {
            // "everyone"
            sPermObject = Language.EVERYONE.getString();
            for (ChunkyObject target : targets) {
                target.setDefaultPerms(permissions);
                if (perms == null)
                    perms = ChunkyManager.getPermissions(target, target);
            }
        } else if (permissibleString.equalsIgnoreCase("all")) {
            // "all specific players"
            sPermObject = Language.ALL_SPECIFIC_PLAYERS.getString();

            for (ChunkyObject target : targets) {
                if (sTargetForPermissible.isEmpty()) {
                    // On the first loop through, get word reference for current target
                    if (target instanceof ChunkyChunk) {
                        sTargetForPermissible = Language.CHUNK_AT.getString(((ChunkyChunk) target).getCoord());
                    } else if (target instanceof ChunkyPlayer) {
                        sTargetForPermissible = Language.THEIR_PROPERTY.getString();
                    }
                }

                // Grab the permissions for the current target object and loop through the objects with permissions in order to set those perms
                Set<ChunkyObject> objectPerms = ChunkyManager.getAllPermissions(target).keySet();
                for (ChunkyObject permObject : objectPerms) {
                    ChunkyManager.setPermissions(target, permObject, permissions);

                    if (perms == null)
                        perms = ChunkyManager.getPermissions(target, permObject);

                    if (permObject != null && permObject instanceof ChunkyPlayer) {
                        if (targets.size() == 1) {
                            Language.PERMS_FOR_YOU.normal((ChunkyPlayer) permObject, cPlayer.getName(), perms.toLongString(), sTargetForPermissible);
                        } else {
                            Language.PERMS_FOR_YOU.normal((ChunkyPlayer) permObject, cPlayer.getName(), perms.toLongString(), Language.ALL_THEIR_CURRENT_PROPERTY.getString());
                        }
                    }
                }
            }
        } else {
            if (permissibleString.startsWith("g:")) {
                // groups
                String groupName = permissibleString.substring(2);
                HashSet<ChunkyObject> groups = cPlayer.getOwnables().get(ChunkyGroup.class.getName());
                ChunkyGroup group = null;
                for (ChunkyObject object : groups) {
                    if (object instanceof ChunkyGroup) {
                        if (object.getName().equalsIgnoreCase(groupName)) {
                            group = (ChunkyGroup) object;
                            break;
                        }
                    }
                }
                if (group == null) {
                    Language.NO_SUCH_GROUP.bad(cPlayer, groupName);
                    return;
                }
                sPermObject = "Group: " + group.getName();
                for (ChunkyObject target : targets) {
                    group.setPerms(target, permissions);
                    if (perms == null)
                        perms = ChunkyManager.getPermissions(target, group);
                }
            } else {
                // Specific player "name"

                // Grab ChunkyPlayer from args
                ChunkyPlayer permPlayer = ChunkyManager.getChunkyPlayer(permissibleString);
                if (permPlayer == null) {
                    Language.NO_SUCH_PLAYER.bad(cPlayer, permissibleString);
                    return;
                }
                sPermObject = permPlayer.getName();

                for (ChunkyObject target : targets) {
                    permPlayer.setPerms(target, permissions);
                    if (sTargetForPermissible.isEmpty()) {
                        perms = ChunkyManager.getPermissions(target, permPlayer);
                        if (targets.size() == 1) {
                            if (target instanceof ChunkyChunk) {
                                sTargetForPermissible = Language.CHUNK_AT.getString(((ChunkyChunk) target).getCoord());
                            } else if (target instanceof ChunkyPlayer) {
                                sTargetForPermissible = Language.THEIR_PROPERTY.getString();
                            }
                        } else {
                            sTargetForPermissible = Language.ALL_THEIR_CURRENT_PROPERTY.toString();
                        }
                    }
                }

                Language.PERMS_FOR_YOU.normal(permPlayer, cPlayer.getName(), perms.toLongString(), sTargetForPermissible);
            }
        }

        if (perms == null) {
            Logging.debug("Perm Relationship null for " + sTarget + " by " + cPlayer.getName() + " for " + sPermObject);
        }
        if (perms != null)
            Language.PERMISSIONS.good(cPlayer, sTarget, perms.toLongString(), sPermObject);
    }
}