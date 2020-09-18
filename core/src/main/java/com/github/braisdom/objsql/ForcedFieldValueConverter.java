package com.github.braisdom.objsql;

import java.sql.Timestamp;

/**
 * The class is used for converting the field value from various raw value.
 * Such as, a string into float, a integer into float, etc. It is an utility tools
 * for converting JSON to Java Class or column value to Java Class.
 *
 * The exceptions will be thrown while encounter invalid format.
 */
public interface ForcedFieldValueConverter {

    Float toFloat(Object raw);

    Double toDouble(Object raw);

    Short toShort(Object raw);

    Integer toInteger(Object raw);

    Long toLong(Object raw);

    Boolean toBoolean(Object raw);

    Timestamp toTimestamp(Object raw);
}
