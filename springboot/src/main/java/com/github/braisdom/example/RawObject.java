package com.github.braisdom.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility for prettying codes, and convenient methods provided.
 */
public class RawObject extends HashMap<String, Object> {

    public RawObject() {
        super();
    }

    public RawObject(Map<String, Object> nested) {
        super(nested);
    }

    public RawObject[] getArray(String key) {
        List<Map<String, Object>> nestedObjects = get(List.class, key);
        if (nestedObjects != null) {
            return nestedObjects.stream()
                    .map((nestedObject -> new RawObject(nestedObject)))
                    .toArray(RawObject[]::new);
        } else return new RawObject[0];
    }

    public <T> T get(Class<T> rawType, String key) {
        return rawType.cast(super.get(key));
    }
}
