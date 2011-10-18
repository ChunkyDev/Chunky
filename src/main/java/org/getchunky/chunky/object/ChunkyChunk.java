package org.getchunky.chunky.object;

import org.getchunky.chunky.ChunkyManager;
import org.getchunky.chunky.config.Config;
import org.getchunky.chunky.util.Logging;
import org.json.JSONException;

/**
 * @author dumptruckman, SwearWord
 */
public class ChunkyChunk extends ChunkyLocationObject {

    /**
     * Sets the coordinates of this chunk.  You probably don't need to use this.
     *
     * @param coord Coordinates of chunk
     */
    public final ChunkyChunk setCoord(ChunkyCoordinates coord) {
        try {
            getData().put("location", coord.toString());
        } catch (JSONException e) {
            Logging.warning(e.getMessage());
        }
        return this;
    }


    /**
     * Gets the coordinates of this chunk.
     *
     * @return Chunk coordinates
     */
    public final ChunkyCoordinates getCoord() {
        try {
            return new ChunkyCoordinates(getData().getString("location"));
        } catch (JSONException e) {
            Logging.severe(e.getMessage());
            return null;
        }
    }

    public String getChunkDisplayName() {
        return Config.getChunkDisplayName(this);
    }

    public ChunkyChunk getRelative(final int modX, final int modZ) {
        return ChunkyManager.getChunkyChunk(new ChunkyCoordinates(
                this.getCoord().getWorld(),
                this.getCoord().getX() + modX,
                this.getCoord().getZ() + modZ));
    }

    public ChunkyChunk getRelative(ChunkFace face) {
        return getRelative(face, 1);
    }

    public ChunkyChunk getRelative(ChunkFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModZ() * distance);
    }
    
    public ChunkyChunk[] getDirectlyAdjacentChunks() {
        ChunkyChunk[] chunks = new ChunkyChunk[4];
        chunks[0] = this.getRelative(ChunkFace.NORTH);
        chunks[1] = this.getRelative(ChunkFace.EAST);
        chunks[2] = this.getRelative(ChunkFace.SOUTH);
        chunks[3] = this.getRelative(ChunkFace.WEST);
        return chunks;
    }
}
