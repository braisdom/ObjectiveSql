package com.github.braisdom.funcsql.osql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.osql.expression.AbstractExpression;

import java.util.Arrays;
import java.util.Objects;

public class Table extends AbstractExpression {

    protected final DomainModel domainModel;
    protected final Class modelClass;

    public Table(Class modelClass) {
        Objects.requireNonNull(modelClass, "The modelClass cannot be null");
        this.modelClass = modelClass;
        this.domainModel = (DomainModel) modelClass.getAnnotation(DomainModel.class);
        Objects.requireNonNull(domainModel, "The modelClass must have DomainModel annotation");
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        String[] nameParts = domainModel.tableName().split(".");
        String[] quotedNameParts = Arrays.stream(nameParts)
                .map(namePart -> expressionContext.quoteTable(namePart)).toArray(String[]::new);
        return String.join(".", quotedNameParts);
    }
}
