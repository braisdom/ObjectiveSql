package com.github.braisdom.objsql;

import java.lang.reflect.Field;

/**
 * The class is used for converting the field value from various raw value.
 * Such as, a string into float, a integer into float, etc. It is an utility tools
 * for converting JSON to Java Bean.
 * For example:<br/>
 * <pre>
 *     ForcedFieldValueConverter convert = new DefaultForcedFieldValueConverter();
 *
 *     String rawFieldValue = "0.01";
 *     Float value = converter.convert(Float.class, rawFieldValue);
 * </pre>
 *
 * The exceptions will be thrown while encounter invalid format.
 */
public interface ForcedFieldValueConverter {

    /**
     * Convert the field value with the type of field.
     */
    Object convert(Field field, Object originalValue);

    /**
     * Convert the field value with the given field type.
     */
    Object convert(Class<?> fieldType, Object originalValue);
}
