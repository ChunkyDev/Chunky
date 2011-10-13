package org.getchunky.actionmenu;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * @author dumptruckman
 */
public abstract class ActionMenu implements Iterable, Observer {

    private List<ActionMenuItem> contents = new ArrayList<ActionMenuItem>();
    private int selectedIndex = 0;
    private List<String> header = new ArrayList<String>();
    private List<String> footer = new ArrayList<String>();
    private JavaPlugin plugin;

    public ActionMenu(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ActionMenu() {
        this(null);
    }

    /**
     * Retrieves the plugin stored in this menu, if any.
     *
     * @return The plugin running the menu or null.
     */
    final public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Adds a menu item to the end of the menu.
     *
     * @param item Menu item to add to the menu.
     * @return Index of the new menu item.
     */
    public Integer add(ActionMenuItem item) {
        contents.add(item);
        item.addObserver(this);
        onChange();
        return contents.size() - 1;
    }

    /**
     * Removes all of the items from this menu. The menu will be empty after this call returns.
     */
    public void clear() {
        contents.clear();
        onChange();
    }

    /**
     * Returns true if this menu contains the specified item. More formally, returns true if and only if this menu contains at least one item i such that (item==null ? i==null : item.equals(i)).
     *
     * @param item Item whose presence in this menu is to be tested.
     * @return true If this menu contains the specified item.
     */
    public boolean contains(ActionMenuItem item) {
        return contents.contains(item);
    }

    /**
     * Returns the item at the specified position in this menu.
     *
     * @param index Index of the item to return.
     * @return The item at the specified index of this menu.
     * @throws IndexOutOfBoundsException
     */
    public ActionMenuItem get(int index) throws IndexOutOfBoundsException {
        return contents.get(index);
    }

    /**
     * Returns true if this menu contains no items.
     *
     * @return true if this menu contains no items.
     */
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    /**
     * Returns an iterator over the items in this menu in proper sequence.
     *
     * @return an iterator over the items in this menu in proper sequence.
     */
    public Iterator<ActionMenuItem> iterator() {
        return contents.iterator();
    }

    /**
     * Returns the index of the first occurrence of the specified itme in this menu, or -1 if this menu does not contain the item. More formally, returns the lowest index i such that (item==null ? get(i)==null : item.equals(get(i))), or -1 if there is no such index.
     *
     * @param item Item to search for.
     * @return The index of the first occurrence of the specified item in this menu, or -1 if this menu does not contain the item.
     */
    public Integer indexOf(ActionMenuItem item) {
        return contents.indexOf(item);
    }

    /**
     * Returns the index of the last occurrence of the specified item in this menu, or -1 if this menu does not contain the item. More formally, returns the highest index i such that (item==null ? get(i)==null : item.equals(get(i))), or -1 if there is no such index.
     *
     * @param item Item to search for.
     * @return The index of the last occurrence of the specified item in this menu, or -1 if this menu does not contain the item.
     */
    public Integer lastIndexOf(ActionMenuItem item) {
        return contents.lastIndexOf(item);
    }

    /**
     * Removes the menu item at the specified position in this menu. Shifts any subsequent menu items to the left (subtracts one from their indices). Returns the menu item that was removed from the menu.
     *
     * @param index Index of menu item to remove
     * @return Menu item removed.
     * @throws IndexOutOfBoundsException
     */
    public ActionMenuItem remove(int index) throws IndexOutOfBoundsException {
        ActionMenuItem item = contents.remove(index);
        onChange();
        return item;
    }

    /**
     * Removes the first occurrence of the specified item from this menu, if it is present. If this menu does not contain the item, it is unchanged. More formally, removes the item with the lowest index i such that (item==null ? get(i)==null : item.equals(get(i))) (if such an item exists). Returns true if this menu contained the specified item (or equivalently, if this menu changed as a result of the call).
     *
     * @param item element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public boolean remove(ActionMenuItem item) {
        boolean result = contents.remove(item);
        onChange();
        return result;
    }

    /**
     * Replaces the item at the specified position in this menu with the specified item.
     *
     * @param index Index of the item to replace.
     * @param item  Item to be stored at the specified position.
     * @return The item previously at the specified position
     */
    public ActionMenuItem set(int index, ActionMenuItem item) {
        ActionMenuItem result = contents.set(index, item);
        onChange();
        return result;
    }

    /**
     * Returns the number of items in this menu. If this menu contains more than Integer.MAX_VALUE items, returns Integer.MAX_VALUE.
     *
     * @return
     */
    public Integer size() {
        return contents.size();
    }

    /**
     * Returns the menu item that is selected.
     *
     * @return Selected menu item.
     */
    final public ActionMenuItem getSelectedItem() {
        return contents.get(selectedIndex);
    }

    /**
     * Get the index of the current menu selection.
     *
     * @return The selected menu item's index.
     */
    final public Integer getIndex() {
        return selectedIndex;
    }

    /**
     * Sets the current menu selection to specified index.  This will specify null for the interacting sender.
     *
     * @param index Sets the selection index to this.
     */
    final public void setIndex(int index) {
        setIndex(null, index);
    }

    /**
     * Sets the current menu selection to specified index.
     *
     * @param sender Person who activates the menu cycle.  This could be null if the sender is not important for the task.
     * @param index  Sets the selection index to this.
     */
    final public void setIndex(CommandSender sender, int index) {
        selectedIndex = index;
        contents.get(selectedIndex).onSelect(sender);
        onSelect();
        onChange();
    }

    /**
     * Set's the text to go before the menu options.
     *
     * @param firstLine       First line of header.
     * @param additionalLines Optional additional lines of header.
     */
    final public void setHeader(String firstLine, String... additionalLines) {
        header.clear();
        header.add(firstLine);

        if (additionalLines != null) {
            for (String line : additionalLines) {
                header.add(line);
            }
        }
        onChange();
    }

    /**
     * Returns the header for this menu.
     *
     * @return List of header lines.
     */
    final public List<String> getHeader() {
        return header;
    }

    /**
     * Set's the text to go after the menu options.
     *
     * @param firstLine       First line of footer.
     * @param additionalLines Optional additional lines of footer.
     */
    final public void setFooter(String firstLine, String... additionalLines) {
        footer.clear();
        footer.add(firstLine);

        if (additionalLines != null) {
            for (String line : additionalLines) {
                footer.add(line);
            }
        }
        onChange();
    }

    /**
     * Returns the footer for this menu.
     *
     * @return List of footer lines.
     */
    final public List<String> getFooter() {
        return footer;
    }

    /**
     * Specify a list of contents for this menu.
     *
     * @param contents List of menu items to set for this menu.
     */
    /*final public void setContents(List<ActionMenuItem> contents) {
        this.contents = contents;
    }*/

    /**
     * Retrieve the underlying ArrayList of menu items.
     *
     * @return Menu item list.
     */
    /*final public List<ActionMenuItem> getContents() {
        return contents;
    }*/

    /**
     * Cycles the selection through the menu options.
     */
    final public void cycleMenu() {
        cycleMenu(null, false);
    }

    /**
     * Cycles the selection through the menu option.
     *
     * @param sender Person who activates the menu cycle.  This could be null if the sender is not important for the task.
     */
    final public void cycleMenu(CommandSender sender) {
        cycleMenu(sender, false);
    }

    /**
     * Cycles the selection through the menu options.
     *
     * @param sender  Person who activates the menu cycle.  This could be null if the sender is not important for the task.
     * @param reverse If set to true, cycles backwards.
     */
    final public void cycleMenu(CommandSender sender, boolean reverse) {
        if (reverse) {
            selectedIndex--;
        } else {
            selectedIndex++;
        }
        if (selectedIndex < 0) {
            selectedIndex = contents.size() - 1;
        }
        if (selectedIndex >= contents.size()) {
            selectedIndex = 0;
        }
        triggerAllOnCycleEvent(sender);
        contents.get(selectedIndex).onSelect(sender);
        onCycle();
        onChange();
    }

    /**
     * Empty method.  Called when menu is cycled.
     */
    protected void onCycle() {

    }

    /**
     * Empty method.  Called when a menu item is selected directly.
     */
    protected void onSelect() {

    }

    /**
     * Empty method.  Called when menu contents are altered in any way or when the selection index is altered.
     */
    protected void onChange() {

    }

    /**
     * Calls onCycle() on each menu item in this menu
     *
     * @param sender Person who activates the menu cycle.  This could be null if the sender is not important for the task.
     */
    final protected void triggerAllOnCycleEvent(CommandSender sender) {
        for (ActionMenuItem item : contents) {
            item.onCycle(sender);
        }
    }

    /**
     * Perform doMenuItem() of the menu at specific index for the sender.
     *
     * @param sender Whoever is activating the menu item. This could be null if the sender is not important for the task.
     * @param index  Index of the menu item to perform.
     * @return The item performed.
     */
    final public ActionMenuItem doMenuItem(CommandSender sender, int index) {
        ActionMenuItem selectedItem = contents.get(index);
        selectedItem.setInteracting(sender);
        selectedItem.run();
        return selectedItem;
    }

    /**
     * Performs doMenuItem() on the currently selected menu item for the sender.
     *
     * @param sender Whoever is activating the menu item. This could be null if the sender is not important for the task.
     * @return the item performed
     */
    final public ActionMenuItem doSelectedMenuItem(CommandSender sender) {
        return doMenuItem(sender, selectedIndex);
    }

    /**
     * Runs the update method on all menu items.
     */
    final public void updateMenuItems() {
        for (ActionMenuItem item : contents) {
            item.update();
        }
    }

    public void update(Observable o, Object arg) {
        if (o instanceof ActionMenuItem) {
            onChange();
        }
    }

    /**
     * Shows the menu to a CommandSender.
     *
     * @param sender CommandSender to show menu to.  Possibly null depending on implementation.
     */
    public abstract void showMenu(CommandSender sender);
}
