package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.BelongsTo;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.annotations.RelatedTo;

import java.util.Arrays;
import java.util.Objects;

public class RelationDefinition {

    private RelationType relationType;
    private Class relatedClass;
    private String conditions;

    public RelationDefinition(RelationType relationType, Class relatedClass) {
        Objects.requireNonNull(relatedClass, "The relationType cannot be null");
        Objects.requireNonNull(relatedClass, "The base cannot be null");

        this.relationType = relationType;
        this.relatedClass = relatedClass;
    }

    public String getBelongsToPrimaryKey(Class baseClass) {
        BelongsTo[] primaryKeys = (BelongsTo[]) baseClass.getAnnotationsByType(BelongsTo.class);
        if (primaryKeys.length == 1)
            return primaryKeys[0].primaryKey();
        else if (primaryKeys.length > 0) {
            BelongsTo[] rawPrimaryKeys = (BelongsTo[]) Arrays.stream(primaryKeys)
                    .filter(primaryKey -> primaryKey.base().equals(baseClass))
                    .toArray();
            if (rawPrimaryKeys.length == 1)
                return rawPrimaryKeys[0].primaryKey();
            else
                throw new RelationException("Cannot find belongs primary key for " + baseClass.getName()
                        + " from " + relatedClass.getName());
        } else
            throw new RelationException("Cannot find belongs primary key for " + baseClass.getName()
                    + " from " + relatedClass.getName());
    }

    public String getBelongsToForeignKey(Class baseClass) {
        BelongsTo[] primaryKeys = (BelongsTo[]) baseClass.getAnnotationsByType(BelongsTo.class);
        if (primaryKeys.length == 1)
            return primaryKeys[0].foreignKey();
        else if (primaryKeys.length > 0) {
            BelongsTo[] rawPrimaryKeys = (BelongsTo[]) Arrays.stream(primaryKeys)
                    .filter(primaryKey -> primaryKey.base().equals(baseClass))
                    .toArray();
            if (rawPrimaryKeys.length == 1)
                return rawPrimaryKeys[0].foreignKey();
            else
                throw new RelationException("Cannot find belongs primary key for " + baseClass.getName()
                        + " from " + relatedClass.getName());
        } else
            throw new RelationException("Cannot find belongs primary key for " + baseClass.getName()
                    + " from " + relatedClass.getName());
    }

    public String getPrimaryKey(Class baseClass) {
        PrimaryKey[] primaryKeys = (PrimaryKey[]) baseClass.getAnnotationsByType(PrimaryKey.class);

        if (primaryKeys.length == 1)
            return primaryKeys[0].value();
        else if (primaryKeys.length > 0) {
            PrimaryKey[] rawPrimaryKeys = (PrimaryKey[]) Arrays.stream(primaryKeys)
                    .filter(primaryKey -> primaryKey.relatedClass().equals(baseClass))
                    .toArray();
            if (rawPrimaryKeys.length == 1)
                return rawPrimaryKeys[0].value();
            else
                throw new RelationException("Cannot find primary key for " + baseClass.getName()
                        + " from " + relatedClass.getName());
        } else
            throw new RelationException("Cannot find primary key for " + baseClass.getName()
                    + " from " + relatedClass.getName());
    }

    public String getForeignKey(Class parentClass) {
        RelatedTo[] relatedTos = (RelatedTo[]) relatedClass.getAnnotationsByType(RelatedTo.class);

        if (relatedTos.length == 1)
            return relatedTos[0].foreignKey();
        else if (relatedTos.length > 0) {
            RelatedTo[] rawForeignKeys = (RelatedTo[]) Arrays.stream(relatedTos)
                    .filter(primaryKey -> primaryKey.base().equals(parentClass))
                    .toArray();
            if (rawForeignKeys.length == 1)
                return rawForeignKeys[0].foreignKey();
            else
                throw new RelationException("Cannot find foreign key for " + parentClass.getName()
                        + " from " + relatedClass.getName());
        } else
            throw new RelationException("Cannot find foreign key for " + parentClass.getName()
                    + " from " + relatedClass.getName());
    }

    public RelationType getRelationType() {
        return relationType;
    }


    public Class getRelatedClass() {
        return relatedClass;
    }

    public String getConditions() {
        return conditions;
    }

    public RelationDefinition setConditions(String conditions) {
        this.conditions = conditions;
        return this;
    }
}
