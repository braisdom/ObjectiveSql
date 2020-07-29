package com.github.braisdom.funcsql.example;

import com.github.braisdom.funcsql.ColumnTransitional;
import com.github.braisdom.funcsql.DomainModelDescriptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonColumnTransitional implements ColumnTransitional {

    private Gson gson = new GsonBuilder().create();

    @Override
    public Object sinking(Object object, DomainModelDescriptor domainModelDescriptor, String fieldName, Object fieldValue) {
        return gson.toJson(fieldValue);
    }

    @Override
    public Object rising(Object object, DomainModelDescriptor domainModelDescriptor, String fieldName, Object fieldValue) {
        return gson.fromJson(String.valueOf(fieldValue), domainModelDescriptor.getFieldTypeByFieldName(fieldName));
    }
}