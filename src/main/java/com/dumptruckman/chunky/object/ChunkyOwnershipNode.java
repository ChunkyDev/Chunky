package com.dumptruckman.chunky.object;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyOwnershipNode implements TreeNode {

    /**
     * Returns the child <code>TreeNode</code> at index
     * <code>childIndex</code>.
     */
    public TreeNode getChildAt(int childIndex) {
        return null; //TODO
    }

    /**
     * Returns the number of children <code>TreeNode</code>s the receiver
     * contains.
     */
    public int getChildCount() {
        return -1; // TODO
    }

    /**
     * Returns the parent <code>TreeNode</code> of the receiver.
     */
    public TreeNode getParent() {
        return null; //TODO
    }


    /**
     * Returns true if the receiver is a leaf.
     */
    public boolean isLeaf() {
        return false; // TODO
    }

    /**
     * Returns the children of the receiver as an <code>Enumeration</code>.
     */
    public Enumeration children() {
        return null; //TODO
    }

    /**
     * Returns the index of <code>node</code> in the receivers children.
     * If the receiver does not contain <code>node</code>, -1 will be
     * returned.
     */
    public int getIndex(TreeNode node) {
        return -1; // TODO
    }

    /**
     * Returns true if the receiver allows children.
     */
    public boolean getAllowsChildren() {
        return false; // TODO
    }
}
