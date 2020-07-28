package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.annotations.Volatile;
import com.github.braisdom.funcsql.reflection.ClassUtils;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
import java.util.*;

public class BeanModelDescriptor<T> implements DomainModelDescriptor<T> {

    private final static List<Class> COLUMNIZABLE_FIELD_TYPES = Arrays.asList(new Class[]{
            String.class, char.class,
            Long.class, long.class,
            Integer.class, int.class,
            Short.class, short.class,
            Float.class, float.class,
            Double.class, double.class
    });

    private final Class<T> domainModelClass;
    private final Map<String, ColumnTransition> columnTransitionMap;
    private final Map<String, Field> columnToField;

    public BeanModelDescriptor(Class<T> domainModelClass) {
        Objects.requireNonNull(domainModelClass, "The domainModelClass cannot be null");

        this.domainModelClass = domainModelClass;
        this.columnTransitionMap = new HashMap<>();
        this.columnToField = new HashMap<>();

        prepareColumnToPropertyOverrides(domainModelClass);
        instantiateColumnTransitionMap(domainModelClass.getDeclaredFields());
    }

    @Override
    public T newInstance() {
        return ClassUtils.createNewInstance(domainModelClass);
    }

    @Override
    public Class getDomainModelClass() {
        return domainModelClass;
    }

    @Override
    public DomainModelDescriptor getRelatedModeDescriptor(Class relatedClass) {
        return new BeanModelDescriptor(relatedClass);
    }

    @Override
    public String[] getColumns() {
        return Arrays.stream(getColumnizableFields(domainModelClass, true, true))
                .map(field -> getColumnName(field)).toArray(String[]::new);
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
    public Object getPrimaryValue(Object domainObject) {
        return PropertyUtils.readDirectly(domainObject, getPrimaryKey());
    }

    @Override
    public String[] getInsertableColumns() {
        return Arrays.stream(getColumnizableFields(domainModelClass, true, false))
                .map(field -> getColumnName(field)).toArray(String[]::new);
    }

    @Override
    public String[] getUpdatableColumns() {
        return Arrays.stream(getColumnizableFields(domainModelClass, false, true))
                .map(field -> getColumnName(field)).toArray(String[]::new);
    }

    @Override
    public String getFieldName(String fieldName) {
        Field field = columnToField.get(fieldName);
        return field == null ? null : field.getName();
    }

    @Override
    public Class getFieldType(String columnName) {
        return columnToField.get(columnName).getType();
    }

    @Override
    public Object getValue(T modelObject, String fieldName) {
        return PropertyUtils.readDirectly(modelObject, fieldName);
    }

    @Override
    public void setValue(T modelObject, String fieldName, Object fieldValue) {
        PropertyUtils.writeDirectly(modelObject, fieldName, fieldValue);
    }

    @Override
    public ColumnTransition getColumnTransition(String fieldName) {
        return columnTransitionMap.get(fieldName);
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
                        return isColumnizable(field);
                    else {
                        return ensureColumnizable(column, field, primaryField, insertable, updatable);
                    }
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
                        return ensureColumnizable(column, field, primaryField, insertable, updatable);
                } else return false;
            }).toArray(Field[]::new);
        }
    }

    protected String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null && !StringUtil.isBlank(column.name()))
            return column.name();
        else return WordUtil.underscore(field.getName());
    }

    protected boolean isColumnizable(Field field) {
        return COLUMNIZABLE_FIELD_TYPES.contains(field.getType());
    }

    private boolean ensureColumnizable(Column column, Field field, Field primaryField,
                                       boolean insertable, boolean updatable) {
        if (insertable && updatable)
            return true;
        else if (insertable) {
            return column.insertable();
        } else if (updatable) {
            return (updatable && column.updatable() && !field.equals(primaryField));
        } else return false;
    }

    private void prepareColumnToPropertyOverrides(Class<T> rowClass) {
        Field[] fields = rowClass.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            Column column = field.getAnnotation(Column.class);

            if (primaryKey != null) {
                String columnName = StringUtil.isBlank(primaryKey.name())
                        ? WordUtil.underscore(field.getName()) : primaryKey.name();
                columnToField.put(columnName, field);
            } else if (column != null) {
                String columnName = StringUtil.isBlank(column.name())
                        ? WordUtil.underscore(field.getName()) : primaryKey.name();
                columnToField.put(columnName, field);
            } else {
                columnToField.put(WordUtil.underscore(field.getName()), field);
            }
        });
    }

    private Map<String, ColumnTransition> instantiateColumnTransitionMap(Field[] fields) {
        Arrays.stream(fields).forEach(field -> {
            Column column = field.getAnnotation(Column.class);
            if (column != null && !column.transition().equals(ColumnTransition.class))
                columnTransitionMap.put(field.getName(), ClassUtils.createNewInstance(column.transition()));
        });

        return columnTransitionMap;
    }
}
