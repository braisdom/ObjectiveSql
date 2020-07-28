package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Volatile;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class BeanModelDescriptor implements DomainModelDescriptor {

    private final static List<Class> COLUMNIZABLE_FIELD_TYPES = Arrays.asList(new Class[]{
            String.class, char.class,
            Long.class, long.class,
            Integer.class, int.class,
            Short.class, short.class,
            Float.class, float.class,
            Double.class, double.class
    });

    private final Class<?> domainModelClass;

    public BeanModelDescriptor(Class<?> domainModelClass) {
        Objects.requireNonNull(domainModelClass, "The domainModelClass cannot be null");

        this.domainModelClass = domainModelClass;
    }

    @Override
    public String getTableName() {
        return Table.getTableName(domainModelClass);
    }

    @Override
    public String getPrimaryKey() {
        return Table.getPrimaryKey(domainModelClass);
    }

    @Override
    public String[] getColumnNames(boolean insertable, boolean updatable) {
        return Arrays.stream(getColumnizableFields(domainModelClass, insertable, updatable))
                .map(field -> getColumnName(field)).toArray(String[]::new);
    }

    @Override
    public Object getValue(Object modelObject, String columnName) {
        return PropertyUtils.readDirectly(modelObject, columnName);
    }

    @Override
    public void setValue(Object modelObject, String columnName, Object columnValue) {
        PropertyUtils.writeDirectly(modelObject, columnName, columnValue);
    }

    @Override
    public ColumnTransition getColumnTransition(String columnName) {
        return null;
    }

    protected Field[] getColumnizableFields(Class domainModelClass, boolean insertable, boolean updatable) {
        DomainModel domainModel = (DomainModel) domainModelClass.getAnnotation(DomainModel.class);
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

    protected String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if(column != null && !StringUtil.isBlank(column.name()))
            return column.name();
        else return WordUtil.underscore(field.getName());
    }

    protected boolean isColumnizable(Field field) {
        return COLUMNIZABLE_FIELD_TYPES.contains(field.getType());
    }
}
