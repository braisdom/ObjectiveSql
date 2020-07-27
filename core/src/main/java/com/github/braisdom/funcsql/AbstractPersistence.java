package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Volatile;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractPersistence<T> implements Persistence<T> {

    private static final String INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String UPDATE_STATEMENT = "UPDATE %s SET %s WHERE %s";
    private static final String DELETE_STATEMENT = "DELETE FROM %s WHERE %s";

    private final static List<Class> COLUMNIZABLE_FIELD_TYPES = Arrays.asList(new Class[]{
            String.class, char.class,
            Long.class, long.class,
            Integer.class, int.class,
            Short.class, short.class,
            Float.class, float.class,
            Double.class, double.class
    });

    protected final Class<T> domainModelClass;

    public AbstractPersistence(Class<T> domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    protected Field[] getInsertableFields(Class domainModelClass) throws PersistenceException {
        return getColumnizableFields(domainModelClass, true, false);
    }

    protected Field[] getUpdatableFields(Class domainModelClass) throws PersistenceException {
        return getColumnizableFields(domainModelClass, false, true);
    }

    protected Field getPrimaryField(Class domainModelClass) throws PersistenceException {
        return this.getPrimaryField(domainModelClass, true);
    }

    protected Field getPrimaryField(Class domainModelClass, boolean ensureExists) throws PersistenceException {
        Field primaryField = Table.getPrimaryField(domainModelClass);
        if (primaryField == null && ensureExists)
            throw new PersistenceException(String.format("The %s has no primary field", domainModelClass.getSimpleName()));

        return primaryField;
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

    protected Field[] getColumnizableFields(Class domainModelClass, boolean insertable, boolean updatable) throws PersistenceException {
        DomainModel domainModel = (DomainModel) domainModelClass.getAnnotation(DomainModel.class);
        if (domainModel == null)
            throw new PersistenceException(String.format("The %s has no DomainModel annotation", domainModelClass.getSimpleName()));
        Field primaryField = Table.getPrimaryField(domainModelClass);
        Field[] fields = domainModelClass.getDeclaredFields();

        if (domainModel.allFieldsPersistent()) {
            return Arrays.stream(fields).filter(field -> {
                Column column = field.getAnnotation(Column.class);
                Volatile volatileAnnotation = field.getAnnotation(Volatile.class);
                if (volatileAnnotation == null) {
                    if (column == null)
                        return isColumnizable(field) && !field.equals(primaryField);
                    else
                        return insertable ? column.insertable() :
                                (updatable && column.updatable() && !field.equals(primaryField));
                } else return false;
            }).toArray(Field[]::new);
        } else {
            return Arrays.stream(fields).filter(field -> {
                Column column = field.getAnnotation(Column.class);
                Volatile volatileAnnotation = field.getAnnotation(Volatile.class);
                if (volatileAnnotation == null) {
                    if (column == null)
                        return false;
                    else
                        return insertable ? column.insertable() :
                                (updatable && column.updatable() && !field.equals(primaryField));
                } else return false;
            }).toArray(Field[]::new);
        }
    }

    protected boolean hasColumnAnnotation(Field field) {
        return field.getAnnotation(Column.class) != null;
    }

    protected boolean isColumnizable(Field field) {
        return COLUMNIZABLE_FIELD_TYPES.contains(field.getType());
    }
}
