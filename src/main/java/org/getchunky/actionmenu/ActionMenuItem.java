package org.getchunky.actionmenu;

import org.bukkit.command.CommandSender;

import java.util.Observable;

/**
 * @author dumptruckman
 */
public abstract class ActionMenuItem extends Observable implements Runnable {

    private String text;
    private CommandSender sender;

    /**
     * Creates a menu item with no text.
     */
    public ActionMenuItem() {
        this("");
    }

    /**
     * Creates a menu item with specified text.
     *
     * @param text Text for menu item.
     */
    public ActionMenuItem(String text) {
        this.text = text;
    }

    /**
     * Compares this to another object.
     *
     * @param o Object to compare to.
     * @return True if o is an instance of ActionMenuItem and their text is the same.
     */
    public boolean equals(Object o) {
        return (o instanceof ActionMenuItem && o.toString().equals(this.toString()));
    }

    /**
     * Empty method.  Used to update menu items in some way.
     */
    public void update() {

    }

    /**
     * When the sign is cycled it sets the player causing the cycle event as the player interacting with the sign.
     *
     * @param sender Whoever caused the cycle event.  Could be null.
     */
    final protected void onCycle(CommandSender sender) {
        setInteracting(sender);
        onCycle();
    }

    /**
     * Empty method that is called when the menu is cycled.
     */
    protected void onCycle() {

    }

    /**
     * When the menu item is selected it sets the player causing the cycle event as the player interacting with the sign.
     *
     * @param sender Whoever caused the selection event.  Could be null.
     */
    final protected void onSelect(CommandSender sender) {
        setInteracting(sender);
        onSelect();
    }

    /**
     * Empty method that is called when the menu item is selected.
     */
    protected void onSelect() {

    }

    /**
     * Sets the menu item's text.
     *
     * @param text Text for menu item.
     */
    public void setText(String text) {
        this.text = text;
        setChanged();
        notifyObservers();
    }

    /**
     * Gets the menu item's text.
     *
     * @return Text of menu item.
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the menu item's text.
     *
     * @return Text of menu item.
     */
    public String toString() {
        return getText();
    }

    /**
     * Sets the player for this menu item's interaction.
     *
     * @param sender CommandSender interacting with the menu.
     */
    protected void setInteracting(CommandSender sender) {
        this.sender = sender;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieve the last person to interact with this menu.
     *
     * @return The last interactor.
     */
    public CommandSender getSender() {
        return sender;
    }
}
