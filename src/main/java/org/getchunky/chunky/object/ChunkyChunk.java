package org.getchunky.chunky.object;

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
            this.put("location", coord.toString());
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
            return new ChunkyCoordinates(this.getString("location"));
        } catch (JSONException e) {
            Logging.severe(e.getMessage());
            return null;
        }
    }
}
