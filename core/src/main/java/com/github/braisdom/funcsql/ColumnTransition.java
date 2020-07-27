package com.github.braisdom.funcsql;

import java.lang.reflect.Field;

public interface ColumnTransition<T> {

    default Object sinking(T object, Field field, Object fieldValue) {
        return fieldValue;
    }

    default Object rising(T object, Field field, Object fieldValue) {
        return fieldValue;
    }
}
