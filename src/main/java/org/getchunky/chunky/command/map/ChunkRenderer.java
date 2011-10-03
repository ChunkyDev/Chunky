package org.getchunky.chunky.command.map;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.object.ChunkyChunk;
import org.getchunky.chunky.object.ChunkyPlayer;
import org.getchunky.chunky.permission.ChunkyPermissions;
import org.getchunky.chunky.util.Logging;


public class ChunkRenderer extends MapRenderer{


    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        int worldX = mapView.getCenterX()-64;
        int worldZ = mapView.getCenterZ()-64;
        mapView.setScale(MapView.Scale.CLOSEST);
        ChunkyPlayer chunkyPlayer = ChunkyManager.getChunkyPlayer(player);
        for(int x=0;x<128;x+=16) {
            for(int z=0;z<128;z+=16) {
                int realx = worldX + x;
                int realz = worldZ + z;
                ChunkyChunk chunk = ChunkyManager.getChunk(new Location(player.getWorld(),realx,0,realz));
                if(chunk.getOwner()==null) continue;
                Byte color = MapPalette.RED;

                if(chunk.getOwner().equals(chunkyPlayer)) color=MapPalette.BLUE;
                //else if(chunkyPlayer.hasPerm(chunk, ChunkyPermissions.Flags.BUILD)) color=MapPalette.LIGHT_GREEN;

                drawChunk(x,z,mapCanvas,color);
            }}

    }

    private void drawChunk(int x, int z, MapCanvas canvas, Byte palette) {
        for(int xo=0;xo<16;xo++) {
            for(int zo=0;zo<16;zo++) {
                canvas.setPixel(x+xo,z+zo-12, palette);
            }
        }
    }

    public static void addToMap(MapView map) {

        map.addRenderer(new ChunkRenderer());
    }

}
