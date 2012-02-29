package org.getchunky.chunkie.object;

import org.getchunky.chunkie.ChunkyManager;
import org.getchunky.chunkie.config.Config;
import org.getchunky.chunkie.util.Logging;
import org.json.JSONException;

/**
 * @author dumptruckman, SwearWord
 */
public class IChunkyChunk extends IChunkyLocationObject {

    /**
     * Sets the coordinates of this chunk.  You probably don't need to use this.
     *
     * @param coord Coordinates of chunk
     */
    public final IChunkyChunk setCoord(ChunkyCoordinates coord) {
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

    public IChunkyChunk getRelative(final int modX, final int modZ) {
        return ChunkyManager.getChunkyChunk(new ChunkyCoordinates(
                this.getCoord().getWorld(),
                this.getCoord().getX() + modX,
                this.getCoord().getZ() + modZ));
    }

    public IChunkyChunk getRelative(ChunkFace face) {
        return getRelative(face, 1);
    }

    public IChunkyChunk getRelative(ChunkFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModZ() * distance);
    }
    
    public IChunkyChunk[] getDirectlyAdjacentChunks() {
        IChunkyChunk[] chunks = new IChunkyChunk[4];
        chunks[0] = this.getRelative(ChunkFace.NORTH);
        chunks[1] = this.getRelative(ChunkFace.EAST);
        chunks[2] = this.getRelative(ChunkFace.SOUTH);
        chunks[3] = this.getRelative(ChunkFace.WEST);
        return chunks;
    }
}
