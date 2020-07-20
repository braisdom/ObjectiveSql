package com.github.braisdom.funcsql.relation;

import com.github.braisdom.funcsql.Table;
import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class Relationship {

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
     * The <code>baseClass</code> is relevant by RelationType and is relative, returns parent table class
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
                return Class.forName((genericTypes[0]).getTypeName());
            } catch (ClassNotFoundException e) {
                throw new RelationalException(e.getMessage(), e);
            }
        } else
            return relationField.getType();
    }

    public String getRelationCondition() {
        return relation.condition();
    }

    public String getPrimaryKey() {
        if (StringUtil.isBlank(relation.primaryKey())) {
            if (isBelongsTo())
                return Table.getPrimaryKey(getBaseClass());
            else
                return Table.getPrimaryKey(getRelatedClass());
        } else
            return relation.primaryKey();
    }

    public String getForeignKey() {
        if (StringUtil.isBlank(relation.foreignKey())) {
            if (isBelongsTo()) {
                String rawForeignKey = baseClass.getSimpleName();
                return Table.encodeDefaultKey(WordUtil.underscore(rawForeignKey));
            } else {
                String rawForeignKey = getRelatedClass().getSimpleName();
                return Table.encodeDefaultKey(WordUtil.underscore(rawForeignKey));
            }
        } else
            return relation.foreignKey();
    }

    public String getBaseFieldName() {
        if (StringUtil.isBlank(relation.foreignFieldName())) {
            return relationField.getName();
        } else
            return relation.primaryFieldName();
    }

    public String getAssociatedFieldName() {
        if (StringUtil.isBlank(relation.foreignFieldName())) {
            if (StringUtil.isBlank(relation.foreignKey())) {
                if (isBelongsTo()) {
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

    public boolean isBelongsTo() {
        return RelationType.BELONGS_TO.equals(relation.relationType());
    }

    public static final Object getAssociatedValue(Object row, String fieldName) {
        Class clazz = row.getClass();
        try {
            return PropertyUtils.getProperty(row, fieldName);
        } catch (IllegalAccessException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s from %s access error", fieldName, clazz.getSimpleName())), e);
        } catch (InvocationTargetException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s from % invocation error", fieldName, clazz.getSimpleName())), e);
        } catch (NoSuchMethodException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("The %s has no %s error", clazz.getSimpleName(), fieldName)), e);
        }
    }

    public static void setRelationalObjects(Relationship relationship, Object row, String fieldName, List associatedObjects) {
        if (relationship.isBelongsTo()) {
            if (associatedObjects.size() > 1)
                throw new RelationalException(String.format("The %s[belongs_to] has too many relations", fieldName));

            if (associatedObjects.size() == 1)
                invokeWriteMethod(row, fieldName, associatedObjects.get(0));
            else
                invokeWriteMethod(row, fieldName, null);
        } else {
            invokeWriteMethod(row, fieldName, associatedObjects);
        }
    }

    private static void invokeWriteMethod(Object row, String fieldName, Object value) {
        try {
            PropertyUtils.setProperty(row, fieldName, value);
        } catch (IllegalAccessException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s access error", fieldName)), e);
        } catch (InvocationTargetException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s invocation error", fieldName)), e);
        } catch (NoSuchMethodException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s unknown error (%s)", fieldName, e.getMessage())), e);
        }
    }

    public static final Relationship createRelation(Class baseClass, String fieldName) {
        try {
            Field field = baseClass.getDeclaredField(fieldName);
            Relation relation = field.getAnnotation(Relation.class);
            if (relation == null)
                throw new RelationalException(String.format("The %s has not relation", field));
            return new Relationship(baseClass, field, relation);
        } catch (NoSuchFieldException ex) {
            throw new RelationalException(String.format("The %s has no field '%s' (%s)", baseClass.getSimpleName(),
                    fieldName, ex.getMessage()), ex);
        }
    }
}
