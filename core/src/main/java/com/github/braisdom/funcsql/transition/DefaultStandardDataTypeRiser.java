package com.github.braisdom.funcsql.transition;

public class DefaultStandardDataTypeRiser implements StandardDataTypeRiser {

    @Override
    public Float risingFloat(Object lower) {
        if(lower instanceof Float)
            return (Float) lower;
        if(lower instanceof Double)
            return Float.class.cast(lower);
        if(lower instanceof Integer)
            return Float.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("%s cannot convert to Float", String.valueOf(lower)));
    }

    @Override
    public Double risingDouble(Object lower) {
        if(lower instanceof Float)
            return (Double) lower;
        if(lower instanceof Float)
            return Double.class.cast(lower);
        if(lower instanceof Integer)
            return Double.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("%s cannot convert to Double", String.valueOf(lower)));
    }

    @Override
    public Short risingShort(Object lower) {
        if(lower instanceof Short)
            return (Short) lower;
        if(lower instanceof Integer)
            return Short.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("%s cannot convert to Short", String.valueOf(lower)));
    }

    @Override
    public Integer risingInteger(Object lower) {
        if(lower instanceof Integer)
            return (Integer) lower;
        if(lower instanceof Long)
            return Integer.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("%s cannot convert to Integer", String.valueOf(lower)));
    }

    @Override
    public Long risingLong(Object lower) {
        if(lower instanceof Long)
            return (Long) lower;
        if(lower instanceof Integer)
            return Long.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("%s cannot convert to Long", String.valueOf(lower)));
    }

    @Override
    public Boolean risingBoolean(Object lower) {
        if(lower instanceof Integer)
            return ((Integer)lower) == 1;
        if(lower instanceof Short)
            return ((Short)lower) == 1;
        else
            throw new TransitionException(String.format("%s cannot convert to Boolean", String.valueOf(lower)));
    }

    @Override
    public <T extends Enum<T>> T risingEnum(Class<T> clazz,  Object lower) {
        return Enum.valueOf(clazz, String.valueOf(lower));
    }
}
