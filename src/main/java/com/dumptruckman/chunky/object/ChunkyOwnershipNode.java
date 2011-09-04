package com.dumptruckman.chunky.object;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyOwnershipNode {

    /**
     * Returns the child <code>TreeNode</code> at index
     * <code>childIndex</code>.
     */
    protected ChunkyObject parent;
    protected Vector<ChunkyObject> children = new Vector<ChunkyObject>();
    protected boolean allowsChildren = true;

    /**
     * Returns the number of children <code>TreeNode</code>s the receiver
     * contains.
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    public ChunkyObject getParent() {
        return parent;
    }

    public void setParent(ChunkyObject object) {
        this.parent = object;
    }

    /**
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    public Enumeration children() {
        return children.elements();
    }

    /**
     * Returns true if the receiver allows children.
     */
    public boolean getAllowsChildren() {
        return allowsChildren;
    }

    public void setAllowsChildren(Boolean allows) {
        this.allowsChildren = allows;
    }
}
