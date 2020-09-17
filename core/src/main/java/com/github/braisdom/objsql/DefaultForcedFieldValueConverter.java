package com.github.braisdom.objsql;

import java.math.BigInteger;
import java.sql.Timestamp;

public class DefaultForcedFieldValueConverter implements ForcedFieldValueConverter {
    
    @Override
    public Float risingFloat(Object raw) {
        if(raw instanceof Float)
            return (Float) raw;
        else if(raw instanceof Double)
            return Float.valueOf(String.valueOf(raw));
        else if(raw instanceof Integer)
            return Float.valueOf(String.valueOf(raw));
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Float", String.valueOf(raw)));
    }

    @Override
    public Double risingDouble(Object raw) {
        if(raw instanceof Double)
            return (Double) raw;
        else if(raw instanceof Float)
            return Double.valueOf(String.valueOf(raw));
        else if(raw instanceof Integer)
            return Double.valueOf(String.valueOf(raw));
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Double", String.valueOf(raw)));
    }

    @Override
    public Short risingShort(Object raw) {
        if(raw instanceof Short)
            return (Short) raw;
        else if(raw instanceof Integer)
            return Short.valueOf(String.valueOf(raw));
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Short", String.valueOf(raw)));
    }

    @Override
    public Integer risingInteger(Object raw) {
        if(raw instanceof Integer)
            return (Integer) raw;
        else if(raw instanceof Long)
            return Integer.valueOf(String.valueOf(raw));
        else if(raw instanceof BigInteger)
            return ((BigInteger)raw).intValue();
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Integer", String.valueOf(raw)));
    }

    @Override
    public Long risingLong(Object raw) {
        if(raw instanceof Long)
            return (Long) raw;
        else if(raw instanceof Integer)
            return Long.valueOf(String.valueOf(raw));
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Long", String.valueOf(raw)));
    }

    @Override
    public Boolean risingBoolean(Object raw) {
        if(raw instanceof Integer)
            return ((Integer)raw) == 1;
        if(raw instanceof Short)
            return ((Short)raw) == 1;
        else
            throw new IllegalArgumentException(String.format("'%s' cannot convert to Boolean", String.valueOf(raw)));
    }

    @Override
    public Timestamp risingTimestamp(Object raw) {
        return null;
    }
}
