package org.getchunky.chunkie.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.locale.Language;
import org.getchunky.chunkie.module.ChunkyCommand;
import org.getchunky.chunkie.module.ChunkyCommandExecutor;
import org.getchunky.chunkie.module.ChunkyPermissions;
import org.getchunky.chunkie.object.IChunkyChunk;
import org.getchunky.chunkie.object.IChunkyGroup;
import org.getchunky.chunkie.object.IChunkyObject;
import org.getchunky.chunkie.object.IChunkyPlayer;
import org.getchunky.chunkie.permission.PermissionFlag;
import org.getchunky.chunkie.permission.PermissionRelationship;
import org.getchunky.chunkie.permission.bukkit.Permissions;
import org.getchunky.chunkie.util.Logging;

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

        IChunkyPlayer cPlayer = ChunkyManager.getChunkyPlayer((Player) sender);

        HashSet<IChunkyObject> targets = getTargets(cPlayer, args[0]);
        if (targets.isEmpty()) return;
        HashMap<PermissionFlag, Boolean> permissions = getPermissions(cPlayer, args[1]);
        if (permissions != null && permissions.isEmpty()) return;
        setPermissions(cPlayer, args[2], targets, permissions);
    }

    private HashSet<IChunkyObject> getTargets(IChunkyPlayer cPlayer, String targetString) {
        // The target(s) ChunkyObjects that permissions are being set for
        HashSet<IChunkyObject> targets = new HashSet<IChunkyObject>();

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
            IChunkyChunk cChunk = cPlayer.getCurrentChunk();

            // Determine if command sender is chunk owner or admin to continue
            IChunkyObject chunkOwner = cChunk.getOwner();
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
            targets = cPlayer.getOwnables().get(IChunkyChunk.class.getName());
            if (targets == null || targets.isEmpty()) {
                Language.CHUNK_NONE_OWNED.bad(cPlayer);
                return targets;
            }
        } else if (targetString.startsWith("c:")) {
            targetString = targetString.substring(2);
            HashSet<IChunkyObject> chunks = cPlayer.getOwnables().get(IChunkyChunk.class.getName());
            for (IChunkyObject chunk : chunks) {
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

    private HashMap<PermissionFlag, Boolean> getPermissions(IChunkyPlayer cPlayer, String permString) {
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

    private void setPermissions(IChunkyPlayer cPlayer, String permissibleString, HashSet<IChunkyObject> targets, HashMap<PermissionFlag, Boolean> permissions) {

        // sTargetForPermissible refers to the object the permissible is gaining/losing permissions for from their perspective
        sTargetForPermissible = "";
        // sPermObject refers to the target for the sender's perspective
        String sPermObject = "";
        // The permissions for the target/object relationship for displaying to sender and target
        PermissionRelationship perms = null;

        if (permissibleString.equalsIgnoreCase("global")) {
            // "everyone"
            sPermObject = Language.EVERYONE.getString();
            for (IChunkyObject target : targets) {
                target.setDefaultPerms(permissions);
                if (perms == null)
                    perms = ChunkyManager.getPermissions(target, target);
            }
        } else if (permissibleString.equalsIgnoreCase("all")) {
            // "all specific players"
            sPermObject = Language.ALL_SPECIFIC_PLAYERS.getString();

            for (IChunkyObject target : targets) {
                if (sTargetForPermissible.isEmpty()) {
                    // On the first loop through, get word reference for current target
                    if (target instanceof IChunkyChunk) {
                        sTargetForPermissible = Language.CHUNK_AT.getString(((IChunkyChunk) target).getCoord());
                    } else if (target instanceof IChunkyPlayer) {
                        sTargetForPermissible = Language.THEIR_PROPERTY.getString();
                    }
                }

                // Grab the permissions for the current target object and loop through the objects with permissions in order to set those perms
                Set<IChunkyObject> objectPerms = ChunkyManager.getAllPermissions(target).keySet();
                for (IChunkyObject permObject : objectPerms) {
                    ChunkyManager.setPermissions(target, permObject, permissions);

                    if (perms == null)
                        perms = ChunkyManager.getPermissions(target, permObject);

                    if (permObject != null && permObject instanceof IChunkyPlayer) {
                        if (targets.size() == 1) {
                            Language.PERMS_FOR_YOU.normal((IChunkyPlayer) permObject, cPlayer.getName(), perms.toLongString(), sTargetForPermissible);
                        } else {
                            Language.PERMS_FOR_YOU.normal((IChunkyPlayer) permObject, cPlayer.getName(), perms.toLongString(), Language.ALL_THEIR_CURRENT_PROPERTY.getString());
                        }
                    }
                }
            }
        } else {
            if (permissibleString.startsWith("g:")) {
                // groups
                String groupName = permissibleString.substring(2);
                HashSet<IChunkyObject> groups = cPlayer.getOwnables().get(IChunkyGroup.class.getName());
                IChunkyGroup group = null;
                for (IChunkyObject object : groups) {
                    if (object instanceof IChunkyGroup) {
                        if (object.getName().equalsIgnoreCase(groupName)) {
                            group = (IChunkyGroup) object;
                            break;
                        }
                    }
                }
                if (group == null) {
                    Language.NO_SUCH_GROUP.bad(cPlayer, groupName);
                    return;
                }
                sPermObject = "Group: " + group.getName();
                for (IChunkyObject target : targets) {
                    group.setPerms(target, permissions);
                    if (perms == null)
                        perms = ChunkyManager.getPermissions(target, group);
                }
            } else {
                // Specific player "name"

                // Grab ChunkyPlayer from args
                IChunkyPlayer permPlayer = ChunkyManager.getChunkyPlayer(permissibleString);
                if (permPlayer == null) {
                    Language.NO_SUCH_PLAYER.bad(cPlayer, permissibleString);
                    return;
                }
                sPermObject = permPlayer.getName();

                for (IChunkyObject target : targets) {
                    permPlayer.setPerms(target, permissions);
                    if (sTargetForPermissible.isEmpty()) {
                        perms = ChunkyManager.getPermissions(target, permPlayer);
                        if (targets.size() == 1) {
                            if (target instanceof IChunkyChunk) {
                                sTargetForPermissible = Language.CHUNK_AT.getString(((IChunkyChunk) target).getCoord());
                            } else if (target instanceof IChunkyPlayer) {
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