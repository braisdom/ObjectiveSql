package com.github.braisdom.funcsql.util;

import com.github.braisdom.funcsql.RelationType;
import com.github.braisdom.funcsql.Relationship;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Relation;

public final class AnnotationUtil {

    public static final String getPrimaryKey(Relation relation) {
        if (StringUtil.isBlank(relation.primaryKey())) {
            return Relationship.DEFAULT_PRIMARY_KEY;
        } else
            return relation.primaryKey();
    }

    public static final String getForeignKey(Class baseClass, Class relationClass, Relation relation) {
        if (StringUtil.isBlank(relation.foreignKey())) {
            if (RelationType.HAS_MANY.equals(relation.relationType())
                    || RelationType.HAS_ONE.equals(relation.relationType())) {
                String rawForeignKey = baseClass.getSimpleName();
                return String.format("%s_%s", WordUtil.underscore(rawForeignKey), Relationship.DEFAULT_PRIMARY_KEY);
            } else {
                String rawForeignKey = relationClass.getSimpleName();
                return String.format("%s_%s", WordUtil.underscore(rawForeignKey), Relationship.DEFAULT_PRIMARY_KEY);
            }
        } else
            return relation.foreignKey();
    }

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
}
