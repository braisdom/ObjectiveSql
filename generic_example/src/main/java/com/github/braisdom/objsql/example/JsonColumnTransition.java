package com.github.braisdom.objsql.example;

import com.github.braisdom.objsql.FieldValue;
import com.github.braisdom.objsql.TableRowAdapter;
import com.github.braisdom.objsql.transition.ColumnTransition;
import com.google.gson.Gson ;
import com.google.gson.GsonBuilder;

import java.sql.DatabaseMetaData;
import java.sql.ResultSetMetaData;

public class JsonColumnTransition implements ColumnTransition {

    private Gson gson = new GsonBuilder().create();

    @Override
    public Object sinking(DatabaseMetaData databaseMetaData, Object object,
                          TableRowAdapter tableRowDescriptor, String fieldName, FieldValue fieldValue) {
        if(fieldValue != null)
            return gson.toJson(fieldValue.getValue());
        return null;
    }

    @Override
    public Object rising(DatabaseMetaData databaseMetaData, ResultSetMetaData resultSetMetaData,
                         Object object, TableRowAdapter tableRowDescriptor, String fieldName, Object columnValue) {
        if(columnValue != null)
            return gson.fromJson(String.valueOf(columnValue), tableRowDescriptor.getFieldType(fieldName));
        return null;
    }
}