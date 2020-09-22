package com.github.braisdom.example;

import com.github.braisdom.objsql.util.WordUtil;

import java.util.HashMap;
import java.util.Map;

public class ResponseObject {
    private final boolean status;
    private final Object result;

    private ResponseObject(boolean status, Object result) {
        this.status = status;
        this.result = result;
    }

    public boolean isStatus() {
        return status;
    }

    public Object getResult() {
        return result;
    }

    public static ResponseObject createSuccessResponse() {
        return new ResponseObject(true, null);
    }

    public static ResponseObject createSuccessResponse(Object result) {
        return createSuccessResponse(WordUtil
                .camelize(result.getClass().getSimpleName(), true), result);
    }

    public static ResponseObject createSuccessResponse(String name, Object result) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(name, result);
        return new ResponseObject(true, resultMap);
    }

    public static ResponseObject createFailureResponse(Object result) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(WordUtil.camelize(result.getClass().getSimpleName(), true), result);
        return new ResponseObject(true, resultMap);
    }
}
