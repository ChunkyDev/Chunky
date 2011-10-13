package org.getchunky.actionmenu;

import org.bukkit.entity.Player;

/**
 * @author dumptruckman
 */
public abstract class PlayerActionMenuItem extends ActionMenuItem {

    public PlayerActionMenuItem() {
        this("");
    }

    public PlayerActionMenuItem(String text) {
        super(text);
    }

    /**
     * Retrieves the player interacting with this menu.
     *
     * @return The player interacting with the sign or null if it is not a Player.
     */
    public Player getPlayer() {
        if (getSender() instanceof Player) {
            return (Player) getSender();
        } else {
            return null;
        }
    }
}
