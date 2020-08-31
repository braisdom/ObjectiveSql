package com.github.braisdom.funcsql.sql;

import com.github.braisdom.funcsql.annotations.DomainModel;

import java.util.Objects;

public abstract class AbstractDataset<T> implements Dataset<T> {

    private final DomainModel domainModel;
    private final Class<T> modelClass;

    public AbstractDataset(Class<T> modelClass) {
        Objects.requireNonNull(modelClass, "The modelClass cannot be null");
        this.modelClass = modelClass;
        this.domainModel = modelClass.getAnnotation(DomainModel.class);
        Objects.requireNonNull(domainModel, "The modelClass must have DomainModel annotation");
    }
}
