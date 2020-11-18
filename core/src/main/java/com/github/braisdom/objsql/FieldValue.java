package com.github.braisdom.objsql;

import java.sql.SQLType;

public interface FieldValue {

    boolean isNull();

    SQLType getSQLType();

    Object getValue();

    void setValue(Object value);
}
