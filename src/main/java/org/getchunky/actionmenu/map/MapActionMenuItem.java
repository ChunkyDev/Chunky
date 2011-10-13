package org.getchunky.actionmenu.map;

import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;
import org.getchunky.actionmenu.PlayerActionMenuItem;

/**
 * @author dumptruckman
 */
public abstract class MapActionMenuItem extends PlayerActionMenuItem {

    MapFont font;
    int indent;
    int lineSpacing;

    public MapActionMenuItem(String text) {
        super(text);
        font = MinecraftFont.Font;
        indent = 5;
        lineSpacing = 3;
    }

    public MapFont getFont() {
        return font;
    }

    public MapActionMenuItem setFont(MapFont font) {
        this.font = font;
        return this;
    }

    public int getIndent() {
        return indent;
    }

    public MapActionMenuItem setIndent(int pixels) {
        this.indent = pixels;
        return this;
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public MapActionMenuItem setLineSpacing(int pixels) {
        this.lineSpacing = pixels;
        return this;
    }
}
