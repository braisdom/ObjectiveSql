package com.github.braisdom.objsql;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

public class DefaultForcedFieldValueConverter implements ForcedFieldValueConverter {

    private Float toFloat(Object raw) {
        if(raw == null)
            return null;
        else if (raw instanceof Float)
            return (Float) raw;
        else if (raw instanceof Double)
            return Float.valueOf(String.valueOf(raw));
        else if (raw instanceof Integer)
            return Float.valueOf(String.valueOf(raw));
        else if (raw instanceof String)
            return Float.valueOf((String) raw);
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Float", String.valueOf(raw)));
    }

    private Double toDouble(Object raw) {
        if(raw == null)
            return null;
        else if (raw instanceof Double)
            return (Double) raw;
        else if (raw instanceof Float)
            return Double.valueOf(String.valueOf(raw));
        else if (raw instanceof Integer)
            return Double.valueOf(String.valueOf(raw));
        else if (raw instanceof String)
            return Double.valueOf((String) raw);
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Double", String.valueOf(raw)));
    }

    private Short toShort(Object raw) {
        if(raw == null)
            return null;
        else if (raw instanceof Short)
            return (Short) raw;
        else if (raw instanceof Integer)
            return Short.valueOf(String.valueOf(raw));
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Short", String.valueOf(raw)));
    }

    private Integer toInteger(Object raw) {
        if(raw == null)
            return null;
        else if (raw instanceof Integer)
            return (Integer) raw;
        else if (raw instanceof Long)
            return Integer.valueOf(String.valueOf(raw));
        else if (raw instanceof BigInteger)
            return ((BigInteger) raw).intValue();
        else if (raw instanceof String)
            return Integer.valueOf((String) raw);
        else if (raw instanceof BigDecimal)
            return ((BigDecimal) raw).intValue();
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Integer", String.valueOf(raw)));
    }

    private Long toLong(Object raw) {
        if(raw == null)
            return null;
        else if (raw instanceof Long)
            return (Long) raw;
        else if (raw instanceof Integer)
            return Long.valueOf(String.valueOf(raw));
        else if (raw instanceof String)
            return Long.valueOf((String) raw);
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Long", raw));
    }

    private Boolean toBoolean(Object raw) {
        if(raw == null)
            return null;
        else if(raw instanceof Boolean)
            return (Boolean) raw;
        else if (raw instanceof Integer)
            return ((Integer) raw) == 1;
        else if (raw instanceof Short)
            return ((Short) raw) == 1;
        else if (raw instanceof String)
            return Boolean.valueOf((String) raw);
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Boolean", String.valueOf(raw)));
    }

    private Timestamp toTimestamp(Object raw) {
        if(raw == null)
            return null;
        else if(raw instanceof Timestamp)
            return (Timestamp) raw;
        else if (raw instanceof String)
            return Timestamp.valueOf((String) raw);
        else if (raw instanceof Long)
            return new Timestamp((Long) raw);
        else if (raw instanceof Date)
            return new Timestamp(((Date) raw).getTime());
        throw new IllegalArgumentException(String.format("'%s' cannot convert to Timestamp", String.valueOf(raw)));
    }

    @Override
    public Object convert(Field field, Object originalValue) {
        Class fieldType = field.getType();
        return convert(fieldType, originalValue);
    }

    @Override
    public Object convert(Class<?> fieldType, Object originalValue) {
        if(Float.class.isAssignableFrom(fieldType))
            return toFloat(originalValue);
        else if(Double.class.isAssignableFrom(fieldType))
            return toDouble(originalValue);
        else if(Integer.class.isAssignableFrom(fieldType))
            return toInteger(originalValue);
        else if(Short.class.isAssignableFrom(fieldType))
            return toShort(originalValue);
        else if(Long.class.isAssignableFrom(fieldType))
            return toLong(originalValue);
        else if(Boolean.class.isAssignableFrom(fieldType))
            return toBoolean(originalValue);
        else if(Timestamp.class.isAssignableFrom(fieldType))
            return toTimestamp(originalValue);

        return originalValue;
    }
}
