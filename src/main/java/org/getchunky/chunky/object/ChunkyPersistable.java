package org.getchunky.chunky.object;

import org.getchunky.chunky.exceptions.ChunkyObjectNotInitializedException;
import org.getchunky.chunky.util.Logging;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * @author dumptruckman
 */
public class ChunkyPersistable {

    protected transient JSONObject data = new JSONObject();

    public void save() throws ChunkyObjectNotInitializedException {
        save(this.getClass());
    }

    private void save(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!Modifier.isTransient(field.getModifiers())) {
                try {
                    Object value = field.get(this);
                    if (value == null) continue;
                    Logging.debug("Attempting to save field: " + field.getName() + " with value: " + value.toString());
                    Object wrappedObject = JSONObject.wrap(value);
                    if (wrappedObject == null) {
                        Logging.warning("Attempting to save incompatible object type.");
                        Logging.warning("Field: " + field.getName() + " in Class: " + clazz.getName());
                        Logging.warning("You may mark this field as transient to skip persistence.");
                        continue;
                    }
                    data.put(field.getName(), wrappedObject);
                    Logging.debug(data.toString());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            save(superClazz);
        }
    }

    public void load(String json) throws JSONException {
        parseJsonData(json);
        fillFields(this.getClass());
    }

    private void parseJsonData(String json) {
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
            data.put(key, x.nextValue());

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

    private void fillFields(Class clazz) {
        if (data.names() == null) return;
        for (int i = 0; i < data.names().length(); i++) {
            String fieldName = data.names().get(i).toString();
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                try {
                    Logging.debug("Setting field \"" + fieldName + "\" to \"" + data.get(fieldName) + "\"");
                    field.set(this, data.get(fieldName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException ignore) {}
        }
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            fillFields(superClazz);
        }
    }

    public String toString() {
        return data.toString();
    }
}
