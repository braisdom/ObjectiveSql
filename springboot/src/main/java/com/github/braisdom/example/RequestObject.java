package com.github.braisdom.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility for prettying codes, and convenient methods provided.
 */
public class RequestObject extends HashMap<String, Object> {

    public RequestObject() {
        super();
    }

    public RequestObject(Map<String, Object> nested) {
        super(nested);
    }

    public RequestObject[] getArray(String key) {
        List<Map<String, Object>> nestedObjects = get(List.class, key);
        if (nestedObjects != null) {
            return nestedObjects.stream()
                    .map((nestedObject -> new RequestObject(nestedObject)))
                    .toArray(RequestObject[]::new);
        } else return new RequestObject[0];
    }

    public <T> T get(Class<T> rawType, String key) {
        return rawType.cast(super.get(key));
    }

    public String getString(String key) {
        Object raw = get(key);
        if (raw != null)
            return String.valueOf(raw);
        else return null;
    }

    public Float getFloat(String key) {
        Object raw = get(key);
        if (raw != null) {
            if (raw instanceof Float)
                return (Float) raw;
            else return Float.valueOf(raw.toString());
        } else return null;
    }
}
