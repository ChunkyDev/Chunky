package org.getchunky.chunkie.persistance;

import org.getchunky.chunky.persistence.ChunkyPersistable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * @author dumptruckman, SwearWord
 */
public class IChunkyPersistable implements ChunkyPersistable {

    private JSONObject data = new JSONObject();

    /**
     * Gets the JSON data of your object.  This is where all nice and good magical data is stored.
     *
     * @return JSONObject of your object's data.
     */
    public final JSONObject getData() {
        return data;
    }

    /**
     * Returns the JSON data as a string.
     *
     * @return The string of JSON data.
     */
    public final String toJSONString() {
        return data.toString();
    }


    public void load(String json) throws JSONException {
        JSONTokener x = new JSONTokener(json);
        char c;
        String key;

        if (x.nextClean() != '{') {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        }
        for (; ; ) {
            c = x.nextClean();
            switch (c) {
                case 0:
                    throw x.syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return;
                default:
                    x.back();
                    key = x.nextValue().toString();
            }

            // The key is followed by ':'. We will also tolerate '=' or '=>'.

            c = x.nextClean();
            if (c == '=') {
                if (x.next() != '>') {
                    x.back();
                }
            } else if (c != ':') {
                throw x.syntaxError("Expected a ':' after a key");
            }
            getData().put(key, x.nextValue());

            // Pairs are separated by ','. We will also tolerate ';'.

            switch (x.nextClean()) {
                case ';':
                case ',':
                    if (x.nextClean() == '}') {
                        return;
                    }
                    x.back();
                    break;
                case '}':
                    return;
                default:
                    throw x.syntaxError("Expected a ',' or '}'");
            }
        }
    }
}
