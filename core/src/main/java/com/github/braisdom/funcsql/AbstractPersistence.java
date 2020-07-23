package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.DomainModel;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractPersistence<T> implements Persistence<T> {

    private static final String UPDATE_STATEMENT = "UPDATE %s SET %s ";
    private static final String DELETE_STATEMENT = "DELETE FROM %s ";

    private final static List<Class> COLUMNIZABLE_FIELD_TYPES = Arrays.asList(new Class[]{
            String.class, char.class,
            Long.class, long.class,
            Integer.class, int.class,
            Short.class, short.class,
            Float.class, float.class,
            Double.class, double.class
    });

    private final Class<T> domainModelClass;

    public AbstractPersistence(Class<T> domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    public Class<T> getDomainModelClass() {
        return domainModelClass;
    }

    protected Field[] getInsertableFields(Class domainModelClass) throws PersistenceException {
        return getColumnizableFields(domainModelClass, true, false);
    }

    protected Field[] getUpdatableFields(Class domainModelClass) throws PersistenceException {
        return getColumnizableFields(domainModelClass, false, true);
    }

    protected Field getPrimaryField(Class domainModelClass) throws PersistenceException {
        Field primaryField = Table.getPrimaryField(domainModelClass);
        if(primaryField == null)
            throw new PersistenceException(String.format("The %s has no primary field", domainModelClass.getSimpleName()));

        return primaryField;
    }

    protected Field[] getColumnizableFields(Class domainModelClass, boolean insertable, boolean updatable) throws PersistenceException {
        DomainModel domainModel = (DomainModel) domainModelClass.getAnnotation(DomainModel.class);
        if (domainModel == null)
            throw new PersistenceException(String.format("The %s has no DomainModel annotation", domainModelClass.getSimpleName()));

        Field[] fields = domainModelClass.getDeclaredFields();
        if (domainModel.allFieldsPersistent()) {
            return Arrays.stream(fields).filter(field -> {
                Column column = field.getAnnotation(Column.class);
                if(column == null)
                    return isColumnizable(field);
                else
                    return insertable ? column.insertable() : (updatable && column.updatable());
            }).toArray(Field[]::new);
        } else {
            return Arrays.stream(fields).filter(field -> {
                Column column = field.getAnnotation(Column.class);
                if(column == null)
                    return false;
                else
                    return insertable ? column.insertable() : (updatable && column.updatable());
            }).toArray(Field[]::new);
        }
    }

    protected boolean isColumnizable(Field field) {
        return COLUMNIZABLE_FIELD_TYPES.contains(field.getType());
    }
}
