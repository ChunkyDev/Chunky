package org.getchunky.actionmenu.sign;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getchunky.actionmenu.ActionMenu;

import java.util.logging.Logger;

/**
 * @author dumprtuckman
 */
public class SignActionMenu extends ActionMenu {

    private Block block;

    /**
     * Create a sign based ActionMenu.
     *
     * @param block Block that this menu is assigned to.  It must be a Sign.
     */
    public SignActionMenu(Block block) {
        this.block = block;
    }

    /**
     * Returns the block associated with this menu.
     *
     * @return Block associated with this menu.
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Sets the block this menu is assigned to.
     *
     * @param block Block menu will be assigned to.
     */
    public void setBlock(Block block) {

    }

    /**
     * Cause this menu to show on the sign.  If a sender is specified it will send a false block update to them first to ensure the text is displayed.
     *
     * @param sender CommandSender to show menu to.  May be set to null.
     */
    public void showMenu(CommandSender sender) {
        showSelectedMenuItem(sender);
    }

    /**
     * Shows a single menu item on the sign.  Since signs are small, they a
     *
     * @param sender
     */
    private void showSelectedMenuItem(CommandSender sender) {
        Sign sign = null;
        try {
            sign = (Sign) this.block.getState();
        } catch (ClassCastException e) {
            Logger.getLogger("Minecraft.ActionMenu").severe("Tried to show a SignActionMenu on a non-sign block.");
            e.printStackTrace();
            return;
        }
        for (int i = 0; i < 4; i++) {
            sign.setLine(i, ((SignActionMenuItem) this.getSelectedItem()).getLine(i));
        }
        if (sender instanceof Player && sender != null) {
            ((Player) sender).sendBlockChange(sign.getBlock().getLocation(), 0, (byte) 0);
        }
        sign.update(true);
    }
}
