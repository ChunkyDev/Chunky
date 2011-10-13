package org.getchunky.actionmenu.map;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

/**
 * @author dumptruckman
 */
public class MapActionMenuRenderer extends MapRenderer {

    private MapActionMenu menu;

    public MapActionMenuRenderer(MapActionMenu menu) {
        this.menu = menu;
    }

    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        if (menu.hasChanged()) {
            System.out.println("rendering");
            int y = menu.getY();
            int scrollPos = 0;//getScrollPos();
            for (int i = scrollPos; i < menu.getHeader().size(); i++) {
                y = MapUtil.writeLines(menu, mapCanvas, menu.getX(), y, menu.getFont(), menu.getHeader().get(i));
            }
            scrollPos -= menu.getHeader().size();
            if (scrollPos < 0) scrollPos = 0;
            for (int i = scrollPos; i < menu.size(); i++) {
                if (!(menu.get(i) instanceof MapActionMenuItem)) continue;
                MapActionMenuItem item = (MapActionMenuItem) menu.get(i);
                String text = "";
                if (menu.getIndex() == i) {
                    text += "-> ";
                }
                text += item.getText();
                y = MapUtil.writeLines(menu, mapCanvas, menu.getX(), y + 2, item.getFont(), text);
            }

            scrollPos -= menu.size();
            if (scrollPos < 0) scrollPos = 0;
            for (int i = scrollPos; i < menu.getFooter().size(); i++) {
                y = MapUtil.writeLines(menu, mapCanvas, menu.getX(), y + 2, menu.getFont(), menu.getFooter().get(i));
            }
            menu.setChanged(false);
        }
    }
}
