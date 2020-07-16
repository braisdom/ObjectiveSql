package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
import java.util.Objects;

public class RelationDefinition {

    public static final String DEFAULT_PRIMARY_KEY = "id";

    private final Field relationField;
    private final Relation relation;

    public RelationDefinition(Field relationField, Relation relation) {
        Objects.requireNonNull(relationField, "The relationField cannot be null");
        Objects.requireNonNull(relation, String.format("The %s has no relation annotation",
                relationField.getName()));
        this.relationField = relationField;
        this.relation = relation;
    }

    public String getPrimaryKey() {
        if (StringUtil.isBlank(relation.primaryKey()))
            return DEFAULT_PRIMARY_KEY;
        else
            return relation.primaryKey();
    }

    public String getForeignKey() {
        if (StringUtil.isBlank(relation.foreignKey())) {
            if(RelationType.HAS_MANY.equals(relation.relationType())) {
                String typeName = relationField.getType().getGenericSuperclass().getTypeName();
            }else {
                return WordUtil.underscore(relationField.getName());
            }
        } else
            return relation.foreignKey();
        return null;
    }

    public static final RelationDefinition createRelation(Class clazz, String fieldName) {
        try {
            Field field = clazz.getField(fieldName);
            Relation relation = field.getAnnotation(Relation.class);
            return new RelationDefinition(field, relation);
        } catch (NoSuchFieldException ex) {
            throw new RelationException(String.format("The %s has no field '%s' (%s)", clazz.getName(),
                    fieldName, ex.getMessage()), ex);
        }
    }
}
