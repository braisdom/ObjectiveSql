package com.github.braisdom.objsql;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractPersistence<T> implements Persistence<T> {

    private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String UPDATE_STATEMENT = "UPDATE %s SET %s WHERE %s";
    private static final String DELETE_STATEMENT = "DELETE FROM %s WHERE %s";

    protected final DomainModelDescriptor domainModelDescriptor;

    public AbstractPersistence(Class<T> domainClass) {
        this(new BeanModelDescriptor(domainClass));
    }

    public AbstractPersistence(DomainModelDescriptor domainModelDescriptor) {
        Objects.requireNonNull(domainModelDescriptor, "The domainModelDescriptor cannot be null");

        this.domainModelDescriptor = domainModelDescriptor;
    }

    protected String formatInsertSql(String tableName, String[] columnNames) {
        String[] valuesPlaceHolder = Arrays.stream(columnNames).map(c -> "?").toArray(String[]::new);
        return formatInsertSql(tableName, columnNames, String.join(",", valuesPlaceHolder));
    }

    protected String formatInsertSql(String tableName, String[] columnNames, String values) {
        return String.format(INSERT_TEMPLATE, tableName, String.join(",", columnNames), values);
    }

    protected String formatUpdateSql(String tableName, String updates, String predicate) {
        return String.format(UPDATE_STATEMENT, tableName, updates, predicate);
    }

    protected String formatDeleteSql(String tableName, String predicate) {
        return String.format(DELETE_STATEMENT, tableName, predicate);
    }
}
