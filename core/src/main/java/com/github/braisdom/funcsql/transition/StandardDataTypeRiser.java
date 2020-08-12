package com.github.braisdom.funcsql.transition;

public interface StandardDataTypeRiser {

    Float risingFloat(Object lower);

    Double risingDouble(Object lower);

    Short risingShort(Object lower);

    Integer risingInteger(Object lower);

    Long risingLong(Object lower);

    Boolean risingBoolean(Object lower);

    public <T extends Enum<T>> T risingEnum(Class<T> clazz,  Object lower);
}
