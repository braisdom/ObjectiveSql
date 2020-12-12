package com.github.braisdom.objsql.sample;

import com.github.braisdom.objsql.util.Inflector;

import java.util.HashMap;
import java.util.Map;

public class ResponseObject {

    public static final String STATUS_SUCC = "success";
    public static final String STATUS_FAULT = "fault";

    private final String status;
    private final Object result;

    private ResponseObject(String status, Object result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public Object getResult() {
        return result;
    }

    public static ResponseObject createSuccessResponse() {
        return new ResponseObject(STATUS_SUCC, null);
    }


    public static ResponseObject createSuccessResponse(Object result) {
        return new ResponseObject(STATUS_SUCC, result);
    }

    public static ResponseObject createFailureResponse(Object result) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Inflector.getInstance().camelize(result.getClass().getSimpleName(), true), result);
        return new ResponseObject(STATUS_SUCC, resultMap);
    }
}
