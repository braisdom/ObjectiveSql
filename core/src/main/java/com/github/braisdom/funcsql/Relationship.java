package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.util.AnnotationUtil;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
import java.util.Objects;

public class Relationship {

    public static final String DEFAULT_PRIMARY_KEY = "id";

    private final Class baseClass;
    private final Field relationField;
    private final Relation relation;

    public Relationship(Class baseClass, Field relationField, Relation relation) {
        Objects.requireNonNull(relationField, "The relationField cannot be null");
        Objects.requireNonNull(relation, String.format("The %s has no relation annotation",
                relationField.getName()));

        this.baseClass = baseClass;
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
        return relationField.getName();
    }

    public String getPrimaryKey() {
        return AnnotationUtil.getPrimaryKey(relation);
    }

    public String getForeignKey() {
        return AnnotationUtil.getForeignKey(baseClass, getRelationClass(), relation);
    }

    public static final Relationship createRelation(Class baseClass, String fieldName) {
        try {
            Field field = baseClass.getDeclaredField(fieldName);
            Relation relation = field.getAnnotation(Relation.class);
            return new Relationship(baseClass, field, relation);
        } catch (NoSuchFieldException ex) {
            throw new RelationException(String.format("The %s has no field '%s' (%s)", baseClass.getSimpleName(),
                    fieldName, ex.getMessage()), ex);
        }
    }
}
