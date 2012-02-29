package org.getchunky.chunky.object;

import org.getchunky.chunkie.exceptions.ChunkyObjectNotInitializedException;
import org.getchunky.chunky.persistence.ChunkyPersistable;

/**
 * @author dumptruckman, SwearWord
 */
public interface ChunkyObject extends ChunkyPersistable {

    public boolean save() throws ChunkyObjectNotInitializedException;

    public void delete();

    public String getName();

    public String getId();

    public ChunkyObject setId(String id);

    public String getType();

    public String getFullId();

    public ChunkyObject setName(String name);

    public boolean isOwned();

    /**
     * Checks if o is owned by this object.
     *
     * @param o object to hasPerm ownership for
     * @return true if this object owns o
     */
    public boolean isOwnerOf(ChunkyObject o);

    /**
     * @param owner Check if this object is an ancestor.
     * @return
     */
    public boolean isOwnedBy(ChunkyObject owner);

    public boolean isDirectlyOwnedBy(ChunkyObject owner);

    /**
     * Returns the owner <code>TreeNode</code> of the receiver.
     */
    public ChunkyObject getOwner();
}
