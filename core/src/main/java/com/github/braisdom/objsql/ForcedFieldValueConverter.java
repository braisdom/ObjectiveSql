package com.github.braisdom.objsql;

import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * The class is used for converting the field value from various raw value.
 * Such as, a string into float, a integer into float, etc. It is an utility tools
 * for converting JSON to Java Class or column value to Java Class.
 *
 * The exceptions will be thrown while encounter invalid format.
 */
public interface ForcedFieldValueConverter {

    Object convert(Field field, Object originalValue);

    Object convert(Class<?> fieldType, Object originalValue);
}
