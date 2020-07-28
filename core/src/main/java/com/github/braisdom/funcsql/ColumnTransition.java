package com.github.braisdom.funcsql;

public interface ColumnTransition<T> {

    default Object sinking(T object, DomainModelDescriptor domainModelDescriptor, String fieldName, Object fieldValue) {
        return fieldValue;
    }

    default Object rising(T object, DomainModelDescriptor domainModelDescriptor, String fieldName, Object fieldValue) {
        return fieldValue;
    }
}
