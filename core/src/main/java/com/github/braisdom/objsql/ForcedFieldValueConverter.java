package com.github.braisdom.objsql;

import java.sql.Timestamp;

public interface ForcedFieldValueConverter {

    Float toFloat(Object raw);

    Double toDouble(Object raw);

    Short toShort(Object raw);

    Integer toInteger(Object raw);

    Long toLong(Object raw);

    Boolean toBoolean(Object raw);

    Timestamp toTimestamp(Object raw);
}
