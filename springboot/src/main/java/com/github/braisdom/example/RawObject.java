package com.github.braisdom.example;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility for prettying codes
 */
public class RawObject extends HashMap<String, Object> {

    public RawObject() {
        super();
    }

    public RawObject(Map<String, Object> nested) {
        super(nested);
    }

    public <T> T get(Class<T> rawType, String key) {
        return rawType.cast(super.get(key));
    }
}
