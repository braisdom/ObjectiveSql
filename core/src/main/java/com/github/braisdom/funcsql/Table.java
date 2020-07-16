package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.util.WordUtil;

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

    public static final String encodeDefaultKey(String name) {
        return String.format("%s_%s", name, DEFAULT_KEY_SUFFIX);
    }
}
