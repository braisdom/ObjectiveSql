package com.github.braisdom.objsql;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

public class DefaultForcedFieldValueConverter implements ForcedFieldValueConverter {

    @Override
    public Float toFloat(Object raw) {
        if (raw instanceof Float)
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

    @Override
    public Double toDouble(Object raw) {
        if (raw instanceof Double)
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

    @Override
    public Short toShort(Object raw) {
        if (raw instanceof Short)
            return (Short) raw;
        else if (raw instanceof Integer)
            return Short.valueOf(String.valueOf(raw));
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Short", String.valueOf(raw)));
    }

    @Override
    public Integer toInteger(Object raw) {
        if (raw instanceof Integer)
            return (Integer) raw;
        else if (raw instanceof Long)
            return Integer.valueOf(String.valueOf(raw));
        else if (raw instanceof BigInteger)
            return ((BigInteger) raw).intValue();
        else if (raw instanceof String)
            return Integer.valueOf((String) raw);
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Integer", String.valueOf(raw)));
    }

    @Override
    public Long toLong(Object raw) {
        if (raw instanceof Long)
            return (Long) raw;
        else if (raw instanceof Integer)
            return Long.valueOf(String.valueOf(raw));
        else if (raw instanceof String)
            return Long.valueOf((String) raw);
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Long", String.valueOf(raw)));
    }

    @Override
    public Boolean toBoolean(Object raw) {
        if (raw instanceof Integer)
            return ((Integer) raw) == 1;
        else if (raw instanceof Short)
            return ((Short) raw) == 1;
        else if (raw instanceof String)
            return Boolean.valueOf((String) raw);
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Boolean", String.valueOf(raw)));
    }

    @Override
    public Timestamp toTimestamp(Object raw) {
        if (raw instanceof String)
            return Timestamp.valueOf((String) raw);
        else if (raw instanceof Long)
            return new Timestamp((Long) raw);
        else if (raw instanceof Date)
            return new Timestamp(((Date) raw).getTime());
        throw new IllegalArgumentException(String.format("'%s' cannot convert to Timestamp", String.valueOf(raw)));
    }


}
