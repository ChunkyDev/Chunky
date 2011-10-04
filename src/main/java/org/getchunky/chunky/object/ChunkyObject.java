package org.getchunky.chunky.object;

import org.getchunky.chunky.Chunky;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.event.object.ChunkyObjectNameEvent;
import org.getchunky.chunky.exceptions.ChunkyObjectNotInitializedException;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.persistance.DatabaseManager;
import org.getchunky.chunky.util.Logging;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author dumptruckman, SwearWord
 */
public abstract class ChunkyObject extends JSONObject {

    /**
     * Returns the child <code>TreeNode</code> at index
     * <code>childIndex</code>.
     */
    protected ChunkyObject owner;
    protected HashMap<String, HashSet<ChunkyObject>> ownables = new HashMap<String, HashSet<ChunkyObject>>();

    private String id;
    private final String className;

    public ChunkyObject() {
        super();
        try {
            this.put("name","");
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        className = this.getClass().getName();
    }

    public final ChunkyObject save() throws ChunkyObjectNotInitializedException {
        if(id == null) throw new ChunkyObjectNotInitializedException("Object persistence failed: Null ID not allowed!");
        DatabaseManager.getDatabase().updateObject(this);
        return this;
    }

    //TODO Should this be moved to super class?
    public final ChunkyObject load(String json) throws JSONException {
        JSONTokener x = new JSONTokener(json);
        char c;
        String key;

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        for (;;) {
            c = x.nextClean();
            switch (c) {
            case 0:
                throw x.syntaxError("A JSONObject text must end with '}'");
            case '}':
                return this;
            default:
                x.back();
                key = x.nextValue().toString();
            }

            // The key is followed by ':'. We will also tolerate '=' or '=>'.

            c = x.nextClean();
            if (c == '=') {
                if (x.next() != '>') {
                    x.back();
                }
            } else if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            put(key, x.nextValue());

            // Pairs are separated by ','. We will also tolerate ';'.

            switch (x.nextClean()) {
            case ';':
            case ',':
                if (x.nextClean() == '}') {
                    return this;
                }
                x.back();
                break;
            case '}':
                return this;
            default:
                throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }

    public final String getName() {
        try {
            return getString("name");
        } catch (JSONException e) {
            Logging.severe(e.getMessage());
            return null;
        }
    }

    public final String getId() {
        return id;
    }

    public final ChunkyObject setId(String id) {
        this.id = id;
        ChunkyManager.registerObject(this);
        return this;
    }

    public final String getType() {
        return className;
    }

    public final String getFullId() {
        return className + ":" + id;
    }

    public final ChunkyObject setName(String name) {
        ChunkyObjectNameEvent event = new ChunkyObjectNameEvent(this, name);
        Chunky.getModuleManager().callEvent(event);
        if (event.isCancelled()) return this;
        try {
            this.put("name", event.getNewName());
        } catch (JSONException e) {
            //Logging.warning(e.getMessage());
        }
        try {
            save();
        } catch (ChunkyObjectNotInitializedException e) {
            Logging.severe(e.getMessage());
        }
        return this;
    }

    public final int hashCode() {
        return getFullId().hashCode();
    }

    public final boolean equals(Object obj) {
        return obj instanceof ChunkyObject && ((ChunkyObject)obj).getFullId().equals(this.getFullId());
    }

    public final boolean isOwned() {
        return !(owner == null);
    }

    /**
     * Causes o to be owned by this object.
     * @param o object to become owned.
     * @return true if this object did not already own o.
     */
    private boolean addOwnable(ChunkyObject o) {
        if (o == null)
            throw new IllegalArgumentException();

        // If owner is a owned by the child, remove owner from tree first.
        // ex. town.setOwner(Peasant) Peasant is removed from parent (Town).

        if(this.isOwnedBy(o) && this.owner != null) {
            this.getOwner().removeOwnableAndTakeChildren(this);
        }
        if (ownables.containsKey(o.getType())) {
            Boolean exists = ownables.get(o.getType()).add(o);
            if(exists) DatabaseManager.getDatabase().addOwnership(this,o);
            return exists;
        } else {
            HashSet<ChunkyObject> ownables = new HashSet<ChunkyObject>();
            ownables.add(o);
            this.ownables.put(o.getType(), ownables);
            DatabaseManager.getDatabase().addOwnership(this,o);
            return true;
        }
    }

    /**
     * Removes an ownable o from this object.
     * @param o the ownable to remove
     * @return true if the set contained the specified element
     */
    private boolean removeOwnable(ChunkyObject o) {
        Boolean removed = ownables.containsKey(o.getType()) && ownables.get(o.getType()).remove(o);
        if(removed) {
            o.owner = null;
            DatabaseManager.getDatabase().removeOwnership(this,o);
        }
        return removed;
    }

    private void removeOwnableAndTakeChildren(ChunkyObject o) {
        if(removeOwnable(o)) takeChildren(o);
    }

    public final void takeChildren(ChunkyObject o) {
        // If a child is removed, parent reposesses all their objects.
        // ex. If a child of a town is removed then their plots are owned by town.

        HashMap<String, HashSet<ChunkyObject>> reposess = o.getOwnables();
        o.ownables = new HashMap<String, HashSet<ChunkyObject>>();
        for(String key : reposess.keySet()) {
            for(ChunkyObject co : reposess.get(key)) {
                this.addOwnable(co);
            }
        }
    }

    /**
     * Checks if o is owned by this object.
     * @param o object to hasPerm ownership for
     * @return true if this object owns o
     */
    public final boolean isOwnerOf(ChunkyObject o) {
        return o.isOwnedBy(this);
    }

    /**
     * @param owner Check if this object is an ancestor.
     * @return
     */
    public final boolean isOwnedBy(ChunkyObject owner) {
        if (owner == null)
            return false;
        ChunkyObject current = this;
        while (current != null && current != owner)
            current = current.getOwner();
        return current == owner;
    }

    public final boolean isDirectlyOwnedBy(ChunkyObject owner) {
        return this.owner == owner;
    }

    /**
     * Returns the owner <code>TreeNode</code> of the receiver.
     */
    public final ChunkyObject getOwner() {
        return owner;
    }

    /**
     * @param object the object that will become the owner.
     * @param keepChildren false transfers the object's children to current owner.
     */
    public final void setOwner(ChunkyObject object, Boolean keepChildren, boolean clearPermissions) {
        ChunkyObject oldowner = this.owner;
        if (owner != null)
            if(keepChildren) owner.removeOwnable(this);
            else owner.removeOwnableAndTakeChildren(this);
        if (object != null) {
            if (object.addOwnable(this)) owner = object;
        } else {
            owner = null;
        }
        if(oldowner != null) {

        }
        if(clearPermissions) {
            ChunkyManager.getAllPermissions(getFullId()).clear();
            DatabaseManager.getDatabase().removeAllPermissions(this.getFullId());}
    }

    public final Boolean hasDefaultPerm(ChunkyPermissions.Flags type) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(getFullId(), getFullId());
        Logging.debug("default perms: " + perms + " contains " + type + "?");
        return perms.contains(type);
    }

    public final EnumSet<ChunkyPermissions.Flags> getDefaultPerms() {
        return ChunkyManager.getPermissions(getFullId(), getFullId()).getFlags();
    }

    public final void setDefaultPerm(ChunkyPermissions.Flags type, boolean status) {
        setDefaultPerm(type, status, true);
    }

    public final void setDefaultPerms(EnumSet<ChunkyPermissions.Flags> flags) {
        if (flags == null) {
            ChunkyManager.getPermissions(getFullId(), getFullId()).clearFlags();
            DatabaseManager.getDatabase().removePermissions(this.getFullId(), this.getFullId());
            return;
        }
        
        EnumSet<ChunkyPermissions.Flags> notSet = EnumSet.complementOf(flags);
        for (ChunkyPermissions.Flags flag : flags) {
            setDefaultPerm(flag, true);
        }
        for (ChunkyPermissions.Flags flag : notSet) {
            setDefaultPerm(flag, false);
        }
    }

    public final void setDefaultPerm(ChunkyPermissions.Flags type, boolean status, boolean persist) {
        ChunkyPermissions perms = ChunkyManager.getPermissions(getFullId(), getFullId());
        // Set flag
        perms.setFlag(type, status);

        // Persist if requested
        if (persist) {
            DatabaseManager.getDatabase().updateDefaultPermissions(this.getFullId(), perms.getFlags());
        }
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    public final boolean isLeaf() {
        return ownables.size() == 0;
    }


    /**
     * Returns all ownables of this object.  You may not change the structure/values of this HashMap.
     * @return
     */
    public final HashMap<String, HashSet<ChunkyObject>> getOwnables() {
        @SuppressWarnings("unchecked")
        HashMap<String, HashSet<ChunkyObject>> ownables = (HashMap<String, HashSet<ChunkyObject>>)this.ownables.clone();
        return ownables;
    }

    public final String toString() {
        return super.toString();
    }
}
