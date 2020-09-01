package com.github.braisdom.funcsql.relation;

import com.github.braisdom.funcsql.DomainModelException;
import com.github.braisdom.funcsql.Tables;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import java.lang.reflect.Field;
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

    public Field getRelationField() {
        return relationField;
    }

    public String getRelationCondition() {
        return relation.condition();
    }

    public String getPrimaryKey() {
        if (StringUtil.isBlank(relation.primaryKey())) {
            if (isBelongsTo()) {
                PrimaryKey primaryKey = Tables.getPrimaryKey(getBaseClass());
                if(primaryKey == null)
                    throw new DomainModelException(String.format("The %s has no primary key", getBaseClass().getSimpleName()));
                return primaryKey.name();
            } else {
                PrimaryKey primaryKey = Tables.getPrimaryKey(getRelatedClass());
                if(primaryKey == null)
                    throw new DomainModelException(String.format("The %s has no primary key", getRelatedClass().getSimpleName()));
                return primaryKey.name();
            }
        } else
            return relation.primaryKey();
    }

    public String getForeignKey() {
        if (StringUtil.isBlank(relation.foreignKey())) {
            if (isBelongsTo()) {
                String rawForeignKey = getRelatedClass().getSimpleName();
                return Tables.encodeDefaultKey(WordUtil.underscore(rawForeignKey));
            } else {
                String rawForeignKey = baseClass.getSimpleName();
                return Tables.encodeDefaultKey(WordUtil.underscore(rawForeignKey));
            }
        } else
            return relation.foreignKey();
    }

    public String getPrimaryAssociationFieldName() {
        if (StringUtil.isBlank(relation.primaryFieldName())) {
            if(isBelongsTo())
                return Tables.getPrimaryField(getRelatedClass()).getName();
            else
                return Tables.getPrimaryField(getBaseClass()).getName();
        } else
            return relation.primaryFieldName();
    }

    public String getForeignFieldName() {
        if (StringUtil.isBlank(relation.foreignFieldName())) {
            if (isBelongsTo()) {
                String rawForeignFieldName = WordUtil.underscore(getRelatedClass().getSimpleName());
                return WordUtil.camelize(Tables.encodeDefaultKey(rawForeignFieldName), true);
            } else {
                String rawForeignFieldName = WordUtil.underscore(getBaseClass().getSimpleName());
                return WordUtil.camelize(Tables.encodeDefaultKey(rawForeignFieldName), true);
            }
        } else
            return relation.foreignFieldName();
    }

    public boolean isBelongsTo() {
        return RelationType.BELONGS_TO.equals(relation.relationType());
    }

    public RelationProcessor createProcessor() {
        if(isBelongsTo())
            return new BelongsToProcessor();
        return new HasAnyProcessor();
    }

    public static void setRelationalObjects(Relationship relationship, Object row, String fieldName, List associatedObjects) {
        if (relationship.isBelongsTo()) {
            if (associatedObjects.size() > 1)
                throw new RelationalException(String.format("The %s[belongs_to] has too many relations", fieldName));

            if (associatedObjects.size() == 1)
                PropertyUtils.writeDirectly(row, fieldName, associatedObjects.get(0));
            else
                PropertyUtils.writeDirectly(row, fieldName, null);
        } else {
            PropertyUtils.writeDirectly(row, fieldName, associatedObjects);
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
