package org.getchunky.actionmenu.sign;

import org.getchunky.actionmenu.PlayerActionMenuItem;

import java.util.List;

/**
 * @author dumptruckman
 */
public abstract class SignActionMenuItem extends PlayerActionMenuItem {

    protected List<String> lines;

    /**
     * ActionMenuItem designed specifically for the SignActionMenu.  It contains 4 lines of text.
     *
     * @param lines List of text lines.
     */
    public SignActionMenuItem(List<String> lines) {
        this.lines = lines;
    }

    /**
     * Get the lines of text on this sign.
     *
     * @return Lines of text on the sign.
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Returns a single line of text at specified index.
     *
     * @param index Index of text line ot retrieve.
     * @return Line of text at index.
     */
    public String getLine(int index) {
        return lines.get(index);
    }

    /**
     * Sets the text for this menu item.
     *
     * @param lines List of text.
     */
    public void setLines(List<String> lines) {
        this.lines = lines;
        setChanged();
        notifyObservers();
    }

    /**
     * Sets a specific line of this menu item's text.
     *
     * @param index Index of line.
     * @param line  Text for line.
     * @throws IndexOutOfBoundsException
     */
    public void setLine(int index, String line) throws IndexOutOfBoundsException {
        if (index > 3) {
            throw new IndexOutOfBoundsException("Only 4 lines allowed");
        }
        lines.set(index, line);
        setChanged();
        notifyObservers();
    }
}
