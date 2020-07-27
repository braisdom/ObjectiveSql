package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Table {

    public static final String DEFAULT_PRIMARY_KEY = "id";
    public static final String DEFAULT_KEY_SUFFIX = "id";

    private static final Map<Class, ColumnValueIntervenor> COLUMN_VALUE_ADJUSTERS = new HashMap();

    private static ColumnValueIntervenor DEFAULT_COLUMN_VALUE_INTERVENOR = new ColumnValueIntervenor() {
        @Override
        public Object sleeping(Field field, Object value) {
            return value;
        }

        @Override
        public Object waking(Field field, Object value) {
            return value;
        }
    };

    public static final String getTableName(Class baseClass) {
        Objects.requireNonNull(baseClass, "The baseClass cannot be null");
        DomainModel domainModel = (DomainModel) baseClass.getAnnotation(DomainModel.class);

        Objects.requireNonNull(domainModel, "The baseClass must have the DomainModel annotation");

        String tableName;
        if (!StringUtil.isBlank(domainModel.tableName()))
            tableName = domainModel.tableName();
        else
            tableName = WordUtil.tableize(baseClass.getSimpleName());

        return tableName;
    }

    public static final String getPrimaryKey(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        for(Field field:fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if(primaryKey != null) {
                if(StringUtil.isBlank(primaryKey.value()))
                    return WordUtil.underscore(field.getName());
                else
                    return primaryKey.value();
            }
        }
        return Table.DEFAULT_PRIMARY_KEY;
    }

    public static final boolean isPrimaryField(Field field) {
        PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
        if(primaryKey != null)
            return true;
        else return DEFAULT_PRIMARY_KEY.equals(field.getName());
    }

    public static final Field getPrimaryField(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();

        Field defaultField = null;
        Field primaryField = null;

        for(Field field:fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (primaryKey != null)
                primaryField = field;

            if(DEFAULT_PRIMARY_KEY.equals(field.getName()))
                defaultField = field;
        }

        return defaultField == null ? primaryField : defaultField;
    }

    public static final void installColumnValueIntervenor(Class clazz, ColumnValueIntervenor valueIntervenor) {
        Objects.requireNonNull(clazz, "The clazz cannot be null");
        Objects.requireNonNull(valueIntervenor, "The valueIntervenor cannot be null");

        COLUMN_VALUE_ADJUSTERS.put(clazz, valueIntervenor);
    }

    public static final ColumnValueIntervenor getColumnValueIntervenor(Class clazz) {
        ColumnValueIntervenor columnValueIntervenor = COLUMN_VALUE_ADJUSTERS.get(clazz);
        if(columnValueIntervenor == null)
            return DEFAULT_COLUMN_VALUE_INTERVENOR;
        else return columnValueIntervenor;
    }

    public static final String encodeDefaultKey(String name) {
        return String.format("%s_%s", name, DEFAULT_KEY_SUFFIX);
    }
}
