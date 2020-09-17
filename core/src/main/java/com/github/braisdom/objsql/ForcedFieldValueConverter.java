package com.github.braisdom.objsql;

import java.sql.Timestamp;

public interface ForcedFieldValueConverter {

    Float risingFloat(Object raw);

    Double risingDouble(Object raw);

    Short risingShort(Object raw);

    Integer risingInteger(Object raw);

    Long risingLong(Object raw);

    Boolean risingBoolean(Object raw);

    Timestamp risingTimestamp(Object raw);
}
