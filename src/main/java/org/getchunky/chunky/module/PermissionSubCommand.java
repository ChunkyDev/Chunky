package org.getchunky.chunky.module;

import org.getchunky.chunky.locale.Language;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyObject;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.PermissionFlag;
import org.getchunky.chunky.permission.bukkit.Permissions;

import java.util.*;

/**
 * @author dumptruckman
 */
public abstract class PermissionSubCommand {

    private Collection<ChunkyObject> targets = new ArrayList<ChunkyObject>();
    private Collection<ChunkyObject> permissibles = new ArrayList<ChunkyObject>();
    private HashMap<PermissionFlag, Boolean> permissions = new HashMap<PermissionFlag, Boolean>();

    private boolean canHaveSpecifiedPermissible = false;

    private List<String> errorStrings = null;

    private String sTarget = "";

    private ChunkyPlayer chunkyPlayer;

    protected PermissionSubCommand setSendingPlayer(ChunkyPlayer chunkyPlayer) {
        this.chunkyPlayer = chunkyPlayer;
        return this;
    }

    protected ChunkyPlayer getSendingPlayer() {
        return this.chunkyPlayer;
    }

    protected PermissionSubCommand setTargets(ChunkyObject...targets) {
        return this.setTargets(Arrays.asList(targets));
    }

    protected PermissionSubCommand setTargets(Collection<ChunkyObject> targets) {
        this.targets = targets;
        return this;
    }

    protected void setErrorStrings(List<String> errors) {
        this.errorStrings = errors;
    }

    protected List<String> getErrorStrings() {
        return this.errorStrings;
    }

    protected PermissionSubCommand setTargetsFromString(String targetString) {
        if (targetString.equalsIgnoreCase("global")) {
            // Wants to set default perms for their stuff

            // What to refer to target as "your property"
            sTarget = Language.YOUR_PROPERTY.getString();
            // Default perms means it sets the perms on the ChunkyPlayer sending the command
            targets.add(getSendingPlayer());
        } else if (targetString.equalsIgnoreCase("this")) {
            // Wants to set perms for current chunk

            // What to refer to the target as "this chunk"
            sTarget = Language.THIS_CHUNK.getString();
            // Retrieve the current chunk
            ChunkyChunk cChunk = getSendingPlayer().getCurrentChunk();

            // Determine if command sender is chunk owner or admin to continue
            ChunkyObject chunkOwner = cChunk.getOwner();
            if (chunkOwner == null || !cChunk.isOwnedBy(getSendingPlayer()) || !getSendingPlayer().hasPerm(cChunk, ChunkyPermissions.OWNER)) {
                if (!Permissions.ADMIN_SETPERM.hasPerm(getSendingPlayer())) {
                    if (chunkOwner != null) {
                        //error = PermissionError.CHUNK_OWNED;
                        setErrorStrings(Language.CHUNK_OWNED.getStrings(chunkOwner.getName()));
                    } else {
                        setErrorStrings(Language.CHUNK_NOT_OWNED.getStrings());
                    }
                    return this;
                }
            }
            // Setting perms for the current chunk
            targets.add(cChunk);
        } else if (targetString.equalsIgnoreCase("all")) {
            // Wants to set perms for currently owned chunks

            // What to refer to the target as "your current property"
            sTarget = Language.YOUR_CURRENT_PROPERTY.getString();

            // Grabs all the players current property, errors out if null
            targets = getSendingPlayer().getOwnables().get(ChunkyChunk.class.getName());
            if (targets == null || targets.isEmpty()) {
                setErrorStrings(Language.CHUNK_NONE_OWNED.getStrings());
                return this;
            }
        } else {
            //targetString = targetString.substring(2);
            HashSet<ChunkyObject> chunks = getSendingPlayer().getOwnables().get(ChunkyChunk.class.getName());
            for (ChunkyObject chunk : chunks) {
                if (chunk.getName().equalsIgnoreCase(targetString))
                    targets.add(chunk);
            }
            if (targets.isEmpty()) {
                setErrorStrings(Language.NO_SUCH_CHUNKS.getStrings(targetString));
                return this;
            }
            // What to refer to the target as "your current property"
            sTarget = Language.CHUNKS_NAMED.getString(targetString);
        }
        return this;
    }

    protected PermissionSubCommand setPermissibles(ChunkyObject...permissibles) {
        this.setPermissibles(Arrays.asList(permissibles));
        return this;
    }

    protected PermissionSubCommand setPermissibles(Collection<ChunkyObject> permissibles) {
        this.permissibles = permissibles;
        return this;
    }

    protected PermissionSubCommand setPermissionFlags(HashMap<PermissionFlag, Boolean> flags) {
        this.permissions = flags;
        return this;
    }

    protected PermissionSubCommand setPermissionFlagsFromString(String permString) {
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
                setErrorStrings(Language.CMD_CHUNKY_PERMISSION_ADD_SUBTRACT.getStrings());
                getErrorStrings().addAll(Language.CMD_CHUNKY_PERMISSION_HELP_REMINDER.getStrings());
                return this;
            }
            String[] flagsString = permString.toLowerCase().substring(1).split(",");
            for (String flagString : flagsString) {
                PermissionFlag flag = ChunkyPermissions.getFlagByTag(flagString);
                if (flag == null) continue;
                permissions.put(flag, add);
            }
            if (permissions.isEmpty()) {
                setErrorStrings(Language.CMD_CHUNKY_PERMISSION_SPECIFY_FLAGS.getStrings());
                getErrorStrings().addAll(Language.CMD_CHUNKY_PERMISSION_HELP_REMINDER.getStrings());
                return this;
            }
        }
        return this;
    }

    public PermissionSubCommand setCanHaveSpecifiedPermissible(boolean canHaveSpecifiedPermissible) {
        this.canHaveSpecifiedPermissible = canHaveSpecifiedPermissible;
        return this;
    }

    protected boolean canHaveSpecifiedPermissible() {
        return canHaveSpecifiedPermissible;
    }

    private String permissibleString = null;

    protected String getPermissibleString() {
        return permissibleString;
    }

    protected void setPermissibleString(String permissibleString) {
        this.permissibleString = permissibleString;
    }

    protected abstract Collection<ChunkyObject> getPermissibles();
}
