package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;

public final class Table {

    public static final String DEFAULT_PRIMARY_KEY = "id";
    public static final String DEFAULT_KEY_SUFFIX = "id";

    public static final String getTableName(Class baseClass) {
        String tableName;
        DomainModel domainModel = (DomainModel) (baseClass == null
                ? null : baseClass.getAnnotation(DomainModel.class));

        if (domainModel != null)
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

    public static final String encodeDefaultKey(String name) {
        return String.format("%s_%s", name, DEFAULT_KEY_SUFFIX);
    }
}
