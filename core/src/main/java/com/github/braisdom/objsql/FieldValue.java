package com.github.braisdom.objsql;

import java.sql.SQLType;

public interface FieldValue {

    SQLType getSQLType();

    Object getValue();

    void resetValue(Object value);
}
