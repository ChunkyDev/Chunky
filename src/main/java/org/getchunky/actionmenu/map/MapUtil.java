package org.getchunky.actionmenu.map;

import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapFont;

/**
 * @author dumptruckman
 */
public class MapUtil {

    private static int CHAR_SPACE = 3;

    protected static int writeLines(MapActionMenu menu, MapCanvas canvas, int x, int y, MapFont font, String text) {
        int xPos = x;
        int yPos = y;
        int xLimit = menu.getWidth() - x;
        int yLimit = menu.getHeight() - y;
        int spaceWidth = font.getWidth(" ") + CHAR_SPACE;
        String[] words = text.split("\\s");
        String lineBuffer = "";
        int lineWidth = 0;
        for (int i = 0; i < words.length; i++) {
            int wordWidth = font.getWidth(words[i]);
            if (wordWidth <= xLimit) {
                if (xPos + lineWidth + wordWidth <= xLimit) {
                    lineBuffer += words[i] + " ";
                    lineWidth = font.getWidth(lineBuffer);
                } else {
                    canvas.drawText(xPos, yPos, font, lineBuffer);
                    lineBuffer = "";
                    lineWidth = 0;
                    yPos += font.getHeight() + menu.getLineSpacing();
                    i--;
                    continue;
                }
            } else {
                char[] chars = words[i].toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    String sChar = Character.toString(chars[j]);
                    int charWidth = font.getWidth(sChar);
                    if (xPos + lineWidth + charWidth < xLimit) {
                        lineBuffer += sChar;
                        lineWidth = font.getWidth(lineBuffer);
                    } else {
                        canvas.drawText(xPos, yPos, font, lineBuffer);
                        lineBuffer = "";
                        lineWidth = 0;
                        yPos += font.getHeight() + menu.getLineSpacing();
                        j--;
                        continue;
                    }
                }
                if (lineWidth != 0) {
                    lineBuffer += " ";
                    lineWidth = font.getWidth(lineBuffer);
                }
            }
        }
        if (lineWidth != 0) {
            canvas.drawText(xPos, yPos, font, lineBuffer);
            yPos += font.getHeight() + menu.getLineSpacing();
        }
        return yPos;
    }
}
