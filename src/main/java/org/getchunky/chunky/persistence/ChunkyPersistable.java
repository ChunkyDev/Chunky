package org.getchunky.chunky.persistence;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This really needs to be something more general some how.  Not really possible with current persistence implementation.
 */
public interface ChunkyPersistable {

    /**
     * Gets the JSON data of your object.  This is where all nice and good magical data is stored.
     *
     * @return JSONObject of your object's data.
     */
    public JSONObject getData();

    /**
     * Returns the JSON data as a string.
     *
     * @return The string of JSON data.
     */
    public String toJSONString();

    /**
     * Loads a JSON string in to the object.
     *
     * @param json String of data representing your object.
     * @throws JSONException
     */
    public abstract void load(String json) throws JSONException;
}
