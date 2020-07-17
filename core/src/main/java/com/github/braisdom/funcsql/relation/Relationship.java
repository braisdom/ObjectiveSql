package com.github.braisdom.funcsql.relation;

import com.github.braisdom.funcsql.Table;
import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;

public class Relationship {

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

    /**
     * The baseClass is relevant by RelationType and is relative, returns parent table class
     * when the RelationType is 'has many' or 'has one', otherwise, child table class.
     *
     * @return the parent table class or child table class
     * @see RelationType
     */
    public Class getBaseClass() {
        return baseClass;
    }

    public Class getRelatedClass() {
        if (Collection.class.isAssignableFrom(relationField.getType())) {
            ParameterizedType parameterizedType = (ParameterizedType) relationField.getGenericType();
            Type[] genericTypes = parameterizedType.getActualTypeArguments();

            if (genericTypes.length == 0)
                throw new RelationalException(String.format("The %s of %s has no generic type",
                        relationField.getName(), getBaseClass().getSimpleName()));

            try {
                return Class.forName(((Type) genericTypes[0]).getTypeName());
            } catch (ClassNotFoundException e) {
                throw new RelationalException(e.getMessage(), e);
            }
        }
        return relationField.getType();
    }

    public String getPrimaryKey() {
        if (StringUtil.isBlank(relation.primaryKey())) {
            if(isPrimaryRelation())
                return Table.getPrimaryKey(getBaseClass());
            else
                return Table.getPrimaryKey(getRelatedClass());
        } else
            return relation.primaryKey();
    }

    public String getForeignKey() {
        if (StringUtil.isBlank(relation.foreignKey())) {
            if (isPrimaryRelation()) {
                String rawForeignKey = baseClass.getSimpleName();
                return Table.encodeDefaultKey(WordUtil.underscore(rawForeignKey));
            } else {
                String rawForeignKey = getRelatedClass().getSimpleName();
                return Table.encodeDefaultKey(WordUtil.underscore(rawForeignKey));
            }
        } else
            return relation.foreignKey();
    }

    public String getPrimaryFieldName() {
        if (StringUtil.isBlank(relation.foreignFieldName())) {
            if(isPrimaryRelation())
                return Table.getPrimaryField(getBaseClass()).getName();
            else
                return Table.getPrimaryField(getRelatedClass()).getName();
        }else
            return relation.primaryFieldName();
    }

    public String getForeignFieldName() {
        if (StringUtil.isBlank(relation.foreignFieldName())) {
            if (StringUtil.isBlank(relation.foreignKey())) {
                if (isPrimaryRelation()) {
                    String rawForeignName = getBaseClass().getSimpleName();
                    return WordUtil.camelize(rawForeignName, true);
                } else {
                    String rawForeignName = getRelatedClass().getSimpleName();
                    return WordUtil.camelize(rawForeignName, true);
                }
            } else
                return WordUtil.camelize(relation.foreignKey(), true);
        } else
            return relation.foreignFieldName();
    }

    protected boolean isPrimaryRelation() {
        return RelationType.HAS_MANY.equals(relation.relationType())
                || RelationType.HAS_ONE.equals(relation.relationType());
    }

    public static final Relationship createRelation(Class baseClass, String fieldName) {
        try {
            Field field = baseClass.getDeclaredField(fieldName);
            Relation relation = field.getAnnotation(Relation.class);
            if(relation == null)
                throw new RelationalException(String.format("The %s has not relation", field));
            return new Relationship(baseClass, field, relation);
        } catch (NoSuchFieldException ex) {
            throw new RelationalException(String.format("The %s has no field '%s' (%s)", baseClass.getSimpleName(),
                    fieldName, ex.getMessage()), ex);
        }
    }
}
