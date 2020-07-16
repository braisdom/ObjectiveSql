package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
import java.util.Objects;

public class RelationDefinition {

    public static final String DEFAULT_PRIMARY_KEY = "id";

    private final Class baseClass;
    private final String fieldName;
    private final Field relationField;
    private final Relation relation;

    public RelationDefinition(Class baseClass, String fieldName, Field relationField, Relation relation) {
        Objects.requireNonNull(relationField, "The relationField cannot be null");
        Objects.requireNonNull(relation, String.format("The %s has no relation annotation",
                relationField.getName()));
        this.baseClass = baseClass;
        this.fieldName = fieldName;
        this.relationField = relationField;
        this.relation = relation;
    }

    public Class getBaseClass() {
        return baseClass;
    }

    public Class getRelationClass() {
        return relationField.getType();
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getPrimaryKey() {
        if (StringUtil.isBlank(relation.primaryKey()))
            return DEFAULT_PRIMARY_KEY;
        else
            return relation.primaryKey();
    }

    public String getForeignKey() {
        if (StringUtil.isBlank(relation.foreignKey())) {
            if (RelationType.HAS_MANY.equals(relation.relationType())
                    || RelationType.HAS_ONE.equals(relation.relationType())) {
                String rawForeignKey = baseClass.getSimpleName();
                return String.format("%s_%s", WordUtil.underscore(rawForeignKey), DEFAULT_PRIMARY_KEY);
            } else {
                String rawForeignKey = relationField.getType().getSimpleName();
                return String.format("%s_%s", WordUtil.underscore(rawForeignKey), DEFAULT_PRIMARY_KEY);
            }
        } else
            return relation.foreignKey();
    }

    public static final RelationDefinition createRelation(Class baseClass, String fieldName) {
        try {
            Field field = baseClass.getDeclaredField(fieldName);
            Relation relation = field.getAnnotation(Relation.class);
            return new RelationDefinition(baseClass, fieldName, field, relation);
        } catch (NoSuchFieldException ex) {
            throw new RelationException(String.format("The %s has no field '%s' (%s)", baseClass.getSimpleName(),
                    fieldName, ex.getMessage()), ex);
        }
    }
}
