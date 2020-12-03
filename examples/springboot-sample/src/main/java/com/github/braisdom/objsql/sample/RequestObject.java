package com.github.braisdom.objsql.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility for prettying codes, and convenient methods provided. <br/>
 * In PostMapping of Springboot, it is a formal parameter of @RequestBody:
 * <pre>
 *     @PostMapping("/orders/{memberNo}")
 *     public ResponseObject makeOrder(@RequestBody RequestObject rawOrder)...
 * </pre>
 *
 * In GetMapping of Springboot, it will be adapted by <code>RequestObject.create</code> method
 * <pre>
 *     @GetMapping(value = "/product/sales_analysis")
 *     public ResponseObject analysisProductSales(@RequestParam Map<String, String> rawRequest) {
 *         RequestObject requestObject = RequestObject.create(rawRequest);
 *         ...
 *     }
 * </pre>
 *
 * @see RequestObject#create(Map)
 */
public class RequestObject extends HashMap<String, Object> {

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

    public String[] getStringArray(String key) {
        return getStringArray(key, ",");
    }

    public String[] getStringArray(String key, String sep) {
        Object value = get(key);

        if(value == null)
            return new String[0];

        if(value instanceof String[])
            return (String[]) value;

        if(value instanceof String)
            return ((String) value).split(sep);

        return get(String[].class, key);
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

    /**
     * It is a adapter method for request parameters of GetMapping in Springboot
     * @param rawRequest the request parameters from GetMapping in Springboot
     * @return
     */
    public static RequestObject create(Map<String, String> rawRequest) {
        Map<String, Object> request = new HashMap<>(rawRequest);
        return new RequestObject(request);
    }
}
