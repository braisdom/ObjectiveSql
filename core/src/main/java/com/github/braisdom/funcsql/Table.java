package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 *
 * @author braisdom
 * @since 1.0
 */
public final class Table {

    public static final String DEFAULT_PRIMARY_KEY = "id";
    public static final String DEFAULT_KEY_SUFFIX = "id";

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

    public static final PrimaryKey getPrimaryKey(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        for(Field field:fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if(primaryKey != null) {
                return primaryKey;
            }
        }
        return null;
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

        return primaryField == null ? defaultField : primaryField;
    }

    public static final String encodeDefaultKey(String name) {
        return String.format("%s_%s", name, DEFAULT_KEY_SUFFIX);
    }
}
